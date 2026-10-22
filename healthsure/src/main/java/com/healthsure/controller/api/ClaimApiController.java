/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.v3.oas.annotations.Operation
 *  io.swagger.v3.oas.annotations.tags.Tag
 *  jakarta.validation.Valid
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.security.core.annotation.AuthenticationPrincipal
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.healthsure.controller.api;

import com.healthsure.dto.ClaimResponseDto;
import com.healthsure.dto.ClaimSubmissionRequest;
import com.healthsure.dto.DashboardStats;
import com.healthsure.dto.ReviewDecisionRequest;
import com.healthsure.dto.UserResponseDto;
import com.healthsure.dto.ValidationResult;
import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.security.CustomUserPrincipal;
import com.healthsure.service.AuditService;
import com.healthsure.service.ClaimService;
import com.healthsure.service.ClaimValidationService;
import com.healthsure.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/v1"})
@Tag(name="HealthSure REST API", description="Endpoints for claims, policies, reviews, and audit events")
public class ClaimApiController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ClaimApiController.class);
    private final ClaimService claimService;
    private final PolicyService policyService;
    private final ClaimValidationService validationService;
    private final AuditService auditService;

    @GetMapping(value={"/auth/me"})
    @Operation(summary="Get current authenticated user profile")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
        User u = principal.getUser();
        return ResponseEntity.ok(UserResponseDto.builder().id(u.getId()).username(u.getUsername()).email(u.getEmail()).firstName(u.getFirstName()).lastName(u.getLastName()).phoneNumber(u.getPhone()).roles(u.getRoles()).enabled(u.isEnabled()).createdAt(u.getCreatedAt()).build());
    }

    @GetMapping(value={"/claims"})
    @Operation(summary="List claims based on user role")
    public ResponseEntity<Page<ClaimResponseDto>> getClaims(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size) {
        User user = principal.getUser();
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"createdAt"}).descending());
        Page<InsuranceClaim> claims = user.getRoles().contains("ADMIN") ? this.claimService.findAll((Pageable)pageable) : (user.getRoles().contains("REVIEWER") ? this.claimService.findPendingReview((Pageable)pageable) : this.claimService.findByUser(user, (Pageable)pageable));
        Page<ClaimResponseDto> dtoPage = claims.map(this::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping(value={"/claims"})
    @Operation(summary="Create a new draft claim")
    public ResponseEntity<ClaimResponseDto> createClaim(@AuthenticationPrincipal CustomUserPrincipal principal, @Valid @RequestBody ClaimSubmissionRequest request) {
        InsuranceClaim claim = this.claimService.createDraft(request, principal.getUser());
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body(this.toDto(claim));
    }

    @GetMapping(value={"/claims/{id}"})
    @Operation(summary="Get claim details by ID")
    public ResponseEntity<ClaimResponseDto> getClaimById(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal) {
        boolean isReviewerOrAdmin;
        InsuranceClaim claim = this.claimService.findById(id);
        User user = principal.getUser();
        boolean isOwner = claim.getSubmittedBy().getId().equals(user.getId());
        boolean bl = isReviewerOrAdmin = user.getRoles().contains("REVIEWER") || user.getRoles().contains("ADMIN");
        if (!isOwner && !isReviewerOrAdmin) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.toDto(claim));
    }

    @PostMapping(value={"/claims/{id}/submit"})
    @Operation(summary="Submit a draft claim for automated validation and review")
    public ResponseEntity<ClaimResponseDto> submitClaim(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal) {
        InsuranceClaim claim = this.claimService.submit(id, principal.getUser());
        return ResponseEntity.ok(this.toDto(claim));
    }

    @PostMapping(value={"/claims/{id}/validate"})
    @Operation(summary="Run automated business validation rules on a claim")
    public ResponseEntity<ValidationResult> validateClaim(@PathVariable Long id) {
        InsuranceClaim claim = this.claimService.findById(id);
        ValidationResult result = this.validationService.validate(claim);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value={"/claims/{id}/review"})
    @PreAuthorize(value="hasAnyRole('REVIEWER', 'ADMIN')")
    @Operation(summary="Record reviewer decision (APPROVE / REJECT / ADDITIONAL_INFO_REQUIRED)")
    public ResponseEntity<ClaimResponseDto> reviewClaim(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal, @Valid @RequestBody ReviewDecisionRequest request) {
        User reviewer = principal.getUser();
        return ResponseEntity.ok(this.toDto(switch (request.getDecision()) {
            case InsuranceClaim.ClaimStatus.APPROVED -> this.claimService.approve(id, reviewer, request.getNotes(), request.getRecommendedAmount());
            case InsuranceClaim.ClaimStatus.REJECTED -> this.claimService.reject(id, reviewer, request.getDecisionReason(), request.getNotes());
            case InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED -> this.claimService.requestAdditionalInfo(id, reviewer, request.getAdditionalInfoRequest(), request.getNotes());
            default -> throw new IllegalArgumentException("Invalid decision: " + String.valueOf((Object)request.getDecision()));
        }));
    }

    @GetMapping(value={"/claims/{id}/audit"})
    @Operation(summary="Get immutable audit event timeline for a claim")
    public ResponseEntity<List<ClaimAuditEvent>> getAuditHistory(@PathVariable Long id) {
        InsuranceClaim claim = this.claimService.findById(id);
        List<ClaimAuditEvent> history = this.auditService.getClaimHistory(claim);
        return ResponseEntity.ok(history);
    }

    @GetMapping(value={"/policies"})
    @Operation(summary="List active policies for current user")
    public ResponseEntity<List<InsurancePolicy>> getPolicies(@AuthenticationPrincipal CustomUserPrincipal principal) {
        List<InsurancePolicy> policies = this.policyService.findActivePoliciesForUser(principal.getUser());
        return ResponseEntity.ok(policies);
    }

    @GetMapping(value={"/dashboard/stats"})
    @Operation(summary="Get role-specific dashboard metrics")
    public ResponseEntity<DashboardStats> getStats(@AuthenticationPrincipal CustomUserPrincipal principal) {
        User user = principal.getUser();
        String role = user.getRoles().contains("ADMIN") ? "ADMIN" : (user.getRoles().contains("REVIEWER") ? "REVIEWER" : "POLICYHOLDER");
        return ResponseEntity.ok(this.claimService.getDashboardStats(user, role));
    }

    private ClaimResponseDto toDto(InsuranceClaim c) {
        return ClaimResponseDto.builder().id(c.getId()).claimReference(c.getClaimReference()).policyNumber(c.getPolicy() != null ? c.getPolicy().getPolicyNumber() : null).claimType(c.getClaimType() != null ? c.getClaimType().name() : null).claimAmount(c.getClaimedAmount()).approvedAmount(c.getApprovedAmount()).status(c.getStatus() != null ? c.getStatus().name() : null).hospitalName(c.getHospitalName()).incidentDate(c.getTreatmentStartDate()).description(c.getDescription()).submittedByUsername(c.getSubmittedBy() != null ? c.getSubmittedBy().getUsername() : null).reviewerNotes(c.getReviewerNotes()).submissionDate(c.getSubmittedAt()).createdAt(c.getCreatedAt()).updatedAt(c.getUpdatedAt()).build();
    }

    @Generated
    public ClaimApiController(ClaimService claimService, PolicyService policyService, ClaimValidationService validationService, AuditService auditService) {
        this.claimService = claimService;
        this.policyService = policyService;
        this.validationService = validationService;
        this.auditService = auditService;
    }
}
