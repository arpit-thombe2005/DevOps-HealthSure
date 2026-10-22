/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.EnumType
 *  jakarta.persistence.Enumerated
 *  jakarta.persistence.FetchType
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Index
 *  jakarta.persistence.JoinColumn
 *  jakarta.persistence.ManyToOne
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.healthsure.entity;

import com.healthsure.entity.BaseEntity;
import com.healthsure.entity.InsuranceClaim;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Generated;

@Entity
@Table(name="claim_audit_events", indexes={@Index(name="idx_audit_claim", columnList="claim_id"), @Index(name="idx_audit_actor", columnList="actorId")})
public class ClaimAuditEvent
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="claim_id", nullable=false)
    private InsuranceClaim claim;
    @Column(nullable=false)
    private Long actorId;
    @Column(nullable=false, length=100)
    private String actorUsername;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=30)
    private EventType eventType;
    @Enumerated(value=EnumType.STRING)
    @Column(length=30)
    private InsuranceClaim.ClaimStatus previousStatus;
    @Enumerated(value=EnumType.STRING)
    @Column(length=30)
    private InsuranceClaim.ClaimStatus newStatus;
    @Column(length=2000)
    private String description;
    @Column(length=50)
    private String ipAddress;

    @Generated
    public static ClaimAuditEventBuilder builder() {
        return new ClaimAuditEventBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public InsuranceClaim getClaim() {
        return this.claim;
    }

    @Generated
    public Long getActorId() {
        return this.actorId;
    }

    @Generated
    public String getActorUsername() {
        return this.actorUsername;
    }

    @Generated
    public EventType getEventType() {
        return this.eventType;
    }

    @Generated
    public InsuranceClaim.ClaimStatus getPreviousStatus() {
        return this.previousStatus;
    }

    @Generated
    public InsuranceClaim.ClaimStatus getNewStatus() {
        return this.newStatus;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setClaim(InsuranceClaim claim) {
        this.claim = claim;
    }

    @Generated
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Generated
    public void setActorUsername(String actorUsername) {
        this.actorUsername = actorUsername;
    }

    @Generated
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Generated
    public void setPreviousStatus(InsuranceClaim.ClaimStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    @Generated
    public void setNewStatus(InsuranceClaim.ClaimStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Generated
    public ClaimAuditEvent() {
    }

    @Generated
    public ClaimAuditEvent(Long id, InsuranceClaim claim, Long actorId, String actorUsername, EventType eventType, InsuranceClaim.ClaimStatus previousStatus, InsuranceClaim.ClaimStatus newStatus, String description, String ipAddress) {
        this.id = id;
        this.claim = claim;
        this.actorId = actorId;
        this.actorUsername = actorUsername;
        this.eventType = eventType;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.description = description;
        this.ipAddress = ipAddress;
    }

    @Generated
    public static class ClaimAuditEventBuilder {
        @Generated
        private Long id;
        @Generated
        private InsuranceClaim claim;
        @Generated
        private Long actorId;
        @Generated
        private String actorUsername;
        @Generated
        private EventType eventType;
        @Generated
        private InsuranceClaim.ClaimStatus previousStatus;
        @Generated
        private InsuranceClaim.ClaimStatus newStatus;
        @Generated
        private String description;
        @Generated
        private String ipAddress;

        @Generated
        ClaimAuditEventBuilder() {
        }

        @Generated
        public ClaimAuditEventBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder claim(InsuranceClaim claim) {
            this.claim = claim;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder actorId(Long actorId) {
            this.actorId = actorId;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder actorUsername(String actorUsername) {
            this.actorUsername = actorUsername;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder previousStatus(InsuranceClaim.ClaimStatus previousStatus) {
            this.previousStatus = previousStatus;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder newStatus(InsuranceClaim.ClaimStatus newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ClaimAuditEventBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        @Generated
        public ClaimAuditEvent build() {
            return new ClaimAuditEvent(this.id, this.claim, this.actorId, this.actorUsername, this.eventType, this.previousStatus, this.newStatus, this.description, this.ipAddress);
        }

        @Generated
        public String toString() {
            return "ClaimAuditEvent.ClaimAuditEventBuilder(id=" + this.id + ", claim=" + String.valueOf(this.claim) + ", actorId=" + this.actorId + ", actorUsername=" + this.actorUsername + ", eventType=" + String.valueOf((Object)this.eventType) + ", previousStatus=" + String.valueOf((Object)this.previousStatus) + ", newStatus=" + String.valueOf((Object)this.newStatus) + ", description=" + this.description + ", ipAddress=" + this.ipAddress + ")";
        }
    }

    public static enum EventType {
        CLAIM_CREATED,
        CLAIM_SUBMITTED,
        CLAIM_VALIDATED,
        VALIDATION_FAILED,
        REVIEW_STARTED,
        CLAIM_APPROVED,
        CLAIM_REJECTED,
        ADDITIONAL_INFO_REQUESTED,
        ADDITIONAL_INFO_PROVIDED,
        DOCUMENT_UPLOADED,
        DOCUMENT_DELETED,
        STATUS_CHANGED,
        ADMIN_ACTION,
        NOTE_ADDED;

    }
}
