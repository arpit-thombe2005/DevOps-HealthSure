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
import com.healthsure.entity.User;
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
import java.math.BigDecimal;
import lombok.Generated;

@Entity
@Table(name="claim_reviews", indexes={@Index(name="idx_review_claim", columnList="claim_id"), @Index(name="idx_review_reviewer", columnList="reviewer_id")})
public class ClaimReview
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="claim_id", nullable=false)
    private InsuranceClaim claim;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="reviewer_id", nullable=false)
    private User reviewer;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=30)
    private InsuranceClaim.ClaimStatus previousStatus;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=30)
    private InsuranceClaim.ClaimStatus newStatus;
    @Column(length=2000)
    private String notes;
    @Column(length=2000)
    private String decisionReason;
    @Column(precision=15, scale=2)
    private BigDecimal recommendedAmount;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=20)
    private DecisionType decisionType;

    @Generated
    private static DecisionType $default$decisionType() {
        return DecisionType.IN_PROGRESS;
    }

    @Generated
    public static ClaimReviewBuilder builder() {
        return new ClaimReviewBuilder();
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
    public User getReviewer() {
        return this.reviewer;
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
    public String getNotes() {
        return this.notes;
    }

    @Generated
    public String getDecisionReason() {
        return this.decisionReason;
    }

    @Generated
    public BigDecimal getRecommendedAmount() {
        return this.recommendedAmount;
    }

    @Generated
    public DecisionType getDecisionType() {
        return this.decisionType;
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
    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
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
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Generated
    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
    }

    @Generated
    public void setRecommendedAmount(BigDecimal recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    @Generated
    public void setDecisionType(DecisionType decisionType) {
        this.decisionType = decisionType;
    }

    @Generated
    public ClaimReview() {
        this.decisionType = ClaimReview.$default$decisionType();
    }

    @Generated
    public ClaimReview(Long id, InsuranceClaim claim, User reviewer, InsuranceClaim.ClaimStatus previousStatus, InsuranceClaim.ClaimStatus newStatus, String notes, String decisionReason, BigDecimal recommendedAmount, DecisionType decisionType) {
        this.id = id;
        this.claim = claim;
        this.reviewer = reviewer;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.notes = notes;
        this.decisionReason = decisionReason;
        this.recommendedAmount = recommendedAmount;
        this.decisionType = decisionType;
    }

    public static enum DecisionType {
        IN_PROGRESS,
        APPROVED,
        REJECTED,
        ADDITIONAL_INFO_REQUIRED,
        REASSIGNED;

    }

    @Generated
    public static class ClaimReviewBuilder {
        @Generated
        private Long id;
        @Generated
        private InsuranceClaim claim;
        @Generated
        private User reviewer;
        @Generated
        private InsuranceClaim.ClaimStatus previousStatus;
        @Generated
        private InsuranceClaim.ClaimStatus newStatus;
        @Generated
        private String notes;
        @Generated
        private String decisionReason;
        @Generated
        private BigDecimal recommendedAmount;
        @Generated
        private boolean decisionType$set;
        @Generated
        private DecisionType decisionType$value;

        @Generated
        ClaimReviewBuilder() {
        }

        @Generated
        public ClaimReviewBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ClaimReviewBuilder claim(InsuranceClaim claim) {
            this.claim = claim;
            return this;
        }

        @Generated
        public ClaimReviewBuilder reviewer(User reviewer) {
            this.reviewer = reviewer;
            return this;
        }

        @Generated
        public ClaimReviewBuilder previousStatus(InsuranceClaim.ClaimStatus previousStatus) {
            this.previousStatus = previousStatus;
            return this;
        }

        @Generated
        public ClaimReviewBuilder newStatus(InsuranceClaim.ClaimStatus newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        @Generated
        public ClaimReviewBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        @Generated
        public ClaimReviewBuilder decisionReason(String decisionReason) {
            this.decisionReason = decisionReason;
            return this;
        }

        @Generated
        public ClaimReviewBuilder recommendedAmount(BigDecimal recommendedAmount) {
            this.recommendedAmount = recommendedAmount;
            return this;
        }

        @Generated
        public ClaimReviewBuilder decisionType(DecisionType decisionType) {
            this.decisionType$value = decisionType;
            this.decisionType$set = true;
            return this;
        }

        @Generated
        public ClaimReview build() {
            DecisionType decisionType$value = this.decisionType$value;
            if (!this.decisionType$set) {
                decisionType$value = ClaimReview.$default$decisionType();
            }
            return new ClaimReview(this.id, this.claim, this.reviewer, this.previousStatus, this.newStatus, this.notes, this.decisionReason, this.recommendedAmount, decisionType$value);
        }

        @Generated
        public String toString() {
            return "ClaimReview.ClaimReviewBuilder(id=" + this.id + ", claim=" + String.valueOf(this.claim) + ", reviewer=" + String.valueOf(this.reviewer) + ", previousStatus=" + String.valueOf((Object)this.previousStatus) + ", newStatus=" + String.valueOf((Object)this.newStatus) + ", notes=" + this.notes + ", decisionReason=" + this.decisionReason + ", recommendedAmount=" + String.valueOf(this.recommendedAmount) + ", decisionType$value=" + String.valueOf((Object)this.decisionType$value) + ")";
        }
    }
}
