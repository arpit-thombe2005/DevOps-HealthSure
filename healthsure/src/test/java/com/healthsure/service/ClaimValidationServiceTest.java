package com.healthsure.service;

import com.healthsure.dto.ValidationResult;
import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimValidationServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @InjectMocks
    private ClaimValidationService validationService;

    private User policyholder;
    private InsurancePolicy policy;
    private InsuranceClaim validClaim;

    @BeforeEach
    void setUp() {
        policyholder = User.builder()
                .id(1L)
                .username("john.doe")
                .email("john@demo.com")
                .roles(Set.of("POLICYHOLDER"))
                .enabled(true)
                .build();

        policy = InsurancePolicy.builder()
                .id(10L)
                .policyNumber("POL-1001")
                .policyholder(policyholder)
                .policyType(InsurancePolicy.PolicyType.INDIVIDUAL)
                .coverageAmount(new BigDecimal("500000.00"))
                .usedCoverage(new BigDecimal("50000.00"))
                .startDate(LocalDate.now().minusMonths(6))
                .expiryDate(LocalDate.now().plusMonths(6))
                .status(InsurancePolicy.PolicyStatus.ACTIVE)
                .build();

        ClaimDocument document = ClaimDocument.builder()
                .id(100L)
                .originalFilename("bill.pdf")
                .status(ClaimDocument.DocumentStatus.ACTIVE)
                .build();

        validClaim = InsuranceClaim.builder()
                .id(50L)
                .claimReference("CLM-20261022-0001")
                .policy(policy)
                .submittedBy(policyholder)
                .claimType(InsuranceClaim.ClaimType.HOSPITALIZATION)
                .hospitalName("City General Hospital")
                .doctorName("Dr. Robert Smith")
                .treatmentStartDate(LocalDate.now().minusDays(5))
                .treatmentEndDate(LocalDate.now().minusDays(2))
                .claimedAmount(new BigDecimal("25000.00"))
                .description("Inpatient admission for severe acute infection")
                .documents(new ArrayList<>(List.of(document)))
                .build();
    }

    @Test
    @DisplayName("Should validate successfully for a complete, valid claim")
    void testValidateSuccess() {
        when(claimRepository.findDuplicateCandidates(any(), any()))
                .thenReturn(Collections.emptyList());

        ValidationResult result = validationService.validate(validClaim);

        assertTrue(result.isValid(), "Validation should pass for valid claim");
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when mandatory fields are missing")
    void testValidateMissingFields() {
        validClaim.setHospitalName(null);
        validClaim.setClaimedAmount(BigDecimal.ZERO);

        ValidationResult result = validationService.validate(validClaim);

        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("Hospital name is required")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("greater than zero")));
    }

    @Test
    @DisplayName("Should fail validation when claim submitter does not own the policy")
    void testValidateUnauthorizedPolicyOwnership() {
        User differentUser = User.builder().id(999L).username("imposter").build();
        validClaim.setSubmittedBy(differentUser);

        ValidationResult result = validationService.validate(validClaim);

        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("do not own the selected policy")));
    }

    @Test
    @DisplayName("Should fail validation when policy is expired or not active")
    void testValidateExpiredPolicy() {
        policy.setStatus(InsurancePolicy.PolicyStatus.EXPIRED);
        policy.setExpiryDate(LocalDate.now().minusDays(10));

        ValidationResult result = validationService.validate(validClaim);

        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("Policy is not active")));
    }

    @Test
    @DisplayName("Should fail validation when claim amount exceeds available balance")
    void testValidateExcessiveClaimAmount() {
        // Available coverage is 500,000 - 50,000 = 450,000. Claim 460,000.
        validClaim.setClaimedAmount(new BigDecimal("460000.00"));

        ValidationResult result = validationService.validate(validClaim);

        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("exceeds available coverage")));
    }

    @Test
    @DisplayName("Should fail validation when treatment date range is inverted or future")
    void testValidateInvertedTreatmentDates() {
        validClaim.setTreatmentStartDate(LocalDate.now().minusDays(2));
        validClaim.setTreatmentEndDate(LocalDate.now().minusDays(5)); // End before start

        ValidationResult result = validationService.validate(validClaim);

        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("Treatment end date cannot be before start date")));
    }

    @Test
    @DisplayName("Should detect potential duplicate claim submission within duplicate window")
    void testValidateDuplicateClaimDetected() {
        InsuranceClaim existingClaim = InsuranceClaim.builder()
                .id(49L)
                .claimReference("CLM-PREV-001")
                .claimType(validClaim.getClaimType())
                .treatmentStartDate(validClaim.getTreatmentStartDate())
                .claimedAmount(validClaim.getClaimedAmount())
                .hospitalName(validClaim.getHospitalName())
                .build();

        when(claimRepository.findDuplicateCandidates(any(), any()))
                .thenReturn(List.of(existingClaim));

        ValidationResult result = validationService.validate(validClaim);

        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("similar claim (CLM-PREV-001) was already submitted")));
    }
}
