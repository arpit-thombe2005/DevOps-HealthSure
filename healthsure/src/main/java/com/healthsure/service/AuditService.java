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

import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.User;
import com.healthsure.repository.ClaimAuditEventRepository;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final ClaimAuditEventRepository auditEventRepository;

    @Transactional
    public void recordEvent(InsuranceClaim claim, User actor, ClaimAuditEvent.EventType eventType, InsuranceClaim.ClaimStatus previousStatus, InsuranceClaim.ClaimStatus newStatus, String description) {
        ClaimAuditEvent event = ClaimAuditEvent.builder().claim(claim).actorId(actor.getId()).actorUsername(actor.getUsername()).eventType(eventType).previousStatus(previousStatus).newStatus(newStatus).description(description).build();
        this.auditEventRepository.save(event);
    }

    @Transactional(readOnly=true)
    public List<ClaimAuditEvent> getClaimHistory(InsuranceClaim claim) {
        return this.auditEventRepository.findByClaimOrderByCreatedAtAsc(claim);
    }

    @Generated
    public AuditService(ClaimAuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }
}
