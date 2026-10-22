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
 *  org.springframework.security.core.annotation.AuthenticationPrincipal
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.validation.BindingResult
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.healthsure.controller;

import com.healthsure.dto.ClaimSubmissionRequest;
import com.healthsure.dto.DashboardStats;
import com.healthsure.entity.ClaimAuditEvent;
import com.healthsure.entity.ClaimDocument;
import com.healthsure.entity.InsuranceClaim;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.security.CustomUserPrincipal;
import com.healthsure.service.AuditService;
import com.healthsure.service.ClaimService;
import com.healthsure.service.DocumentService;
import com.healthsure.service.PolicyService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/claims"})
public class ClaimController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ClaimController.class);
    private final ClaimService claimService;
    private final PolicyService policyService;
    private final AuditService auditService;
    private final DocumentService documentService;

    @GetMapping
    public String listClaims(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, Model model) {
        User user = principal.getUser();
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"createdAt"}).descending());
        boolean isAdmin = user.getRoles().contains("ADMIN");
        boolean isReviewer = user.getRoles().contains("REVIEWER");
        Page<InsuranceClaim> claims = isAdmin ? this.claimService.findAll((Pageable)pageable) : (isReviewer ? this.claimService.findPendingReview((Pageable)pageable) : this.claimService.findByUser(user, (Pageable)pageable));
        DashboardStats stats = this.claimService.getDashboardStats(user, this.getPrimaryRole(user));
        model.addAttribute("claims", claims);
        model.addAttribute("stats", (Object)stats);
        model.addAttribute("currentPage", (Object)page);
        model.addAttribute("totalPages", (Object)claims.getTotalPages());
        model.addAttribute("pageTitle", (Object)(isReviewer ? "Claim Review Queue" : (isAdmin ? "All Claims" : "My Claims")));
        return "claim/list";
    }

    @GetMapping(value={"/new"})
    public String newClaimForm(@AuthenticationPrincipal CustomUserPrincipal principal, Model model) {
        User user = principal.getUser();
        List<InsurancePolicy> policies = this.policyService.findActivePoliciesForUser(user);
        model.addAttribute("policies", policies);
        model.addAttribute("claimTypes", (Object)InsuranceClaim.ClaimType.values());
        model.addAttribute("claimRequest", (Object)new ClaimSubmissionRequest());
        return "claim/new";
    }

    @PostMapping(value={"/new"})
    public String submitClaim(@AuthenticationPrincipal CustomUserPrincipal principal, @Valid @ModelAttribute(value="claimRequest") ClaimSubmissionRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        User user = principal.getUser();
        if (bindingResult.hasErrors()) {
            model.addAttribute("policies", this.policyService.findActivePoliciesForUser(user));
            model.addAttribute("claimTypes", (Object)InsuranceClaim.ClaimType.values());
            return "claim/new";
        }
        try {
            InsuranceClaim claim = this.claimService.createDraft(request, user);
            String status = claim.getStatus().name();
            redirectAttributes.addFlashAttribute("success", (Object)("Claim " + claim.getClaimReference() + " created. Status: " + status));
            return "redirect:/claims/" + claim.getId();
        }
        catch (Exception e) {
            model.addAttribute("error", (Object)e.getMessage());
            model.addAttribute("policies", this.policyService.findActivePoliciesForUser(user));
            model.addAttribute("claimTypes", (Object)InsuranceClaim.ClaimType.values());
            return "claim/new";
        }
    }

    @GetMapping(value={"/{id}"})
    public String claimDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal, Model model) {
        boolean isReviewerOrAdmin;
        User user = principal.getUser();
        InsuranceClaim claim = this.claimService.findById(id);
        boolean isOwner = claim.getSubmittedBy().getId().equals(user.getId());
        boolean bl = isReviewerOrAdmin = user.getRoles().contains("REVIEWER") || user.getRoles().contains("ADMIN");
        if (!isOwner && !isReviewerOrAdmin) {
            return "redirect:/access-denied";
        }
        List<ClaimDocument> documents = this.documentService.findActiveDocuments(claim);
        List<ClaimAuditEvent> auditEvents = this.auditService.getClaimHistory(claim);
        model.addAttribute("claim", (Object)claim);
        model.addAttribute("documents", documents);
        model.addAttribute("auditEvents", auditEvents);
        model.addAttribute("isOwner", (Object)isOwner);
        model.addAttribute("isReviewerOrAdmin", (Object)isReviewerOrAdmin);
        return "claim/detail";
    }

    @PostMapping(value={"/{id}/submit"})
    public String submitDraft(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal, RedirectAttributes redirectAttributes) {
        User user = principal.getUser();
        try {
            InsuranceClaim claim = this.claimService.submit(id, user);
            redirectAttributes.addFlashAttribute("success", (Object)("Claim submitted. Status: " + claim.getStatus().name()));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/claims/" + id;
    }

    private String getPrimaryRole(User user) {
        if (user.getRoles().contains("ADMIN")) {
            return "ADMIN";
        }
        if (user.getRoles().contains("REVIEWER")) {
            return "REVIEWER";
        }
        return "POLICYHOLDER";
    }

    @Generated
    public ClaimController(ClaimService claimService, PolicyService policyService, AuditService auditService, DocumentService documentService) {
        this.claimService = claimService;
        this.policyService = policyService;
        this.auditService = auditService;
        this.documentService = documentService;
    }
}
