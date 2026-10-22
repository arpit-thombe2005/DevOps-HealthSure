/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.core.io.Resource
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
 *  org.springframework.security.core.annotation.AuthenticationPrincipal
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.healthsure.controller;

import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.User;
import com.healthsure.security.CustomUserPrincipal;
import com.healthsure.service.ClaimService;
import com.healthsure.service.DocumentService;
import java.io.IOException;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/documents"})
public class DocumentController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private final DocumentService documentService;
    private final ClaimService claimService;

    @PostMapping(value={"/upload/{claimId}"})
    public String uploadDocument(@PathVariable Long claimId, @RequestParam(value="file") MultipartFile file, @RequestParam(required=false) String description, @AuthenticationPrincipal CustomUserPrincipal principal, RedirectAttributes redirectAttributes) {
        boolean isReviewerOrAdmin;
        User user = principal.getUser();
        InsuranceClaim claim = this.claimService.findById(claimId);
        boolean isOwner = claim.getSubmittedBy().getId().equals(user.getId());
        boolean bl = isReviewerOrAdmin = user.getRoles().contains("REVIEWER") || user.getRoles().contains("ADMIN");
        if (!isOwner && !isReviewerOrAdmin) {
            redirectAttributes.addFlashAttribute("error", (Object)"Access denied");
            return "redirect:/claims/" + claimId;
        }
        try {
            this.documentService.upload(file, claim, user, description);
            redirectAttributes.addFlashAttribute("success", (Object)"Document uploaded successfully");
        }
        catch (IOException e) {
            log.error("File upload IO error for claim {}", (Object)claimId);
            redirectAttributes.addFlashAttribute("error", (Object)"Upload failed. Please try again.");
        }
        catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/claims/" + claimId;
    }

    @GetMapping(value={"/download/{docId}/claim/{claimId}"})
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long docId, @PathVariable Long claimId, @AuthenticationPrincipal CustomUserPrincipal principal) {
        User user = principal.getUser();
        try {
            Resource resource = this.documentService.download(docId, claimId, user);
            ClaimDocument doc = this.documentService.findById(docId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(doc.getContentType()))
                    .header("Content-Disposition", "attachment; filename=\"" + doc.getOriginalFilename() + "\"")
                    .body(resource);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping(value={"/delete/{docId}/claim/{claimId}"})
    public String deleteDocument(@PathVariable Long docId, @PathVariable Long claimId, @AuthenticationPrincipal CustomUserPrincipal principal, RedirectAttributes redirectAttributes) {
        User user = principal.getUser();
        try {
            this.documentService.softDelete(docId, claimId, user);
            redirectAttributes.addFlashAttribute("success", (Object)"Document removed");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/claims/" + claimId;
    }

    @Generated
    public DocumentController(DocumentService documentService, ClaimService claimService) {
        this.documentService = documentService;
        this.claimService = claimService;
    }
}
