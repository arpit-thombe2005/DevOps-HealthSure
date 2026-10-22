/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.healthsure.dto;

import java.math.BigDecimal;
import lombok.Generated;

public class DashboardStats {
    private long totalClaims;
    private long draftClaims;
    private long submittedClaims;
    private long underReviewClaims;
    private long approvedClaims;
    private long rejectedClaims;
    private long validationFailedClaims;
    private long additionalInfoRequiredClaims;
    private BigDecimal totalClaimedAmount;
    private BigDecimal totalApprovedAmount;
    private long myTotalClaims;
    private long myActiveClaims;
    private long myApprovedClaims;
    private long totalUsers;
    private long totalPolicies;
    private long activePolicies;

    @Generated
    public static DashboardStatsBuilder builder() {
        return new DashboardStatsBuilder();
    }

    @Generated
    public long getTotalClaims() {
        return this.totalClaims;
    }

    @Generated
    public long getDraftClaims() {
        return this.draftClaims;
    }

    @Generated
    public long getSubmittedClaims() {
        return this.submittedClaims;
    }

    @Generated
    public long getUnderReviewClaims() {
        return this.underReviewClaims;
    }

    @Generated
    public long getApprovedClaims() {
        return this.approvedClaims;
    }

    @Generated
    public long getRejectedClaims() {
        return this.rejectedClaims;
    }

    @Generated
    public long getValidationFailedClaims() {
        return this.validationFailedClaims;
    }

    @Generated
    public long getAdditionalInfoRequiredClaims() {
        return this.additionalInfoRequiredClaims;
    }

    @Generated
    public BigDecimal getTotalClaimedAmount() {
        return this.totalClaimedAmount;
    }

    @Generated
    public BigDecimal getTotalApprovedAmount() {
        return this.totalApprovedAmount;
    }

    @Generated
    public long getMyTotalClaims() {
        return this.myTotalClaims;
    }

    @Generated
    public long getMyActiveClaims() {
        return this.myActiveClaims;
    }

    @Generated
    public long getMyApprovedClaims() {
        return this.myApprovedClaims;
    }

    @Generated
    public long getTotalUsers() {
        return this.totalUsers;
    }

    @Generated
    public long getTotalPolicies() {
        return this.totalPolicies;
    }

    @Generated
    public long getActivePolicies() {
        return this.activePolicies;
    }

    @Generated
    public void setTotalClaims(long totalClaims) {
        this.totalClaims = totalClaims;
    }

    @Generated
    public void setDraftClaims(long draftClaims) {
        this.draftClaims = draftClaims;
    }

    @Generated
    public void setSubmittedClaims(long submittedClaims) {
        this.submittedClaims = submittedClaims;
    }

    @Generated
    public void setUnderReviewClaims(long underReviewClaims) {
        this.underReviewClaims = underReviewClaims;
    }

    @Generated
    public void setApprovedClaims(long approvedClaims) {
        this.approvedClaims = approvedClaims;
    }

    @Generated
    public void setRejectedClaims(long rejectedClaims) {
        this.rejectedClaims = rejectedClaims;
    }

    @Generated
    public void setValidationFailedClaims(long validationFailedClaims) {
        this.validationFailedClaims = validationFailedClaims;
    }

    @Generated
    public void setAdditionalInfoRequiredClaims(long additionalInfoRequiredClaims) {
        this.additionalInfoRequiredClaims = additionalInfoRequiredClaims;
    }

    @Generated
    public void setTotalClaimedAmount(BigDecimal totalClaimedAmount) {
        this.totalClaimedAmount = totalClaimedAmount;
    }

    @Generated
    public void setTotalApprovedAmount(BigDecimal totalApprovedAmount) {
        this.totalApprovedAmount = totalApprovedAmount;
    }

    @Generated
    public void setMyTotalClaims(long myTotalClaims) {
        this.myTotalClaims = myTotalClaims;
    }

    @Generated
    public void setMyActiveClaims(long myActiveClaims) {
        this.myActiveClaims = myActiveClaims;
    }

    @Generated
    public void setMyApprovedClaims(long myApprovedClaims) {
        this.myApprovedClaims = myApprovedClaims;
    }

    @Generated
    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    @Generated
    public void setTotalPolicies(long totalPolicies) {
        this.totalPolicies = totalPolicies;
    }

    @Generated
    public void setActivePolicies(long activePolicies) {
        this.activePolicies = activePolicies;
    }

    @Generated
    public DashboardStats() {
    }

