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
import com.healthsure.entity.InsuranceClaim;
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
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

@Entity
@Table(name="insurance_policies", indexes={@Index(name="idx_policy_number", columnList="policyNumber"), @Index(name="idx_policy_holder", columnList="policyholder_id")})
public class InsurancePolicy
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=20)
    private String policyNumber;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="policyholder_id", nullable=false)
    private User policyholder;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=30)
    private PolicyType policyType;
    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal coverageAmount;
    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal usedCoverage;
    @Column(nullable=false)
    private LocalDate startDate;
    @Column(nullable=false)
    private LocalDate expiryDate;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=20)
    private PolicyStatus status;
    @Column(length=500)
    private String description;
    @OneToMany(mappedBy="policy", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    private List<InsuranceClaim> claims;

    public BigDecimal getAvailableCoverage() {
        return this.coverageAmount.subtract(this.usedCoverage);
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return this.status == PolicyStatus.ACTIVE && !this.startDate.isAfter(today) && !this.expiryDate.isBefore(today);
    }

    @Generated
    private static PolicyStatus $default$status() {
        return PolicyStatus.ACTIVE;
    }

    @Generated
    private static List<InsuranceClaim> $default$claims() {
        return new ArrayList<InsuranceClaim>();
    }

    @Generated
    public static InsurancePolicyBuilder builder() {
        return new InsurancePolicyBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getPolicyNumber() {
        return this.policyNumber;
    }

    @Generated
    public User getPolicyholder() {
        return this.policyholder;
    }

    @Generated
    public PolicyType getPolicyType() {
        return this.policyType;
    }

    @Generated
    public BigDecimal getCoverageAmount() {
        return this.coverageAmount;
    }

    @Generated
    public BigDecimal getUsedCoverage() {
        return this.usedCoverage;
    }

    @Generated
    public LocalDate getStartDate() {
        return this.startDate;
    }

    @Generated
    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    @Generated
    public PolicyStatus getStatus() {
        return this.status;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public List<InsuranceClaim> getClaims() {
        return this.claims;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    @Generated
    public void setPolicyholder(User policyholder) {
        this.policyholder = policyholder;
    }

    @Generated
    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    @Generated
    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    @Generated
    public void setUsedCoverage(BigDecimal usedCoverage) {
        this.usedCoverage = usedCoverage;
    }

    @Generated
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Generated
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Generated
    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setClaims(List<InsuranceClaim> claims) {
        this.claims = claims;
    }

    @Generated
    public InsurancePolicy() {
        this.status = InsurancePolicy.$default$status();
        this.claims = InsurancePolicy.$default$claims();
    }

    @Generated
    public InsurancePolicy(Long id, String policyNumber, User policyholder, PolicyType policyType, BigDecimal coverageAmount, BigDecimal usedCoverage, LocalDate startDate, LocalDate expiryDate, PolicyStatus status, String description, List<InsuranceClaim> claims) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.policyholder = policyholder;
        this.policyType = policyType;
        this.coverageAmount = coverageAmount;
        this.usedCoverage = usedCoverage;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.description = description;
        this.claims = claims;
    }

    public static enum PolicyStatus {
        ACTIVE,
        EXPIRED,
        CANCELLED,
        SUSPENDED;

    }

    @Generated
    public static class InsurancePolicyBuilder {
        @Generated
        private Long id;
        @Generated
        private String policyNumber;
        @Generated
        private User policyholder;
        @Generated
        private PolicyType policyType;
        @Generated
        private BigDecimal coverageAmount;
        @Generated
        private BigDecimal usedCoverage;
        @Generated
        private LocalDate startDate;
        @Generated
        private LocalDate expiryDate;
        @Generated
        private boolean status$set;
        @Generated
        private PolicyStatus status$value;
        @Generated
        private String description;
        @Generated
        private boolean claims$set;
        @Generated
        private List<InsuranceClaim> claims$value;

        @Generated
        InsurancePolicyBuilder() {
        }

        @Generated
        public InsurancePolicyBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder policyholder(User policyholder) {
            this.policyholder = policyholder;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder policyType(PolicyType policyType) {
            this.policyType = policyType;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder coverageAmount(BigDecimal coverageAmount) {
            this.coverageAmount = coverageAmount;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder usedCoverage(BigDecimal usedCoverage) {
            this.usedCoverage = usedCoverage;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder status(PolicyStatus status) {
            this.status$value = status;
            this.status$set = true;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public InsurancePolicyBuilder claims(List<InsuranceClaim> claims) {
            this.claims$value = claims;
            this.claims$set = true;
            return this;
        }

        @Generated
        public InsurancePolicy build() {
            PolicyStatus status$value = this.status$value;
            if (!this.status$set) {
                status$value = InsurancePolicy.$default$status();
            }
            List<InsuranceClaim> claims$value = this.claims$value;
            if (!this.claims$set) {
                claims$value = InsurancePolicy.$default$claims();
            }
            return new InsurancePolicy(this.id, this.policyNumber, this.policyholder, this.policyType, this.coverageAmount, this.usedCoverage, this.startDate, this.expiryDate, status$value, this.description, claims$value);
        }

        @Generated
        public String toString() {
            return "InsurancePolicy.InsurancePolicyBuilder(id=" + this.id + ", policyNumber=" + this.policyNumber + ", policyholder=" + String.valueOf(this.policyholder) + ", policyType=" + String.valueOf((Object)this.policyType) + ", coverageAmount=" + String.valueOf(this.coverageAmount) + ", usedCoverage=" + String.valueOf(this.usedCoverage) + ", startDate=" + String.valueOf(this.startDate) + ", expiryDate=" + String.valueOf(this.expiryDate) + ", status$value=" + String.valueOf((Object)this.status$value) + ", description=" + this.description + ", claims$value=" + String.valueOf(this.claims$value) + ")";
        }
    }

    public static enum PolicyType {
        INDIVIDUAL,
        FAMILY,
        GROUP,
        SENIOR,
        CRITICAL_ILLNESS;

    }
}
