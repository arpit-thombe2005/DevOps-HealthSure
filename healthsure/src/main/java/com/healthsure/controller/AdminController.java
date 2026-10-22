/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
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

import com.healthsure.dto.RegistrationRequest;
import com.healthsure.entity.InsurancePolicy;
import com.healthsure.entity.User;
import com.healthsure.security.CustomUserPrincipal;
import com.healthsure.service.ClaimService;
import com.healthsure.service.PolicyService;
import com.healthsure.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value={"/admin"})
@PreAuthorize(value="hasRole('ADMIN')")
public class AdminController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;
    private final PolicyService policyService;
    private final ClaimService claimService;

    @GetMapping(value={"/dashboard"})
    public String dashboard(@AuthenticationPrincipal CustomUserPrincipal principal, Model model) {
        User user = principal.getUser();
        model.addAttribute("stats", (Object)this.claimService.getDashboardStats(user, "ADMIN"));
        model.addAttribute("recentClaims", this.claimService.findRecent(10));
        model.addAttribute("totalUsers", (Object)this.userService.countAll());
        model.addAttribute("activePolicies", (Object)this.policyService.countActive());
        model.addAttribute("totalPolicies", (Object)this.policyService.countAll());
        return "admin/dashboard";
    }

    @GetMapping(value={"/users"})
    public String listUsers(Model model) {
        model.addAttribute("users", this.userService.findAll());
        model.addAttribute("newUser", (Object)new RegistrationRequest());
        return "admin/users";
    }

    @PostMapping(value={"/users/create"})
    public String createUser(@ModelAttribute RegistrationRequest request, @RequestParam(defaultValue="POLICYHOLDER") String role, RedirectAttributes redirectAttributes) {
        try {
            this.userService.registerWithRole(request, role.toUpperCase());
            redirectAttributes.addFlashAttribute("success", (Object)("User created: " + request.getUsername()));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping(value={"/users/{id}/toggle"})
    public String toggleUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.userService.toggleUserEnabled(id);
            redirectAttributes.addFlashAttribute("success", (Object)"User status updated");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping(value={"/policies"})
    public String listPolicies(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="15") int size, Model model) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"createdAt"}).descending());
        model.addAttribute("policies", this.policyService.findAll((Pageable)pageable));
        model.addAttribute("users", this.userService.findAll());
        model.addAttribute("policyTypes", (Object)InsurancePolicy.PolicyType.values());
        return "admin/policies";
    }

    @PostMapping(value={"/policies/create"})
    public String createPolicy(@RequestParam Long userId, @RequestParam String policyType, @RequestParam BigDecimal coverageAmount, @RequestParam String startDate, @RequestParam String expiryDate, @RequestParam(required=false) String description, RedirectAttributes redirectAttributes) {
        try {
            User user = this.userService.findById(userId);
            this.policyService.createPolicy(user, InsurancePolicy.PolicyType.valueOf(policyType), coverageAmount, LocalDate.parse(startDate), LocalDate.parse(expiryDate), description);
            redirectAttributes.addFlashAttribute("success", (Object)"Policy created successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/admin/policies";
    }

    @PostMapping(value={"/policies/{id}/cancel"})
    public String cancelPolicy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.policyService.updateStatus(id, InsurancePolicy.PolicyStatus.CANCELLED);
            redirectAttributes.addFlashAttribute("success", (Object)"Policy cancelled");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/admin/policies";
    }

    @GetMapping(value={"/claims"})
    public String listAllClaims(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="15") int size, Model model) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"createdAt"}).descending());
        model.addAttribute("claims", this.claimService.findAll((Pageable)pageable));
        return "admin/claims";
    }

    @Generated
    public AdminController(UserService userService, PolicyService policyService, ClaimService claimService) {
        this.userService = userService;
        this.policyService = policyService;
        this.claimService = claimService;
    }
}