    @Generated
    public DashboardStats(long totalClaims, long draftClaims, long submittedClaims, long underReviewClaims, long approvedClaims, long rejectedClaims, long validationFailedClaims, long additionalInfoRequiredClaims, BigDecimal totalClaimedAmount, BigDecimal totalApprovedAmount, long myTotalClaims, long myActiveClaims, long myApprovedClaims, long totalUsers, long totalPolicies, long activePolicies) {
        this.totalClaims = totalClaims;
        this.draftClaims = draftClaims;
        this.submittedClaims = submittedClaims;
        this.underReviewClaims = underReviewClaims;
        this.approvedClaims = approvedClaims;
        this.rejectedClaims = rejectedClaims;
        this.validationFailedClaims = validationFailedClaims;
        this.additionalInfoRequiredClaims = additionalInfoRequiredClaims;
        this.totalClaimedAmount = totalClaimedAmount;
        this.totalApprovedAmount = totalApprovedAmount;
        this.myTotalClaims = myTotalClaims;
        this.myActiveClaims = myActiveClaims;
        this.myApprovedClaims = myApprovedClaims;
        this.totalUsers = totalUsers;
        this.totalPolicies = totalPolicies;
        this.activePolicies = activePolicies;
    }

    @Generated
    public static class DashboardStatsBuilder {
        @Generated
        private long totalClaims;
        @Generated
        private long draftClaims;
        @Generated
        private long submittedClaims;
        @Generated
        private long underReviewClaims;
        @Generated
        private long approvedClaims;
        @Generated
        private long rejectedClaims;
        @Generated
        private long validationFailedClaims;
        @Generated
        private long additionalInfoRequiredClaims;
        @Generated
        private BigDecimal totalClaimedAmount;
        @Generated
        private BigDecimal totalApprovedAmount;
        @Generated
        private long myTotalClaims;
        @Generated
        private long myActiveClaims;
        @Generated
        private long myApprovedClaims;
        @Generated
        private long totalUsers;
        @Generated
        private long totalPolicies;
        @Generated
        private long activePolicies;

        @Generated
        DashboardStatsBuilder() {
        }

        @Generated
        public DashboardStatsBuilder totalClaims(long totalClaims) {
            this.totalClaims = totalClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder draftClaims(long draftClaims) {
            this.draftClaims = draftClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder submittedClaims(long submittedClaims) {
            this.submittedClaims = submittedClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder underReviewClaims(long underReviewClaims) {
            this.underReviewClaims = underReviewClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder approvedClaims(long approvedClaims) {
            this.approvedClaims = approvedClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder rejectedClaims(long rejectedClaims) {
            this.rejectedClaims = rejectedClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder validationFailedClaims(long validationFailedClaims) {
            this.validationFailedClaims = validationFailedClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder additionalInfoRequiredClaims(long additionalInfoRequiredClaims) {
            this.additionalInfoRequiredClaims = additionalInfoRequiredClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder totalClaimedAmount(BigDecimal totalClaimedAmount) {
            this.totalClaimedAmount = totalClaimedAmount;
            return this;
        }

        @Generated
        public DashboardStatsBuilder totalApprovedAmount(BigDecimal totalApprovedAmount) {
            this.totalApprovedAmount = totalApprovedAmount;
            return this;
        }

        @Generated
        public DashboardStatsBuilder myTotalClaims(long myTotalClaims) {
            this.myTotalClaims = myTotalClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder myActiveClaims(long myActiveClaims) {
            this.myActiveClaims = myActiveClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder myApprovedClaims(long myApprovedClaims) {
            this.myApprovedClaims = myApprovedClaims;
            return this;
        }

        @Generated
        public DashboardStatsBuilder totalUsers(long totalUsers) {
            this.totalUsers = totalUsers;
            return this;
        }

        @Generated
        public DashboardStatsBuilder totalPolicies(long totalPolicies) {
            this.totalPolicies = totalPolicies;
            return this;
        }

        @Generated
        public DashboardStatsBuilder activePolicies(long activePolicies) {
            this.activePolicies = activePolicies;
            return this;
        }

        @Generated
        public DashboardStats build() {
            return new DashboardStats(this.totalClaims, this.draftClaims, this.submittedClaims, this.underReviewClaims, this.approvedClaims, this.rejectedClaims, this.validationFailedClaims, this.additionalInfoRequiredClaims, this.totalClaimedAmount, this.totalApprovedAmount, this.myTotalClaims, this.myActiveClaims, this.myApprovedClaims, this.totalUsers, this.totalPolicies, this.activePolicies);
        }

        @Generated
        public String toString() {
            return "DashboardStats.DashboardStatsBuilder(totalClaims=" + this.totalClaims + ", draftClaims=" + this.draftClaims + ", submittedClaims=" + this.submittedClaims + ", underReviewClaims=" + this.underReviewClaims + ", approvedClaims=" + this.approvedClaims + ", rejectedClaims=" + this.rejectedClaims + ", validationFailedClaims=" + this.validationFailedClaims + ", additionalInfoRequiredClaims=" + this.additionalInfoRequiredClaims + ", totalClaimedAmount=" + String.valueOf(this.totalClaimedAmount) + ", totalApprovedAmount=" + String.valueOf(this.totalApprovedAmount) + ", myTotalClaims=" + this.myTotalClaims + ", myActiveClaims=" + this.myActiveClaims + ", myApprovedClaims=" + this.myApprovedClaims + ", totalUsers=" + this.totalUsers + ", totalPolicies=" + this.totalPolicies + ", activePolicies=" + this.activePolicies + ")";
        }
    }
}
