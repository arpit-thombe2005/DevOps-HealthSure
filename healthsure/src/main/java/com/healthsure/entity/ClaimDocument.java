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
import lombok.Generated;

@Entity
@Table(name="claim_documents", indexes={@Index(name="idx_doc_claim", columnList="claim_id")})
public class ClaimDocument
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="claim_id", nullable=false)
    private InsuranceClaim claim;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="uploadedBy_id", nullable=false)
    private User uploadedBy;
    @Column(nullable=false, length=200)
    private String originalFilename;
    @Column(nullable=false, length=200)
    private String storedFilename;
    @Column(nullable=false, length=50)
    private String contentType;
    @Column(nullable=false)
    private Long fileSize;
    @Column(nullable=false, length=500)
    private String storagePath;
    @Column(length=200)
    private String description;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false, length=20)
    private DocumentStatus status;

    @Generated
    private static DocumentStatus $default$status() {
        return DocumentStatus.ACTIVE;
    }

    @Generated
    public static ClaimDocumentBuilder builder() {
        return new ClaimDocumentBuilder();
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
    public User getUploadedBy() {
        return this.uploadedBy;
    }

    @Generated
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Generated
    public String getStoredFilename() {
        return this.storedFilename;
    }

    @Generated
    public String getContentType() {
        return this.contentType;
    }

    @Generated
    public Long getFileSize() {
        return this.fileSize;
    }

    @Generated
    public String getStoragePath() {
        return this.storagePath;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public DocumentStatus getStatus() {
        return this.status;
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
    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Generated
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    @Generated
    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    @Generated
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Generated
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Generated
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    @Generated
    public ClaimDocument() {
        this.status = ClaimDocument.$default$status();
    }

    @Generated
    public ClaimDocument(Long id, InsuranceClaim claim, User uploadedBy, String originalFilename, String storedFilename, String contentType, Long fileSize, String storagePath, String description, DocumentStatus status) {
        this.id = id;
        this.claim = claim;
        this.uploadedBy = uploadedBy;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.storagePath = storagePath;
        this.description = description;
        this.status = status;
    }

    public static enum DocumentStatus {
        ACTIVE,
        DELETED;

    }

    @Generated
    public static class ClaimDocumentBuilder {
        @Generated
        private Long id;
        @Generated
        private InsuranceClaim claim;
        @Generated
        private User uploadedBy;
        @Generated
        private String originalFilename;
        @Generated
        private String storedFilename;
        @Generated
        private String contentType;
        @Generated
        private Long fileSize;
        @Generated
        private String storagePath;
        @Generated
        private String description;
        @Generated
        private boolean status$set;
        @Generated
        private DocumentStatus status$value;

        @Generated
        ClaimDocumentBuilder() {
        }

        @Generated
        public ClaimDocumentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder claim(InsuranceClaim claim) {
            this.claim = claim;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder uploadedBy(User uploadedBy) {
            this.uploadedBy = uploadedBy;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder originalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder storedFilename(String storedFilename) {
            this.storedFilename = storedFilename;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder storagePath(String storagePath) {
            this.storagePath = storagePath;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ClaimDocumentBuilder status(DocumentStatus status) {
            this.status$value = status;
            this.status$set = true;
            return this;
        }

        @Generated
        public ClaimDocument build() {
            DocumentStatus status$value = this.status$value;
            if (!this.status$set) {
                status$value = ClaimDocument.$default$status();
            }
            return new ClaimDocument(this.id, this.claim, this.uploadedBy, this.originalFilename, this.storedFilename, this.contentType, this.fileSize, this.storagePath, this.description, status$value);
        }

        @Generated
        public String toString() {
            return "ClaimDocument.ClaimDocumentBuilder(id=" + this.id + ", claim=" + String.valueOf(this.claim) + ", uploadedBy=" + String.valueOf(this.uploadedBy) + ", originalFilename=" + this.originalFilename + ", storedFilename=" + this.storedFilename + ", contentType=" + this.contentType + ", fileSize=" + this.fileSize + ", storagePath=" + this.storagePath + ", description=" + this.description + ", status$value=" + String.valueOf((Object)this.status$value) + ")";
        }
    }
}
