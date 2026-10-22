/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.security.core.Authentication
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.GetMapping
 */
package com.healthsure.controller;

import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value={"/"})
    public String index(Principal principal) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping(value={"/access-denied"})
    public String accessDenied() {
        return "error/access-denied";
    }

    @GetMapping(value={"/dashboard"})
    public String dashboard(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }
        boolean isReviewer = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_REVIEWER"));
        if (isReviewer) {
            return "redirect:/reviewer/dashboard";
        }
        return "redirect:/claims";
    }
}
