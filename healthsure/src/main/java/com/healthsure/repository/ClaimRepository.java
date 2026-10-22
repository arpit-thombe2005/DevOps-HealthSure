/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.healthsure.repository;

import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository
extends JpaRepository<InsuranceClaim, Long> {
    public Optional<InsuranceClaim> findByClaimReference(String var1);

    public boolean existsByClaimReference(String var1);

    public Page<InsuranceClaim> findBySubmittedBy(User var1, Pageable var2);

    public List<InsuranceClaim> findBySubmittedBy(User var1);

    public Page<InsuranceClaim> findByStatus(InsuranceClaim.ClaimStatus var1, Pageable var2);

    public Page<InsuranceClaim> findByAssignedReviewer(User var1, Pageable var2);

    public long countByStatus(InsuranceClaim.ClaimStatus var1);

    public long countBySubmittedBy(User var1);

    public long countBySubmittedByAndStatus(User var1, InsuranceClaim.ClaimStatus var2);

    @Query(value="SELECT SUM(c.claimedAmount) FROM InsuranceClaim c WHERE c.status IN :statuses")
    public BigDecimal sumClaimedAmountByStatuses(@Param(value="statuses") List<InsuranceClaim.ClaimStatus> var1);

    @Query(value="SELECT SUM(c.approvedAmount) FROM InsuranceClaim c WHERE c.status = 'APPROVED'")
    public BigDecimal sumApprovedAmount();

    @Query(value="SELECT c FROM InsuranceClaim c WHERE c.policy = :policy AND c.status NOT IN ('REJECTED') AND c.submittedAt > :since")
    public List<InsuranceClaim> findDuplicateCandidates(@Param(value="policy") InsurancePolicy var1, @Param(value="since") LocalDateTime var2);

    @Query(value="SELECT c FROM InsuranceClaim c WHERE c.submittedBy = :user AND (:status IS NULL OR c.status = :status) ORDER BY c.createdAt DESC")
    public Page<InsuranceClaim> findByUserAndStatus(@Param(value="user") User var1, @Param(value="status") InsuranceClaim.ClaimStatus var2, Pageable var3);

    @Query(value="SELECT c FROM InsuranceClaim c WHERE c.status IN ('SUBMITTED', 'UNDER_REVIEW', 'ADDITIONAL_INFO_REQUIRED') ORDER BY c.submittedAt ASC")
    public Page<InsuranceClaim> findPendingReviewClaims(Pageable var1);

    @Query(value="SELECT c FROM InsuranceClaim c ORDER BY c.createdAt DESC")
    public List<InsuranceClaim> findRecentClaims(Pageable var1);
}
