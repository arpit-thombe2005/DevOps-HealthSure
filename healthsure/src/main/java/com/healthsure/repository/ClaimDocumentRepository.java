/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.healthsure.repository;

import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimDocumentRepository
extends JpaRepository<ClaimDocument, Long> {
    public List<ClaimDocument> findByClaimAndStatus(InsuranceClaim var1, ClaimDocument.DocumentStatus var2);

    public List<ClaimDocument> findByClaim(InsuranceClaim var1);

    public Optional<ClaimDocument> findByIdAndClaim(Long var1, InsuranceClaim var2);

    public long countByClaimAndStatus(InsuranceClaim var1, ClaimDocument.DocumentStatus var2);
}
