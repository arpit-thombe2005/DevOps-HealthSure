/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.security.core.GrantedAuthority
 *  org.springframework.security.core.authority.SimpleGrantedAuthority
 *  org.springframework.security.core.userdetails.UserDetails
 */
package com.healthsure.security;

import com.healthsure.entity.User;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserPrincipal
implements UserDetails {
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public CustomUserPrincipal(User user) {
        this.user = user;
        this.authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority((String)(r.startsWith("ROLE_") ? r : "ROLE_" + r))).collect(Collectors.toSet());
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public String getFullName() {
        String ln;
        if (this.user == null) {
            return "User";
        }
        String fn = this.user.getFirstName() != null ? this.user.getFirstName().trim() : "";
        String full = (fn + " " + (ln = this.user.getLastName() != null ? this.user.getLastName().trim() : "")).trim();
        return full.isEmpty() ? (this.user.getUsername() != null ? this.user.getUsername() : "User") : full;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getPassword() {
        return this.user.getPasswordHash();
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    @Generated
    public User getUser() {
        return this.user;
    }
}
