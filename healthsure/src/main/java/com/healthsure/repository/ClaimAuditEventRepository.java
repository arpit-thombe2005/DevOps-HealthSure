/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.healthsure.repository;

import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.InsuranceClaim;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimAuditEventRepository
extends JpaRepository<ClaimAuditEvent, Long> {
    public List<ClaimAuditEvent> findByClaimOrderByCreatedAtAsc(InsuranceClaim var1);

    public List<ClaimAuditEvent> findByActorId(Long var1);
}
