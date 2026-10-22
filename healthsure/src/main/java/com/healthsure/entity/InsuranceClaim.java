/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.CascadeType
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
 *  jakarta.persistence.OneToMany
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.healthsure.entity;

import com.healthsure.entity.BaseEntity;
import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.ClaimReview;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

@Entity
@Table(name="insurance_claims", indexes={@Index(name="idx_claim_ref", columnList="claimReference"), @Index(name="idx_claim_policy", columnList="policy_id"), @Index(name="idx_claim_submitter", columnList="submittedBy_id"), @Index(name="idx_claim_status", columnList="status")})
public class InsuranceClaim
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=20)
    private String claimReference;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="policy_id", nullable=false)
    private InsurancePolicy policy;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="submittedBy_id", nullable=false)
    private User submittedBy;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=30)
    private ClaimType claimType;
    @Column(nullable=false, length=200)
    private String hospitalName;
    @Column(nullable=false, length=200)
    private String doctorName;
    @Column(nullable=false)
    private LocalDate treatmentStartDate;
    private LocalDate treatmentEndDate;
    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal claimedAmount;
    @Column(precision=15, scale=2)
    private BigDecimal approvedAmount;
    @Column(length=2000)
    private String description;
    @Column(length=2000)
    private String additionalNotes;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=30)
    private ClaimStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="assignedReviewer_id")
    private User assignedReviewer;
    @Column(length=2000)
    private String reviewerNotes;
    @Column(length=2000)
    private String rejectionReason;
    @Column(length=2000)
    private String additionalInfoRequest;
    @OneToMany(mappedBy="claim", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    private List<ClaimDocument> documents;
    @OneToMany(mappedBy="claim", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    private List<ClaimAuditEvent> auditEvents;
    @OneToMany(mappedBy="claim", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    private List<ClaimReview> reviews;

    @Generated
    private static ClaimStatus $default$status() {
        return ClaimStatus.DRAFT;
    }

    @Generated
    private static List<ClaimDocument> $default$documents() {
        return new ArrayList<ClaimDocument>();
    }

    @Generated
    private static List<ClaimAuditEvent> $default$auditEvents() {
        return new ArrayList<ClaimAuditEvent>();
    }

    @Generated
    private static List<ClaimReview> $default$reviews() {
        return new ArrayList<ClaimReview>();
    }

    @Generated
    public static InsuranceClaimBuilder builder() {
        return new InsuranceClaimBuilder();
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
    public InsurancePolicy getPolicy() {
        return this.policy;
    }

    @Generated
    public User getSubmittedBy() {
        return this.submittedBy;
    }

    @Generated
    public ClaimType getClaimType() {
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
    public BigDecimal getApprovedAmount() {
        return this.approvedAmount;
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
    public ClaimStatus getStatus() {
        return this.status;
    }

    @Generated
    public LocalDateTime getSubmittedAt() {
        return this.submittedAt;
    }

    @Generated
    public LocalDateTime getReviewedAt() {
        return this.reviewedAt;
    }

    @Generated
    public User getAssignedReviewer() {
        return this.assignedReviewer;
    }

    @Generated
    public String getReviewerNotes() {
        return this.reviewerNotes;
    }

    @Generated
    public String getRejectionReason() {
        return this.rejectionReason;
    }

    @Generated
    public String getAdditionalInfoRequest() {
        return this.additionalInfoRequest;
    }

    @Generated
    public List<ClaimDocument> getDocuments() {
        return this.documents;
    }

    @Generated
    public List<ClaimAuditEvent> getAuditEvents() {
        return this.auditEvents;
    }

    @Generated
    public List<ClaimReview> getReviews() {
        return this.reviews;
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
    public void setPolicy(InsurancePolicy policy) {
        this.policy = policy;
    }

    @Generated
    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }

    @Generated
    public void setClaimType(ClaimType claimType) {
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
    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
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
    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    @Generated
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Generated
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    @Generated
    public void setAssignedReviewer(User assignedReviewer) {
        this.assignedReviewer = assignedReviewer;
    }

    @Generated
    public void setReviewerNotes(String reviewerNotes) {
        this.reviewerNotes = reviewerNotes;
    }

    @Generated
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Generated
    public void setAdditionalInfoRequest(String additionalInfoRequest) {
        this.additionalInfoRequest = additionalInfoRequest;
    }

    @Generated
    public void setDocuments(List<ClaimDocument> documents) {
        this.documents = documents;
    }

    @Generated
    public void setAuditEvents(List<ClaimAuditEvent> auditEvents) {
        this.auditEvents = auditEvents;
    }

    @Generated
    public void setReviews(List<ClaimReview> reviews) {
        this.reviews = reviews;
    }

    @Generated
    public InsuranceClaim() {
        this.status = InsuranceClaim.$default$status();
        this.documents = InsuranceClaim.$default$documents();
        this.auditEvents = InsuranceClaim.$default$auditEvents();
        this.reviews = InsuranceClaim.$default$reviews();
    }

    @Generated
    public InsuranceClaim(Long id, String claimReference, InsurancePolicy policy, User submittedBy, ClaimType claimType, String hospitalName, String doctorName, LocalDate treatmentStartDate, LocalDate treatmentEndDate, BigDecimal claimedAmount, BigDecimal approvedAmount, String description, String additionalNotes, ClaimStatus status, LocalDateTime submittedAt, LocalDateTime reviewedAt, User assignedReviewer, String reviewerNotes, String rejectionReason, String additionalInfoRequest, List<ClaimDocument> documents, List<ClaimAuditEvent> auditEvents, List<ClaimReview> reviews) {
        this.id = id;
        this.claimReference = claimReference;
        this.policy = policy;
        this.submittedBy = submittedBy;
        this.claimType = claimType;
        this.hospitalName = hospitalName;
        this.doctorName = doctorName;
        this.treatmentStartDate = treatmentStartDate;
        this.treatmentEndDate = treatmentEndDate;
        this.claimedAmount = claimedAmount;
        this.approvedAmount = approvedAmount;
        this.description = description;
        this.additionalNotes = additionalNotes;
        this.status = status;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.assignedReviewer = assignedReviewer;
        this.reviewerNotes = reviewerNotes;
        this.rejectionReason = rejectionReason;
        this.additionalInfoRequest = additionalInfoRequest;
        this.documents = documents;
        this.auditEvents = auditEvents;
        this.reviews = reviews;
    }

    public static enum ClaimStatus {
        DRAFT,
        SUBMITTED,
        VALIDATION_FAILED,
        UNDER_REVIEW,
        ADDITIONAL_INFO_REQUIRED,
        APPROVED,
        REJECTED;

    }

    @Generated
    public static class InsuranceClaimBuilder {
        @Generated
        private Long id;
        @Generated
        private String claimReference;
        @Generated
        private InsurancePolicy policy;
        @Generated
        private User submittedBy;
        @Generated
        private ClaimType claimType;
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
        private BigDecimal approvedAmount;
        @Generated
        private String description;
        @Generated
        private String additionalNotes;
        @Generated
        private boolean status$set;
        @Generated
        private ClaimStatus status$value;
        @Generated
        private LocalDateTime submittedAt;
        @Generated
        private LocalDateTime reviewedAt;
        @Generated
        private User assignedReviewer;
        @Generated
        private String reviewerNotes;
        @Generated
        private String rejectionReason;
        @Generated
        private String additionalInfoRequest;
        @Generated
        private boolean documents$set;
        @Generated
        private List<ClaimDocument> documents$value;
        @Generated
        private boolean auditEvents$set;
        @Generated
        private List<ClaimAuditEvent> auditEvents$value;
        @Generated
        private boolean reviews$set;
        @Generated
        private List<ClaimReview> reviews$value;

        @Generated
        InsuranceClaimBuilder() {
        }

        @Generated
        public InsuranceClaimBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder claimReference(String claimReference) {
            this.claimReference = claimReference;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder policy(InsurancePolicy policy) {
            this.policy = policy;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder submittedBy(User submittedBy) {
            this.submittedBy = submittedBy;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder claimType(ClaimType claimType) {
            this.claimType = claimType;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder hospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder doctorName(String doctorName) {
            this.doctorName = doctorName;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder treatmentStartDate(LocalDate treatmentStartDate) {
            this.treatmentStartDate = treatmentStartDate;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder treatmentEndDate(LocalDate treatmentEndDate) {
            this.treatmentEndDate = treatmentEndDate;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder claimedAmount(BigDecimal claimedAmount) {
            this.claimedAmount = claimedAmount;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder approvedAmount(BigDecimal approvedAmount) {
            this.approvedAmount = approvedAmount;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder additionalNotes(String additionalNotes) {
            this.additionalNotes = additionalNotes;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder status(ClaimStatus status) {
            this.status$value = status;
            this.status$set = true;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder submittedAt(LocalDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder reviewedAt(LocalDateTime reviewedAt) {
            this.reviewedAt = reviewedAt;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder assignedReviewer(User assignedReviewer) {
            this.assignedReviewer = assignedReviewer;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder reviewerNotes(String reviewerNotes) {
            this.reviewerNotes = reviewerNotes;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder additionalInfoRequest(String additionalInfoRequest) {
            this.additionalInfoRequest = additionalInfoRequest;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder documents(List<ClaimDocument> documents) {
            this.documents$value = documents;
            this.documents$set = true;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder auditEvents(List<ClaimAuditEvent> auditEvents) {
            this.auditEvents$value = auditEvents;
            this.auditEvents$set = true;
            return this;
        }

        @Generated
        public InsuranceClaimBuilder reviews(List<ClaimReview> reviews) {
            this.reviews$value = reviews;
            this.reviews$set = true;
            return this;
        }

        @Generated
        public InsuranceClaim build() {
            ClaimStatus status$value = this.status$value;
            if (!this.status$set) {
                status$value = InsuranceClaim.$default$status();
            }
            List<ClaimDocument> documents$value = this.documents$value;
            if (!this.documents$set) {
                documents$value = InsuranceClaim.$default$documents();
            }
            List<ClaimAuditEvent> auditEvents$value = this.auditEvents$value;
            if (!this.auditEvents$set) {
                auditEvents$value = InsuranceClaim.$default$auditEvents();
            }
            List<ClaimReview> reviews$value = this.reviews$value;
            if (!this.reviews$set) {
                reviews$value = InsuranceClaim.$default$reviews();
            }
            return new InsuranceClaim(this.id, this.claimReference, this.policy, this.submittedBy, this.claimType, this.hospitalName, this.doctorName, this.treatmentStartDate, this.treatmentEndDate, this.claimedAmount, this.approvedAmount, this.description, this.additionalNotes, status$value, this.submittedAt, this.reviewedAt, this.assignedReviewer, this.reviewerNotes, this.rejectionReason, this.additionalInfoRequest, documents$value, auditEvents$value, reviews$value);
        }

        @Generated
        public String toString() {
            return "InsuranceClaim.InsuranceClaimBuilder(id=" + this.id + ", claimReference=" + this.claimReference + ", policy=" + String.valueOf(this.policy) + ", submittedBy=" + String.valueOf(this.submittedBy) + ", claimType=" + String.valueOf((Object)this.claimType) + ", hospitalName=" + this.hospitalName + ", doctorName=" + this.doctorName + ", treatmentStartDate=" + String.valueOf(this.treatmentStartDate) + ", treatmentEndDate=" + String.valueOf(this.treatmentEndDate) + ", claimedAmount=" + String.valueOf(this.claimedAmount) + ", approvedAmount=" + String.valueOf(this.approvedAmount) + ", description=" + this.description + ", additionalNotes=" + this.additionalNotes + ", status$value=" + String.valueOf((Object)this.status$value) + ", submittedAt=" + String.valueOf(this.submittedAt) + ", reviewedAt=" + String.valueOf(this.reviewedAt) + ", assignedReviewer=" + String.valueOf(this.assignedReviewer) + ", reviewerNotes=" + this.reviewerNotes + ", rejectionReason=" + this.rejectionReason + ", additionalInfoRequest=" + this.additionalInfoRequest + ", documents$value=" + String.valueOf(this.documents$value) + ", auditEvents$value=" + String.valueOf(this.auditEvents$value) + ", reviews$value=" + String.valueOf(this.reviews$value) + ")";
        }
    }

    public static enum ClaimType {
        HOSPITALIZATION,
        MEDICAL_PROCEDURE,
        EMERGENCY_TREATMENT,
        OUTPATIENT,
        DIAGNOSTIC,
        PRESCRIPTION,
        DENTAL,
        VISION,
        MATERNITY;

    }
}
