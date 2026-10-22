/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.Valid
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.security.core.annotation.AuthenticationPrincipal
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.healthsure.controller;

import com.healthsure.dto.ReviewDecisionRequest;
import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.User;
import com.healthsure.security.CustomUserPrincipal;
import com.healthsure.service.AuditService;
import com.healthsure.service.ClaimService;
import com.healthsure.service.DocumentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/reviewer"})
@PreAuthorize(value="hasAnyRole('REVIEWER','ADMIN')")
public class ReviewerController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ReviewerController.class);
    private final ClaimService claimService;
    private final AuditService auditService;
    private final DocumentService documentService;

    @GetMapping(value={"/dashboard"})
    public String dashboard(@AuthenticationPrincipal CustomUserPrincipal principal, Model model) {
        User user = principal.getUser();
        model.addAttribute("stats", (Object)this.claimService.getDashboardStats(user, "REVIEWER"));
        model.addAttribute("recentClaims", this.claimService.findRecent(5));
        return "reviewer/dashboard";
    }

    @GetMapping(value={"/queue"})
    public String claimQueue(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="15") int size, Model model) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"submittedAt"}).ascending());
        Page<InsuranceClaim> claims = this.claimService.findPendingReview((Pageable)pageable);
        model.addAttribute("claims", claims);
        model.addAttribute("currentPage", (Object)page);
        model.addAttribute("totalPages", (Object)claims.getTotalPages());
        return "reviewer/queue";
    }

    @GetMapping(value={"/claims/{id}"})
    public String reviewClaim(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal, Model model) {
        InsuranceClaim claim = this.claimService.findById(id);
        List<ClaimDocument> documents = this.documentService.findActiveDocuments(claim);
        List<ClaimAuditEvent> auditEvents = this.auditService.getClaimHistory(claim);
        model.addAttribute("claim", (Object)claim);
        model.addAttribute("documents", documents);
        model.addAttribute("auditEvents", auditEvents);
        model.addAttribute("decisionRequest", (Object)new ReviewDecisionRequest());
        return "reviewer/review-detail";
    }

    @PostMapping(value={"/claims/{id}/decide"})
    public String decide(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal, @Valid @ModelAttribute(value="decisionRequest") ReviewDecisionRequest request, RedirectAttributes redirectAttributes) {
        User reviewer = principal.getUser();
        try {
            redirectAttributes.addFlashAttribute("success", (Object)("Decision recorded: " + (switch (request.getDecision()) {
                case InsuranceClaim.ClaimStatus.APPROVED -> this.claimService.approve(id, reviewer, request.getNotes(), request.getRecommendedAmount());
                case InsuranceClaim.ClaimStatus.REJECTED -> this.claimService.reject(id, reviewer, request.getDecisionReason(), request.getNotes());
                case InsuranceClaim.ClaimStatus.ADDITIONAL_INFO_REQUIRED -> this.claimService.requestAdditionalInfo(id, reviewer, request.getAdditionalInfoRequest(), request.getNotes());
                default -> throw new IllegalArgumentException("Invalid decision: " + String.valueOf((Object)request.getDecision()));
            }).getStatus().name()));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/reviewer/claims/" + id;
    }

    @Generated
    public ReviewerController(ClaimService claimService, AuditService auditService, DocumentService documentService) {
        this.claimService = claimService;
        this.auditService = auditService;
        this.documentService = documentService;
    }
}
