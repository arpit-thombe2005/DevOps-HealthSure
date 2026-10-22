/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.healthsure.service;

import com.healthsure.dto.ClaimSubmissionRequest;
import com.healthsure.dto.DashboardStats;
import com.healthsure.dto.ValidationResult;
import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.ClaimReview;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.exception.AccessDeniedException;
import com.healthsure.exception.InvalidStateTransitionException;
import com.healthsure.exception.ResourceNotFoundException;
import com.healthsure.repository.ClaimRepository;
import com.healthsure.repository.ClaimReviewRepository;
import com.healthsure.service.AuditService;
import com.healthsure.service.ClaimValidationService;
import com.healthsure.service.PolicyService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ClaimService.class);
    private final ClaimRepository claimRepository;
    private final ClaimReviewRepository claimReviewRepository;
    private final PolicyService policyService;
    private final ClaimValidationService validationService;
    private final AuditService auditService;

    @Transactional
    public InsuranceClaim createDraft(ClaimSubmissionRequest request, User submitter) {
        InsurancePolicy policy = this.policyService.findById(request.getPolicyId());
        if (!policy.getPolicyholder().getId().equals(submitter.getId())) {
            throw new AccessDeniedException("You do not own this policy");
        }
        String reference = this.generateClaimReference();
        InsuranceClaim claim = InsuranceClaim.builder().claimReference(reference).policy(policy).submittedBy(submitter).claimType(request.getClaimType()).hospitalName(request.getHospitalName()).doctorName(request.getDoctorName()).treatmentStartDate(request.getTreatmentStartDate()).treatmentEndDate(request.getTreatmentEndDate()).claimedAmount(request.getClaimedAmount()).description(request.getDescription()).additionalNotes(request.getAdditionalNotes()).status(InsuranceClaim.ClaimStatus.DRAFT).build();
        InsuranceClaim saved = (InsuranceClaim)this.claimRepository.save(claim);
        this.auditService.recordEvent(saved, submitter, ClaimAuditEvent.EventType.CLAIM_CREATED, null, InsuranceClaim.ClaimStatus.DRAFT, "Claim draft created");
        if (request.isSubmitNow()) {
            return this.submit(saved.getId(), submitter);
        }
        return saved;
    }

    @Transactional
    public InsuranceClaim submit(Long claimId, User submitter) {
        InsuranceClaim claim = this.findById(claimId);
        this.assertOwner(claim, submitter);
        if (claim.getStatus() != InsuranceClaim.ClaimStatus.DRAFT && claim.getStatus() != InsuranceClaim.ClaimStatus.VALIDATION_FAILED && claim.getStatus() != InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED) {
            throw new InvalidStateTransitionException("Cannot submit claim in status: " + String.valueOf((Object)claim.getStatus()));
        }
        InsuranceClaim.ClaimStatus prev = claim.getStatus();
        claim.setStatus(InsuranceClaim.ClaimStatus.SUBMITTED);
        claim.setSubmittedAt(LocalDateTime.now());
        this.claimRepository.save(claim);
        this.auditService.recordEvent(claim, submitter, ClaimAuditEvent.EventType.CLAIM_SUBMITTED, prev, InsuranceClaim.ClaimStatus.SUBMITTED, "Claim submitted for validation");
        return this.validate(claim.getId(), submitter);
    }

    @Transactional
    public InsuranceClaim validate(Long claimId, User actor) {
        InsuranceClaim claim = this.findById(claimId);
        ValidationResult result = this.validationService.validate(claim);
        InsuranceClaim.ClaimStatus prev = claim.getStatus();
        if (result.isValid()) {
            claim.setStatus(InsuranceClaim.ClaimStatus.UNDER_REVIEW);
            this.auditService.recordEvent(claim, actor, ClaimAuditEvent.EventType.CLAIM_VALIDATED, prev, InsuranceClaim.ClaimStatus.UNDER_REVIEW, "Validation passed. Moved to review queue.");
        } else {
            claim.setStatus(InsuranceClaim.ClaimStatus.VALIDATION_FAILED);
            String errorSummary = String.join((CharSequence)"; ", result.getErrors());
            this.auditService.recordEvent(claim, actor, ClaimAuditEvent.EventType.VALIDATION_FAILED, prev, InsuranceClaim.ClaimStatus.VALIDATION_FAILED, "Validation failed: " + errorSummary);
        }
        return (InsuranceClaim)this.claimRepository.save(claim);
    }

    @Transactional
    public InsuranceClaim approve(Long claimId, User reviewer, String notes, BigDecimal approvedAmount) {
        InsuranceClaim claim = this.findById(claimId);
        this.assertReviewerAccess(claim, reviewer);
        if (claim.getStatus() != InsuranceClaim.ClaimStatus.UNDER_REVIEW) {
            throw new InvalidStateTransitionException("Can only approve claims that are under review");
        }
        InsuranceClaim.ClaimStatus prev = claim.getStatus();
        claim.setStatus(InsuranceClaim.ClaimStatus.APPROVED);
        claim.setReviewedAt(LocalDateTime.now());
        claim.setAssignedReviewer(reviewer);
        claim.setReviewerNotes(notes);
        claim.setApprovedAmount(approvedAmount != null ? approvedAmount : claim.getClaimedAmount());
        this.claimRepository.save(claim);
        this.policyService.applyClaimAmount(claim.getPolicy(), claim.getApprovedAmount());
        ClaimReview review = ClaimReview.builder().claim(claim).reviewer(reviewer).previousStatus(prev).newStatus(InsuranceClaim.ClaimStatus.APPROVED).notes(notes).recommendedAmount(approvedAmount).decisionType(ClaimReview.DecisionType.APPROVED).build();
        this.claimReviewRepository.save(review);
        this.auditService.recordEvent(claim, reviewer, ClaimAuditEvent.EventType.CLAIM_APPROVED, prev, InsuranceClaim.ClaimStatus.APPROVED, "Claim approved. Amount: " + String.valueOf(claim.getApprovedAmount()));
        log.info("Claim {} approved by reviewer {}", (Object)claim.getClaimReference(), (Object)reviewer.getUsername());
        return claim;
    }

    @Transactional
    public InsuranceClaim reject(Long claimId, User reviewer, String reason, String notes) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason is mandatory");
        }
        InsuranceClaim claim = this.findById(claimId);
        this.assertReviewerAccess(claim, reviewer);
        if (claim.getStatus() != InsuranceClaim.ClaimStatus.UNDER_REVIEW) {
            throw new InvalidStateTransitionException("Can only reject claims that are under review");
        }
        InsuranceClaim.ClaimStatus prev = claim.getStatus();
        claim.setStatus(InsuranceClaim.ClaimStatus.REJECTED);
        claim.setReviewedAt(LocalDateTime.now());
        claim.setAssignedReviewer(reviewer);
        claim.setReviewerNotes(notes);
        claim.setRejectionReason(reason);
        this.claimRepository.save(claim);
        ClaimReview review = ClaimReview.builder().claim(claim).reviewer(reviewer).previousStatus(prev).newStatus(InsuranceClaim.ClaimStatus.REJECTED).notes(notes).decisionReason(reason).decisionType(ClaimReview.DecisionType.REJECTED).build();
        this.claimReviewRepository.save(review);
        this.auditService.recordEvent(claim, reviewer, ClaimAuditEvent.EventType.CLAIM_REJECTED, prev, InsuranceClaim.ClaimStatus.REJECTED, "Claim rejected. Reason: " + reason);
        log.info("Claim {} rejected by reviewer {}", (Object)claim.getClaimReference(), (Object)reviewer.getUsername());
        return claim;
    }

    @Transactional
    public InsuranceClaim requestAdditionalInfo(Long claimId, User reviewer, String infoRequest, String notes) {
        InsuranceClaim claim = this.findById(claimId);
        this.assertReviewerAccess(claim, reviewer);
        if (claim.getStatus() != InsuranceClaim.ClaimStatus.UNDER_REVIEW) {
            throw new InvalidStateTransitionException("Can only request info for claims under review");
        }
        InsuranceClaim.ClaimStatus prev = claim.getStatus();
        claim.setStatus(InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED);
        claim.setAssignedReviewer(reviewer);
        claim.setAdditionalInfoRequest(infoRequest);
        claim.setReviewerNotes(notes);
        this.claimRepository.save(claim);
        ClaimReview review = ClaimReview.builder().claim(claim).reviewer(reviewer).previousStatus(prev).newStatus(InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED).notes(notes).decisionReason(infoRequest).decisionType(ClaimReview.DecisionType.ADDITIONAL_INFO_REQUIRED).build();
        this.claimReviewRepository.save(review);
        this.auditService.recordEvent(claim, reviewer, ClaimAuditEvent.EventType.ADDITIONAL_INFO_REQUESTED, prev, InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED, "Additional information requested: " + infoRequest);
        return claim;
    }

    @Transactional(readOnly=true)
    public InsuranceClaim findById(Long id) {
        return (InsuranceClaim)this.claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + id));
    }

    @Transactional(readOnly=true)
    public InsuranceClaim findByReference(String ref) {
        return this.claimRepository.findByClaimReference(ref).orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + ref));
    }

    @Transactional(readOnly=true)
    public Page<InsuranceClaim> findByUser(User user, Pageable pageable) {
        return this.claimRepository.findBySubmittedBy(user, pageable);
    }

    @Transactional(readOnly=true)
    public Page<InsuranceClaim> findPendingReview(Pageable pageable) {
        return this.claimRepository.findPendingReviewClaims(pageable);
    }

    @Transactional(readOnly=true)
    public Page<InsuranceClaim> findAll(Pageable pageable) {
        return this.claimRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public List<InsuranceClaim> findRecent(int count) {
        return this.claimRepository.findRecentClaims((Pageable)PageRequest.of((int)0, (int)count));
    }

    @Transactional(readOnly=true)
    public DashboardStats getDashboardStats(User user, String role) {
        DashboardStats stats = new DashboardStats();
        if ("ADMIN".equals(role)) {
            stats.setTotalClaims(this.claimRepository.count());
            stats.setSubmittedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.SUBMITTED));
            stats.setUnderReviewClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.UNDER_REVIEW));
            stats.setApprovedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.APPROVED));
            stats.setRejectedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.REJECTED));
            stats.setAdditionalInfoRequiredClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED));
            stats.setValidationFailedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.VALIDATION_FAILED));
            BigDecimal approved = this.claimRepository.sumApprovedAmount();
            stats.setTotalApprovedAmount(approved != null ? approved : BigDecimal.ZERO);
        } else if ("REVIEWER".equals(role)) {
            stats.setSubmittedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.SUBMITTED));
            stats.setUnderReviewClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.UNDER_REVIEW));
            stats.setApprovedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.APPROVED));
            stats.setRejectedClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.REJECTED));
            stats.setAdditionalInfoRequiredClaims(this.claimRepository.countByStatus(InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED));
        } else {
            stats.setMyTotalClaims(this.claimRepository.countBySubmittedBy(user));
            stats.setMyApprovedClaims(this.claimRepository.countBySubmittedByAndStatus(user, InsuranceClaim.ClaimStatus.APPROVED));
            long active = this.claimRepository.countBySubmittedByAndStatus(user, InsuranceClaim.ClaimStatus.SUBMITTED) + this.claimRepository.countBySubmittedByAndStatus(user, InsuranceClaim.ClaimStatus.UNDER_REVIEW);
            stats.setMyActiveClaims(active);
        }
        return stats;
    }

    private void assertOwner(InsuranceClaim claim, User user) {
        if (!claim.getSubmittedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not own this claim");
        }
    }

    private void assertReviewerAccess(InsuranceClaim claim, User reviewer) {
        boolean isReviewer;
        boolean bl = isReviewer = reviewer.getRoles().contains("REVIEWER") || reviewer.getRoles().contains("ADMIN");
        if (!isReviewer) {
            throw new AccessDeniedException("You do not have reviewer access");
        }
    }

    private String generateClaimReference() {
        String candidate;
        while (this.claimRepository.existsByClaimReference(candidate = "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())) {
        }
        return candidate;
    }

    @Generated
    public ClaimService(ClaimRepository claimRepository, ClaimReviewRepository claimReviewRepository, PolicyService policyService, ClaimValidationService validationService, AuditService auditService) {
        this.claimRepository = claimRepository;
        this.claimReviewRepository = claimReviewRepository;
        this.policyService = policyService;
        this.validationService = validationService;
        this.auditService = auditService;
    }
}
