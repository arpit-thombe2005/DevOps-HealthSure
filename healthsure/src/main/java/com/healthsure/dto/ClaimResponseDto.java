/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.healthsure.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Generated;

public class ClaimResponseDto {
    private Long id;
    private String claimReference;
    private String policyNumber;
    private String claimType;
    private BigDecimal claimAmount;
    private BigDecimal approvedAmount;
    private String status;
    private String hospitalName;
    private LocalDate incidentDate;
    private String description;
    private String submittedByUsername;
    private String reviewerNotes;
    private LocalDateTime submissionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Generated
    public static ClaimResponseDtoBuilder builder() {
        return new ClaimResponseDtoBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getClaimReference() {
        return this.claimReference;
    }

    @Generated
    public String getPolicyNumber() {
        return this.policyNumber;
    }

    @Generated
    public String getClaimType() {
        return this.claimType;
    }

    @Generated
    public BigDecimal getClaimAmount() {
        return this.claimAmount;
    }

    @Generated
    public BigDecimal getApprovedAmount() {
        return this.approvedAmount;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getHospitalName() {
        return this.hospitalName;
    }

    @Generated
    public LocalDate getIncidentDate() {
        return this.incidentDate;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getSubmittedByUsername() {
        return this.submittedByUsername;
    }

    @Generated
    public String getReviewerNotes() {
        return this.reviewerNotes;
    }

    @Generated
    public LocalDateTime getSubmissionDate() {
        return this.submissionDate;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setClaimReference(String claimReference) {
        this.claimReference = claimReference;
    }

    @Generated
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    @Generated
    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    @Generated
    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    @Generated
    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Generated
    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setSubmittedByUsername(String submittedByUsername) {
        this.submittedByUsername = submittedByUsername;
    }

    @Generated
    public void setReviewerNotes(String reviewerNotes) {
        this.reviewerNotes = reviewerNotes;
    }

    @Generated
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public ClaimResponseDto() {
    }

    @Generated
    public ClaimResponseDto(Long id, String claimReference, String policyNumber, String claimType, BigDecimal claimAmount, BigDecimal approvedAmount, String status, String hospitalName, LocalDate incidentDate, String description, String submittedByUsername, String reviewerNotes, LocalDateTime submissionDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.claimReference = claimReference;
        this.policyNumber = policyNumber;
        this.claimType = claimType;
        this.claimAmount = claimAmount;
        this.approvedAmount = approvedAmount;
        this.status = status;
        this.hospitalName = hospitalName;
        this.incidentDate = incidentDate;
        this.description = description;
        this.submittedByUsername = submittedByUsername;
        this.reviewerNotes = reviewerNotes;
        this.submissionDate = submissionDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated
    public static class ClaimResponseDtoBuilder {
        @Generated
        private Long id;
        @Generated
        private String claimReference;
        @Generated
        private String policyNumber;
        @Generated
        private String claimType;
        @Generated
        private BigDecimal claimAmount;
        @Generated
        private BigDecimal approvedAmount;
        @Generated
        private String status;
        @Generated
        private String hospitalName;
        @Generated
        private LocalDate incidentDate;
        @Generated
        private String description;
        @Generated
        private String submittedByUsername;
        @Generated
        private String reviewerNotes;
        @Generated
        private LocalDateTime submissionDate;
        @Generated
        private LocalDateTime createdAt;
        @Generated
        private LocalDateTime updatedAt;

        @Generated
        ClaimResponseDtoBuilder() {
        }

        @Generated
        public ClaimResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder claimReference(String claimReference) {
            this.claimReference = claimReference;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder claimType(String claimType) {
            this.claimType = claimType;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder claimAmount(BigDecimal claimAmount) {
            this.claimAmount = claimAmount;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder approvedAmount(BigDecimal approvedAmount) {
            this.approvedAmount = approvedAmount;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder hospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder incidentDate(LocalDate incidentDate) {
            this.incidentDate = incidentDate;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder submittedByUsername(String submittedByUsername) {
            this.submittedByUsername = submittedByUsername;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder reviewerNotes(String reviewerNotes) {
            this.reviewerNotes = reviewerNotes;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder submissionDate(LocalDateTime submissionDate) {
            this.submissionDate = submissionDate;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public ClaimResponseDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Generated
        public ClaimResponseDto build() {
            return new ClaimResponseDto(this.id, this.claimReference, this.policyNumber, this.claimType, this.claimAmount, this.approvedAmount, this.status, this.hospitalName, this.incidentDate, this.description, this.submittedByUsername, this.reviewerNotes, this.submissionDate, this.createdAt, this.updatedAt);
        }

        @Generated
        public String toString() {
            return "ClaimResponseDto.ClaimResponseDtoBuilder(id=" + this.id + ", claimReference=" + this.claimReference + ", policyNumber=" + this.policyNumber + ", claimType=" + this.claimType + ", claimAmount=" + String.valueOf(this.claimAmount) + ", approvedAmount=" + String.valueOf(this.approvedAmount) + ", status=" + this.status + ", hospitalName=" + this.hospitalName + ", incidentDate=" + String.valueOf(this.incidentDate) + ", description=" + this.description + ", submittedByUsername=" + this.submittedByUsername + ", reviewerNotes=" + this.reviewerNotes + ", submissionDate=" + String.valueOf(this.submissionDate) + ", createdAt=" + String.valueOf(this.createdAt) + ", updatedAt=" + String.valueOf(this.updatedAt) + ")";
        }
    }
}
