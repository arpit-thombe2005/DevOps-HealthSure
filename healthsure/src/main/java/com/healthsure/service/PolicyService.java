/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.healthsure.service;

import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.exception.ResourceNotFoundException;
import com.healthsure.repository.PolicyRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);
    private final PolicyRepository policyRepository;

    @Transactional(readOnly=true)
    public InsurancePolicy findById(Long id) {
        return (InsurancePolicy)this.policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + id));
    }

    @Transactional(readOnly=true)
    public List<InsurancePolicy> findByPolicyholder(User user) {
        return this.policyRepository.findByPolicyholder(user);
    }

    @Transactional(readOnly=true)
    public List<InsurancePolicy> findActivePoliciesForUser(User user) {
        return this.policyRepository.findByPolicyholderAndStatus(user, InsurancePolicy.PolicyStatus.ACTIVE);
    }

    @Transactional(readOnly=true)
    public Page<InsurancePolicy> findAll(Pageable pageable) {
        return this.policyRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public InsurancePolicy findByPolicyNumber(String number) {
        return this.policyRepository.findByPolicyNumber(number).orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + number));
    }

    @Transactional
    public InsurancePolicy createPolicy(User policyholder, InsurancePolicy.PolicyType type, BigDecimal coverage, LocalDate start, LocalDate expiry, String description) {
        String policyNumber = this.generatePolicyNumber();
        InsurancePolicy policy = InsurancePolicy.builder().policyNumber(policyNumber).policyholder(policyholder).policyType(type).coverageAmount(coverage).usedCoverage(BigDecimal.ZERO).startDate(start).expiryDate(expiry).status(InsurancePolicy.PolicyStatus.ACTIVE).description(description).build();
        InsurancePolicy saved = (InsurancePolicy)this.policyRepository.save(policy);
        log.info("Policy created: {} for user {}", (Object)saved.getPolicyNumber(), (Object)policyholder.getUsername());
        return saved;
    }

    @Transactional
    public InsurancePolicy updateStatus(Long policyId, InsurancePolicy.PolicyStatus status) {
        InsurancePolicy policy = this.findById(policyId);
        policy.setStatus(status);
        return (InsurancePolicy)this.policyRepository.save(policy);
    }

    @Transactional
    public void applyClaimAmount(InsurancePolicy policy, BigDecimal amount) {
        policy.setUsedCoverage(policy.getUsedCoverage().add(amount));
        this.policyRepository.save(policy);
    }

    @Transactional(readOnly=true)
    public long countActive() {
        return this.policyRepository.countByStatus(InsurancePolicy.PolicyStatus.ACTIVE);
    }

    @Transactional(readOnly=true)
    public long countAll() {
        return this.policyRepository.count();
    }

    private String generatePolicyNumber() {
        String candidate;
        while (this.policyRepository.existsByPolicyNumber(candidate = "POL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())) {
        }
        return candidate;
    }

    @Generated
    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }
}
