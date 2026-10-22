/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.security.core.authority.SimpleGrantedAuthority
 *  org.springframework.security.core.userdetails.UserDetails
 *  org.springframework.security.core.userdetails.UserDetailsService
 *  org.springframework.security.core.userdetails.UsernameNotFoundException
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.healthsure.security;

import com.healthsure.entity.User;
import com.healthsure.repository.UserRepository;
import com.healthsure.security.CustomUserPrincipal;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService
implements UserDetailsService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("Empty username or email");
        }
        String identifier = username.trim();
        User user = this.userRepository.findByUsernameOrEmailIgnoreCase(identifier).orElseThrow(() -> {
            log.warn("Login attempt for unknown user identifier: '{}'", (Object)identifier);
            return new UsernameNotFoundException("User not found: " + identifier);
        });
        Collection authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
        return new CustomUserPrincipal(user, authorities);
    }

    @Generated
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
