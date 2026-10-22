/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.security.crypto.password.PasswordEncoder
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.healthsure.service;

import com.healthsure.dto.RegistrationRequest;
import com.healthsure.entity.User;
import com.healthsure.exception.ResourceNotFoundException;
import com.healthsure.repository.UserRepository;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegistrationRequest request) {
        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        String role = "POLICYHOLDER";
        User user = User.builder().username(request.getUsername()).passwordHash(this.passwordEncoder.encode((CharSequence)request.getPassword())).email(request.getEmail()).firstName(request.getFirstName()).lastName(request.getLastName()).phone(request.getPhone()).address(request.getAddress()).enabled(true).roles(Set.of(role)).build();
        User saved = (User)this.userRepository.save(user);
        log.info("New user registered: {}", (Object)saved.getUsername());
        return saved;
    }

    @Transactional
    public User registerWithRole(RegistrationRequest request, String role) {
        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder().username(request.getUsername()).passwordHash(this.passwordEncoder.encode((CharSequence)request.getPassword())).email(request.getEmail()).firstName(request.getFirstName()).lastName(request.getLastName()).phone(request.getPhone()).address(request.getAddress()).enabled(true).roles(Set.of(role)).build();
        return (User)this.userRepository.save(user);
    }

    @Transactional(readOnly=true)
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Transactional(readOnly=true)
    public User findById(Long id) {
        return (User)this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly=true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<User> findReviewers() {
        return this.userRepository.findByRole("REVIEWER");
    }

    @Transactional
    public User updateProfile(Long userId, String firstName, String lastName, String phone, String address) {
        User user = this.findById(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);
        return (User)this.userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = this.findById(userId);
        if (!this.passwordEncoder.matches((CharSequence)currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPasswordHash(this.passwordEncoder.encode((CharSequence)newPassword));
        this.userRepository.save(user);
        log.info("Password changed for user id={}", (Object)userId);
    }

    @Transactional
    public void toggleUserEnabled(Long userId) {
        User user = this.findById(userId);
        user.setEnabled(!user.isEnabled());
        this.userRepository.save(user);
        log.info("User {} enabled={}", (Object)user.getUsername(), (Object)user.isEnabled());
    }

    @Transactional(readOnly=true)
    public long countAll() {
        return this.userRepository.count();
    }

    @Generated
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
}
