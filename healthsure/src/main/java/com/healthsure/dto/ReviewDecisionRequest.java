/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.constraints.DecimalMin
 *  jakarta.validation.constraints.NotNull
 *  jakarta.validation.constraints.Size
 *  lombok.Generated
 */
package com.healthsure.dto;

import com.healthsure.entity.InsuranceClaim;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Generated;

public class ReviewDecisionRequest {
    @NotNull(message="Decision is required")
    private  @NotNull(message="Decision is required") InsuranceClaim.ClaimStatus decision;
    @Size(max=2000)
    private @Size(max=2000) String notes;
    @Size(max=2000)
    private @Size(max=2000) String decisionReason;
    @Size(max=2000)
    private @Size(max=2000) String additionalInfoRequest;
    @DecimalMin(value="0.00")
    private @DecimalMin(value="0.00") BigDecimal recommendedAmount;

    @Generated
    public static ReviewDecisionRequestBuilder builder() {
        return new ReviewDecisionRequestBuilder();
    }

    @Generated
    public InsuranceClaim.ClaimStatus getDecision() {
        return this.decision;
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
    public String getAdditionalInfoRequest() {
        return this.additionalInfoRequest;
    }

    @Generated
    public BigDecimal getRecommendedAmount() {
        return this.recommendedAmount;
    }

    @Generated
    public void setDecision(InsuranceClaim.ClaimStatus decision) {
        this.decision = decision;
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
    public void setAdditionalInfoRequest(String additionalInfoRequest) {
        this.additionalInfoRequest = additionalInfoRequest;
    }

    @Generated
    public void setRecommendedAmount(BigDecimal recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    @Generated
    public ReviewDecisionRequest() {
    }

    @Generated
    public ReviewDecisionRequest(InsuranceClaim.ClaimStatus decision, String notes, String decisionReason, String additionalInfoRequest, BigDecimal recommendedAmount) {
        this.decision = decision;
        this.notes = notes;
        this.decisionReason = decisionReason;
        this.additionalInfoRequest = additionalInfoRequest;
        this.recommendedAmount = recommendedAmount;
    }

    @Generated
    public static class ReviewDecisionRequestBuilder {
        @Generated
        private InsuranceClaim.ClaimStatus decision;
        @Generated
        private String notes;
        @Generated
        private String decisionReason;
        @Generated
        private String additionalInfoRequest;
        @Generated
        private BigDecimal recommendedAmount;

        @Generated
        ReviewDecisionRequestBuilder() {
        }

        @Generated
        public ReviewDecisionRequestBuilder decision(InsuranceClaim.ClaimStatus decision) {
            this.decision = decision;
            return this;
        }

        @Generated
        public ReviewDecisionRequestBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        @Generated
        public ReviewDecisionRequestBuilder decisionReason(String decisionReason) {
            this.decisionReason = decisionReason;
            return this;
        }

        @Generated
        public ReviewDecisionRequestBuilder additionalInfoRequest(String additionalInfoRequest) {
            this.additionalInfoRequest = additionalInfoRequest;
            return this;
        }

        @Generated
        public ReviewDecisionRequestBuilder recommendedAmount(BigDecimal recommendedAmount) {
            this.recommendedAmount = recommendedAmount;
            return this;
        }

        @Generated
        public ReviewDecisionRequest build() {
            return new ReviewDecisionRequest(this.decision, this.notes, this.decisionReason, this.additionalInfoRequest, this.recommendedAmount);
        }

        @Generated
        public String toString() {
            return "ReviewDecisionRequest.ReviewDecisionRequestBuilder(decision=" + String.valueOf((Object)this.decision) + ", notes=" + this.notes + ", decisionReason=" + this.decisionReason + ", additionalInfoRequest=" + this.additionalInfoRequest + ", recommendedAmount=" + String.valueOf(this.recommendedAmount) + ")";
        }
    }
}
