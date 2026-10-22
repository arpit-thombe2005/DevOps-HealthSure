/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.security.core.annotation.AuthenticationPrincipal
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.healthsure.controller;

import com.healthsure.entity.User;
import com.healthsure.security.CustomUserPrincipal;
import com.healthsure.service.UserService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/profile"})
public class ProfileController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private final UserService userService;

    @GetMapping
    public String profile(@AuthenticationPrincipal CustomUserPrincipal principal, Model model) {
        model.addAttribute("user", (Object)principal.getUser());
        return "auth/profile";
    }

    @PostMapping(value={"/update"})
    public String updateProfile(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestParam String firstName, @RequestParam String lastName, @RequestParam(required=false) String phone, @RequestParam(required=false) String address, RedirectAttributes redirectAttributes) {
        User user = principal.getUser();
        try {
            this.userService.updateProfile(user.getId(), firstName, lastName, phone, address);
            redirectAttributes.addFlashAttribute("success", (Object)"Profile updated successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping(value={"/change-password"})
    public String changePassword(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", (Object)"New passwords do not match");
            return "redirect:/profile";
        }
        if (newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("error", (Object)"Password must be at least 8 characters");
            return "redirect:/profile";
        }
        try {
            this.userService.changePassword(principal.getUser().getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", (Object)"Password changed successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)e.getMessage());
        }
        return "redirect:/profile";
    }

    @Generated
    public ProfileController(UserService userService) {
        this.userService = userService;
    }
}
