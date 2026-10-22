/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.healthsure.service;

import com.healthsure.dto.ValidationResult;
import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.repository.ClaimRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimValidationService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ClaimValidationService.class);
    private final ClaimRepository claimRepository;
    private static final int DUPLICATE_WINDOW_DAYS = 30;

    @Transactional(readOnly=true)
    public ValidationResult validate(InsuranceClaim claim) {
        ValidationResult result = ValidationResult.success();
        this.validateClaimCompleteness(claim, result);
        if (!result.isValid()) {
            return result;
        }
        this.validatePolicyOwnership(claim, result);
        this.validatePolicyActive(claim, result);
        this.validateClaimAmount(claim, result);
        this.validateDates(claim, result);
        this.validateDocuments(claim, result);
        this.checkDuplicate(claim, result);
        log.info("Validation of claim {} - valid={}, errors={}", new Object[]{claim.getClaimReference(), result.isValid(), result.getErrors().size()});
        return result;
    }

    private void validateClaimCompleteness(InsuranceClaim claim, ValidationResult result) {
        if (claim.getPolicy() == null) {
            result.addError("Policy is required");
        }
        if (claim.getClaimType() == null) {
            result.addError("Claim type is required");
        }
        if (claim.getHospitalName() == null || claim.getHospitalName().isBlank()) {
            result.addError("Hospital name is required");
        }
        if (claim.getTreatmentStartDate() == null) {
            result.addError("Treatment start date is required");
        }
        if (claim.getClaimedAmount() == null || claim.getClaimedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            result.addError("Claimed amount must be greater than zero");
        }
        if (claim.getDescription() == null || claim.getDescription().isBlank()) {
            result.addError("Description is required");
        }
    }

    private void validatePolicyOwnership(InsuranceClaim claim, ValidationResult result) {
        if (claim.getPolicy() == null || claim.getSubmittedBy() == null) {
            return;
        }
        if (!claim.getPolicy().getPolicyholder().getId().equals(claim.getSubmittedBy().getId())) {
            result.addError("You do not own the selected policy");
        }
    }

    private void validatePolicyActive(InsuranceClaim claim, ValidationResult result) {
        if (claim.getPolicy() == null) {
            return;
        }
        InsurancePolicy policy = claim.getPolicy();
        if (policy.getStatus() != InsurancePolicy.PolicyStatus.ACTIVE) {
            result.addError("Policy is not active (status: " + String.valueOf((Object)policy.getStatus()) + ")");
            return;
        }
        LocalDate today = LocalDate.now();
        if (policy.getStartDate().isAfter(today)) {
            result.addError("Policy has not started yet (starts: " + String.valueOf(policy.getStartDate()) + ")");
        }
        if (policy.getExpiryDate().isBefore(today)) {
            result.addError("Policy has expired (expired: " + String.valueOf(policy.getExpiryDate()) + ")");
        }
    }

    private void validateClaimAmount(InsuranceClaim claim, ValidationResult result) {
        if (claim.getPolicy() == null || claim.getClaimedAmount() == null) {
            return;
        }
        BigDecimal available = claim.getPolicy().getAvailableCoverage();
        if (claim.getClaimedAmount().compareTo(available) > 0) {
            result.addError(String.format("Claimed amount (\u20b9%.2f) exceeds available coverage (\u20b9%.2f)", claim.getClaimedAmount(), available));
        }
    }

    private void validateDates(InsuranceClaim claim, ValidationResult result) {
        if (claim.getTreatmentStartDate() == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        if (claim.getTreatmentStartDate().isAfter(today)) {
            result.addError("Treatment start date cannot be in the future");
        }
        if (claim.getTreatmentEndDate() != null) {
            if (claim.getTreatmentEndDate().isBefore(claim.getTreatmentStartDate())) {
                result.addError("Treatment end date cannot be before start date");
            }
            if (claim.getTreatmentEndDate().isAfter(today)) {
                result.addError("Treatment end date cannot be in the future");
            }
        }
        if (claim.getPolicy() != null && claim.getTreatmentStartDate().isBefore(claim.getPolicy().getStartDate())) {
            result.addError("Treatment date is before policy start date");
        }
    }

    private void validateDocuments(InsuranceClaim claim, ValidationResult result) {
        long docCount = claim.getDocuments().stream().filter(d -> d.getStatus() == ClaimDocument.DocumentStatus.ACTIVE).count();
        if (docCount == 0L) {
            result.addWarning("No supporting documents uploaded. Attaching documents helps expedite review.");
        }
    }

    private void checkDuplicate(InsuranceClaim claim, ValidationResult result) {
        if (claim.getPolicy() == null || claim.getTreatmentStartDate() == null) {
            return;
        }
        LocalDateTime since = LocalDateTime.now().minusDays(30L);
        List<InsuranceClaim> candidates = this.claimRepository.findDuplicateCandidates(claim.getPolicy(), since);
        for (InsuranceClaim existing : candidates) {
            if (existing.getId().equals(claim.getId()) || existing.getClaimType() != claim.getClaimType() || !existing.getTreatmentStartDate().equals(claim.getTreatmentStartDate())) continue;
            result.addError(String.format("A similar claim (%s) was already submitted recently for the same policy and treatment date. If this is a different claim, please provide a detailed description that distinguishes it.", existing.getClaimReference()));
        }
    }

    @Generated
    public ClaimValidationService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }
}
