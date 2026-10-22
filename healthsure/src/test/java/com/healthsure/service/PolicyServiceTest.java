package com.healthsure.service;

import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyService policyService;

    private User user;
    private InsurancePolicy policy;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("john.doe").build();
        policy = InsurancePolicy.builder()
                .id(10L)
                .policyNumber("POL-1001")
                .policyholder(user)
                .policyType(InsurancePolicy.PolicyType.INDIVIDUAL)
                .coverageAmount(new BigDecimal("500000.00"))
                .usedCoverage(new BigDecimal("50000.00"))
                .startDate(LocalDate.now().minusMonths(1))
                .expiryDate(LocalDate.now().plusMonths(11))
                .status(InsurancePolicy.PolicyStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Should find active policies for user")
    void testFindActivePoliciesForUser() {
        when(policyRepository.findByPolicyholderAndStatus(user, InsurancePolicy.PolicyStatus.ACTIVE))
                .thenReturn(List.of(policy));

        List<InsurancePolicy> result = policyService.findActivePoliciesForUser(user);

        assertEquals(1, result.size());
        assertEquals("POL-1001", result.get(0).getPolicyNumber());
    }

    @Test
    @DisplayName("Should increment used coverage when claim amount is approved")
    void testApplyClaimAmount() {
        when(policyRepository.save(any(InsurancePolicy.class))).thenAnswer(i -> i.getArgument(0));

        policyService.applyClaimAmount(policy, new BigDecimal("25000.00"));

        assertEquals(new BigDecimal("75000.00"), policy.getUsedCoverage());
        verify(policyRepository).save(policy);
    }

    @Test
    @DisplayName("Should create new policy with generated unique reference")
    void testCreatePolicy() {
        when(policyRepository.existsByPolicyNumber(anyString())).thenReturn(false);
        when(policyRepository.save(any(InsurancePolicy.class))).thenAnswer(i -> {
            InsurancePolicy p = i.getArgument(0);
            p.setId(20L);
            return p;
        });

        InsurancePolicy created = policyService.createPolicy(
                user,
                InsurancePolicy.PolicyType.FAMILY,
                new BigDecimal("800000.00"),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                "Family plan"
        );

        assertNotNull(created);
        assertEquals(new BigDecimal("800000.00"), created.getCoverageAmount());
        assertEquals(BigDecimal.ZERO, created.getUsedCoverage());
        assertEquals(InsurancePolicy.PolicyStatus.ACTIVE, created.getStatus());
        assertTrue(created.getPolicyNumber().startsWith("POL-"));
    }
}
