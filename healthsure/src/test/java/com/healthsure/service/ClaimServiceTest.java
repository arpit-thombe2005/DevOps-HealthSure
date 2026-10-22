package com.healthsure.service;

import com.healthsure.dto.ClaimSubmissionRequest;
import com.healthsure.dto.ValidationResult;
import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.ClaimReview;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.exception.AccessDeniedException;
import com.healthsure.exception.InvalidStateTransitionException;
import com.healthsure.repository.ClaimRepository;
import com.healthsure.repository.ClaimReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;
    @Mock
    private ClaimReviewRepository claimReviewRepository;
    @Mock
    private PolicyService policyService;
    @Mock
    private ClaimValidationService validationService;
    @Mock
    private AuditService auditService;

    @InjectMocks
    private ClaimService claimService;

    private User policyholder;
    private User reviewer;
    private InsurancePolicy policy;
    private InsuranceClaim draftClaim;

    @BeforeEach
    void setUp() {
        policyholder = User.builder()
                .id(1L)
                .username("john.doe")
                .roles(Set.of("POLICYHOLDER"))
                .build();

        reviewer = User.builder()
                .id(2L)
                .username("reviewer1")
                .roles(Set.of("REVIEWER"))
                .build();

        policy = InsurancePolicy.builder()
                .id(10L)
                .policyNumber("POL-1001")
                .policyholder(policyholder)
                .status(InsurancePolicy.PolicyStatus.ACTIVE)
                .coverageAmount(new BigDecimal("500000.00"))
                .usedCoverage(BigDecimal.ZERO)
                .build();

        draftClaim = InsuranceClaim.builder()
                .id(100L)
                .claimReference("CLM-100")
                .policy(policy)
                .submittedBy(policyholder)
                .status(InsuranceClaim.ClaimStatus.DRAFT)
                .claimedAmount(new BigDecimal("15000.00"))
                .hospitalName("Apollo Clinic")
                .doctorName("Dr. Rao")
                .treatmentStartDate(LocalDate.now().minusDays(3))
                .description("Diagnostic tests and therapy")
                .build();
    }

    @Test
    @DisplayName("Should successfully create a draft claim")
    void testCreateDraftSuccess() {
        ClaimSubmissionRequest request = ClaimSubmissionRequest.builder()
                .policyId(10L)
                .claimType(InsuranceClaim.ClaimType.MEDICAL_PROCEDURE)
                .hospitalName("Apollo Clinic")
                .doctorName("Dr. Rao")
                .treatmentStartDate(LocalDate.now().minusDays(3))
                .claimedAmount(new BigDecimal("15000.00"))
                .description("Diagnostic tests")
                .submitNow(false)
                .build();

        when(policyService.findById(10L)).thenReturn(policy);
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(i -> {
            InsuranceClaim c = i.getArgument(0);
            c.setId(101L);
            return c;
        });

        InsuranceClaim created = claimService.createDraft(request, policyholder);

        assertNotNull(created);
        assertEquals(InsuranceClaim.ClaimStatus.DRAFT, created.getStatus());
        verify(auditService).recordEvent(eq(created), eq(policyholder), eq(ClaimAuditEvent.EventType.CLAIM_CREATED), isNull(), eq(InsuranceClaim.ClaimStatus.DRAFT), anyString());
    }

    @Test
    @DisplayName("Should transition from DRAFT to UNDER_REVIEW when validation passes")
    void testSubmitClaimSuccess() {
        when(claimRepository.findById(100L)).thenReturn(Optional.of(draftClaim));
        when(validationService.validate(any())).thenReturn(ValidationResult.success());
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(i -> i.getArgument(0));

        InsuranceClaim submitted = claimService.submit(100L, policyholder);

        assertNotNull(submitted);
        assertEquals(InsuranceClaim.ClaimStatus.UNDER_REVIEW, submitted.getStatus());
        verify(auditService).recordEvent(any(), eq(policyholder), eq(ClaimAuditEvent.EventType.CLAIM_SUBMITTED), eq(InsuranceClaim.ClaimStatus.DRAFT), eq(InsuranceClaim.ClaimStatus.SUBMITTED), anyString());
        verify(auditService).recordEvent(any(), eq(policyholder), eq(ClaimAuditEvent.EventType.CLAIM_VALIDATED), eq(InsuranceClaim.ClaimStatus.SUBMITTED), eq(InsuranceClaim.ClaimStatus.UNDER_REVIEW), anyString());
    }

    @Test
    @DisplayName("Should transition to VALIDATION_FAILED when business validation fails")
    void testSubmitClaimValidationFailed() {
        ValidationResult failedResult = ValidationResult.failure("Required documents missing");
        when(claimRepository.findById(100L)).thenReturn(Optional.of(draftClaim));
        when(validationService.validate(any())).thenReturn(failedResult);
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(i -> i.getArgument(0));

        InsuranceClaim result = claimService.submit(100L, policyholder);

        assertNotNull(result);
        assertEquals(InsuranceClaim.ClaimStatus.VALIDATION_FAILED, result.getStatus());
    }

    @Test
    @DisplayName("Should approve claim when in UNDER_REVIEW status")
    void testApproveClaim() {
        draftClaim.setStatus(InsuranceClaim.ClaimStatus.UNDER_REVIEW);
        when(claimRepository.findById(100L)).thenReturn(Optional.of(draftClaim));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(i -> i.getArgument(0));

        InsuranceClaim approved = claimService.approve(100L, reviewer, "Claim verified and accepted", new BigDecimal("15000.00"));

        assertEquals(InsuranceClaim.ClaimStatus.APPROVED, approved.getStatus());
        assertEquals(reviewer, approved.getAssignedReviewer());
        assertEquals(new BigDecimal("15000.00"), approved.getApprovedAmount());
        verify(policyService).applyClaimAmount(eq(policy), eq(new BigDecimal("15000.00")));
        verify(claimReviewRepository).save(any(ClaimReview.class));
    }

    @Test
    @DisplayName("Should reject claim with mandatory reason")
    void testRejectClaim() {
        draftClaim.setStatus(InsuranceClaim.ClaimStatus.UNDER_REVIEW);
        when(claimRepository.findById(100L)).thenReturn(Optional.of(draftClaim));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(i -> i.getArgument(0));

        InsuranceClaim rejected = claimService.reject(100L, reviewer, "Treatment not covered under policy policy terms", "Verified exclusions");

        assertEquals(InsuranceClaim.ClaimStatus.REJECTED, rejected.getStatus());
        assertEquals("Treatment not covered under policy policy terms", rejected.getRejectionReason());
        verify(claimReviewRepository).save(any(ClaimReview.class));
    }

    @Test
    @DisplayName("Should fail rejection when reason is blank")
    void testRejectClaimWithoutReason() {
        assertThrows(IllegalArgumentException.class, () ->
                claimService.reject(100L, reviewer, "  ", "Notes")
        );
    }

    @Test
    @DisplayName("Should request additional information from policyholder")
    void testRequestAdditionalInfo() {
        draftClaim.setStatus(InsuranceClaim.ClaimStatus.UNDER_REVIEW);
        when(claimRepository.findById(100L)).thenReturn(Optional.of(draftClaim));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(i -> i.getArgument(0));

        InsuranceClaim infoReq = claimService.requestAdditionalInfo(100L, reviewer, "Please upload discharge summary", "Need discharge slip");

        assertEquals(InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED, infoReq.getStatus());
        assertEquals("Please upload discharge summary", infoReq.getAdditionalInfoRequest());
    }

    @Test
    @DisplayName("Should reject illegal state transition (e.g. approve an already rejected claim)")
    void testIllegalStateTransition() {
        draftClaim.setStatus(InsuranceClaim.ClaimStatus.REJECTED);
        when(claimRepository.findById(100L)).thenReturn(Optional.of(draftClaim));

        assertThrows(InvalidStateTransitionException.class, () ->
                claimService.approve(100L, reviewer, "Notes", new BigDecimal("1000.00"))
        );
    }
}
