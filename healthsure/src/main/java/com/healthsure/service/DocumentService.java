/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.apache.commons.io.FilenameUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.core.io.Resource
 *  org.springframework.core.io.UrlResource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.web.multipart.MultipartFile
 */
package com.healthsure.service;

import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.User;
import com.healthsure.exception.AccessDeniedException;
import com.healthsure.exception.ResourceNotFoundException;
import com.healthsure.repository.ClaimDocumentRepository;
import com.healthsure.service.AuditService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Generated;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    private final ClaimDocumentRepository documentRepository;
    private final AuditService auditService;
    @Value(value="${healthsure.upload.dir:./uploads}")
    private String uploadDir;
    @Value(value="${healthsure.upload.max-size-mb:10}")
    private int maxSizeMb;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png", "doc", "docx");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("application/pdf", "image/jpeg", "image/png", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    @Transactional
    public ClaimDocument upload(MultipartFile file, InsuranceClaim claim, User uploader, String description) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        long maxBytes = (long)this.maxSizeMb * 1024L * 1024L;
        if (file.getSize() > maxBytes) {
            throw new IllegalArgumentException("File exceeds maximum size of " + this.maxSizeMb + " MB");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension((String)originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("File type not allowed. Permitted: PDF, JPG, PNG, DOC, DOCX");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid content type: " + contentType);
        }
        String safeOriginal = Path.of(originalFilename, new String[0]).getFileName().toString();
        String stored = String.valueOf(UUID.randomUUID()) + "." + extension;
        Path claimDir = Path.of(this.uploadDir, "claims", String.valueOf(claim.getId()));
        Files.createDirectories(claimDir, new FileAttribute[0]);
        Path target = claimDir.resolve(stored);
        if (!target.toAbsolutePath().startsWith(Path.of(this.uploadDir, new String[0]).toAbsolutePath())) {
            throw new IllegalArgumentException("Invalid file path");
        }
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        ClaimDocument doc = ClaimDocument.builder().claim(claim).uploadedBy(uploader).originalFilename(safeOriginal).storedFilename(stored).contentType(contentType).fileSize(file.getSize()).storagePath(target.toString()).description(description).status(ClaimDocument.DocumentStatus.ACTIVE).build();
        ClaimDocument saved = (ClaimDocument)this.documentRepository.save(doc);
        this.auditService.recordEvent(claim, uploader, ClaimAuditEvent.EventType.DOCUMENT_UPLOADED, null, null, "Document uploaded: " + safeOriginal);
        log.info("Document uploaded for claim {}: {}", (Object)claim.getClaimReference(), (Object)safeOriginal);
        return saved;
    }

    @Transactional(readOnly=true)
    public Resource download(Long docId, Long claimId, User requestor) throws MalformedURLException {
        boolean isReviewerOrAdmin;
        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(claimId);
        ClaimDocument doc = (ClaimDocument)this.documentRepository.findById(docId).orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        if (!doc.getClaim().getId().equals(claimId)) {
            throw new AccessDeniedException("Document does not belong to this claim");
        }
        boolean isOwner = doc.getClaim().getSubmittedBy().getId().equals(requestor.getId());
        boolean bl = isReviewerOrAdmin = requestor.getRoles().contains("REVIEWER") || requestor.getRoles().contains("ADMIN");
        if (!isOwner && !isReviewerOrAdmin) {
            throw new AccessDeniedException("Access denied to document");
        }
        Path filePath = Path.of(doc.getStoragePath(), new String[0]);
        UrlResource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResourceNotFoundException("File not found on server");
        }
        return resource;
    }

    @Transactional(readOnly=true)
    public List<ClaimDocument> findActiveDocuments(InsuranceClaim claim) {
        return this.documentRepository.findByClaimAndStatus(claim, ClaimDocument.DocumentStatus.ACTIVE);
    }

    @Transactional(readOnly=true)
    public ClaimDocument findById(Long id) {
        return (ClaimDocument)this.documentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Document not found: " + id));
    }

    @Transactional
    public void softDelete(Long docId, Long claimId, User actor) {
        ClaimDocument doc = (ClaimDocument)this.documentRepository.findById(docId).orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        if (!doc.getClaim().getId().equals(claimId)) {
            throw new AccessDeniedException("Document does not belong to this claim");
        }
        boolean isOwner = doc.getClaim().getSubmittedBy().getId().equals(actor.getId());
        boolean isAdmin = actor.getRoles().contains("ADMIN");
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
        doc.setStatus(ClaimDocument.DocumentStatus.DELETED);
        this.documentRepository.save(doc);
        this.auditService.recordEvent(doc.getClaim(), actor, ClaimAuditEvent.EventType.DOCUMENT_DELETED, null, null, "Document deleted: " + doc.getOriginalFilename());
    }

    @Generated
    public DocumentService(ClaimDocumentRepository documentRepository, AuditService auditService) {
        this.documentRepository = documentRepository;
        this.auditService = auditService;
    }
}
