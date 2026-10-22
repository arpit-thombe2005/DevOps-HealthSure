/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.stereotype.Repository
 */
package com.healthsure.repository;

import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository
extends JpaRepository<InsurancePolicy, Long> {
    public List<InsurancePolicy> findByPolicyholder(User policyholder);

    public List<InsurancePolicy> findByPolicyholderAndStatus(User policyholder, InsurancePolicy.PolicyStatus status);

    public Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);

    public boolean existsByPolicyNumber(String policyNumber);

    public Page<InsurancePolicy> findByPolicyholder(User policyholder, Pageable pageable);

    @Query(value="SELECT p FROM InsurancePolicy p WHERE p.expiryDate < :today AND p.status = 'ACTIVE'")
    public List<InsurancePolicy> findExpiredPolicies(@Param("today") LocalDate today);

    @Query(value="SELECT COUNT(p) FROM InsurancePolicy p WHERE p.status = :status")
    public long countByStatus(@Param("status") InsurancePolicy.PolicyStatus status);
}
