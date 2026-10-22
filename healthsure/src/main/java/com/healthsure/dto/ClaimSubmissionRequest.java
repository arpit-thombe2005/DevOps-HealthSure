/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.constraints.DecimalMax
 *  jakarta.validation.constraints.DecimalMin
 *  jakarta.validation.constraints.NotBlank
 *  jakarta.validation.constraints.NotNull
 *  jakarta.validation.constraints.Size
 *  lombok.Generated
 *  org.springframework.format.annotation.DateTimeFormat
 *  org.springframework.format.annotation.DateTimeFormat$ISO
 */
package com.healthsure.dto;

import com.healthsure.entity.InsuranceClaim;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Generated;
import org.springframework.format.annotation.DateTimeFormat;

public class ClaimSubmissionRequest {
    @NotNull(message="Policy is required")
    private @NotNull(message="Policy is required") Long policyId;
    @NotNull(message="Claim type is required")
    private  @NotNull(message="Claim type is required") InsuranceClaim.ClaimType claimType;
    @NotBlank(message="Hospital name is required")
    @Size(max=200)
    private @NotBlank(message="Hospital name is required") @Size(max=200) String hospitalName;
    @NotBlank(message="Doctor name is required")
    @Size(max=200)
    private @NotBlank(message="Doctor name is required") @Size(max=200) String doctorName;
    @NotNull(message="Treatment start date is required")
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private @NotNull(message="Treatment start date is required") LocalDate treatmentStartDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate treatmentEndDate;
    @NotNull(message="Claimed amount is required")
    @DecimalMin(value="0.01", message="Amount must be greater than zero")
    @DecimalMax(value="9999999.99", message="Amount exceeds maximum allowed")
    private @NotNull(message="Claimed amount is required") @DecimalMin(value="0.01", message="Amount must be greater than zero") @DecimalMax(value="9999999.99", message="Amount exceeds maximum allowed") BigDecimal claimedAmount;
    @NotBlank(message="Description is required")
    @Size(min=20, max=2000, message="Description must be 20-2000 characters")
    private @NotBlank(message="Description is required") @Size(min=20, max=2000, message="Description must be 20-2000 characters") String description;
    @Size(max=2000)
    private @Size(max=2000) String additionalNotes;
    private boolean submitNow;

    @Generated
    private static boolean $default$submitNow() {
        return false;
    }

    @Generated
    public static ClaimSubmissionRequestBuilder builder() {
        return new ClaimSubmissionRequestBuilder();
    }

    @Generated
    public Long getPolicyId() {
        return this.policyId;
    }

    @Generated
    public InsuranceClaim.ClaimType getClaimType() {
        return this.claimType;
    }

    @Generated
    public String getHospitalName() {
        return this.hospitalName;
    }

    @Generated
    public String getDoctorName() {
        return this.doctorName;
    }

    @Generated
    public LocalDate getTreatmentStartDate() {
        return this.treatmentStartDate;
    }

    @Generated
    public LocalDate getTreatmentEndDate() {
        return this.treatmentEndDate;
    }

    @Generated
    public BigDecimal getClaimedAmount() {
        return this.claimedAmount;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getAdditionalNotes() {
        return this.additionalNotes;
    }

    @Generated
    public boolean isSubmitNow() {
        return this.submitNow;
    }

    @Generated
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    @Generated
    public void setClaimType(InsuranceClaim.ClaimType claimType) {
        this.claimType = claimType;
    }

    @Generated
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Generated
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Generated
    public void setTreatmentStartDate(LocalDate treatmentStartDate) {
        this.treatmentStartDate = treatmentStartDate;
    }

    @Generated
    public void setTreatmentEndDate(LocalDate treatmentEndDate) {
        this.treatmentEndDate = treatmentEndDate;
    }

    @Generated
    public void setClaimedAmount(BigDecimal claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    @Generated
    public void setSubmitNow(boolean submitNow) {
        this.submitNow = submitNow;
    }

    @Generated
    public ClaimSubmissionRequest() {
        this.submitNow = ClaimSubmissionRequest.$default$submitNow();
    }

    @Generated
    public ClaimSubmissionRequest(Long policyId, InsuranceClaim.ClaimType claimType, String hospitalName, String doctorName, LocalDate treatmentStartDate, LocalDate treatmentEndDate, BigDecimal claimedAmount, String description, String additionalNotes, boolean submitNow) {
        this.policyId = policyId;
        this.claimType = claimType;
        this.hospitalName = hospitalName;
        this.doctorName = doctorName;
        this.treatmentStartDate = treatmentStartDate;
        this.treatmentEndDate = treatmentEndDate;
        this.claimedAmount = claimedAmount;
        this.description = description;
        this.additionalNotes = additionalNotes;
        this.submitNow = submitNow;
    }

    @Generated
    public static class ClaimSubmissionRequestBuilder {
        @Generated
        private Long policyId;
        @Generated
        private InsuranceClaim.ClaimType claimType;
        @Generated
        private String hospitalName;
        @Generated
        private String doctorName;
        @Generated
        private LocalDate treatmentStartDate;
        @Generated
        private LocalDate treatmentEndDate;
        @Generated
        private BigDecimal claimedAmount;
        @Generated
        private String description;
        @Generated
        private String additionalNotes;
        @Generated
        private boolean submitNow$set;
        @Generated
        private boolean submitNow$value;

        @Generated
        ClaimSubmissionRequestBuilder() {
        }

        @Generated
        public ClaimSubmissionRequestBuilder policyId(Long policyId) {
            this.policyId = policyId;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder claimType(InsuranceClaim.ClaimType claimType) {
            this.claimType = claimType;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder hospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder doctorName(String doctorName) {
            this.doctorName = doctorName;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder treatmentStartDate(LocalDate treatmentStartDate) {
            this.treatmentStartDate = treatmentStartDate;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder treatmentEndDate(LocalDate treatmentEndDate) {
            this.treatmentEndDate = treatmentEndDate;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder claimedAmount(BigDecimal claimedAmount) {
            this.claimedAmount = claimedAmount;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder additionalNotes(String additionalNotes) {
            this.additionalNotes = additionalNotes;
            return this;
        }

        @Generated
        public ClaimSubmissionRequestBuilder submitNow(boolean submitNow) {
            this.submitNow$value = submitNow;
            this.submitNow$set = true;
            return this;
        }

        @Generated
        public ClaimSubmissionRequest build() {
            boolean submitNow$value = this.submitNow$value;
            if (!this.submitNow$set) {
                submitNow$value = ClaimSubmissionRequest.$default$submitNow();
            }
            return new ClaimSubmissionRequest(this.policyId, this.claimType, this.hospitalName, this.doctorName, this.treatmentStartDate, this.treatmentEndDate, this.claimedAmount, this.description, this.additionalNotes, submitNow$value);
        }

        @Generated
        public String toString() {
            return "ClaimSubmissionRequest.ClaimSubmissionRequestBuilder(policyId=" + this.policyId + ", claimType=" + String.valueOf((Object)this.claimType) + ", hospitalName=" + this.hospitalName + ", doctorName=" + this.doctorName + ", treatmentStartDate=" + String.valueOf(this.treatmentStartDate) + ", treatmentEndDate=" + String.valueOf(this.treatmentEndDate) + ", claimedAmount=" + String.valueOf(this.claimedAmount) + ", description=" + this.description + ", additionalNotes=" + this.additionalNotes + ", submitNow$value=" + this.submitNow$value + ")";
        }
    }
}
