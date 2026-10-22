/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  jakarta.validation.Valid
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.validation.BindingResult
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.healthsure.controller;

import com.healthsure.dto.RegistrationRequest;
import com.healthsure.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @GetMapping(value={"/login"})
    public String loginPage(Principal principal, @RequestParam(required=false) String error, @RequestParam(required=false) String logout, Model model) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        if (error != null) {
            model.addAttribute("loginError", (Object)"Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logoutSuccess", (Object)"You have been logged out successfully.");
        }
        return "auth/login";
    }

    @GetMapping(value={"/logout"})
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true";
    }

    @GetMapping(value={"/register"})
    public String registerPage(Principal principal, Model model) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("registrationRequest", (Object)new RegistrationRequest());
        return "auth/register";
    }

    @PostMapping(value={"/register"})
    public String register(@Valid @ModelAttribute(value="registrationRequest") RegistrationRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", (Object)"Passwords do not match");
            return "auth/register";
        }
        try {
            this.userService.register(request);
            redirectAttributes.addFlashAttribute("success", (Object)"Registration successful! Please log in with your credentials.");
            return "redirect:/login";
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("error", (Object)e.getMessage());
            return "auth/register";
        }
    }

    @Generated
    public AuthController(UserService userService) {
        this.userService = userService;
    }
}
