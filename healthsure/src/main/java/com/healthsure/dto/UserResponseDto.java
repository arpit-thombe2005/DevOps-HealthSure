/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.healthsure.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Generated;

public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<String> roles;
    private boolean enabled;
    private LocalDateTime createdAt;

    @Generated
    public static UserResponseDtoBuilder builder() {
        return new UserResponseDtoBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getFirstName() {
        return this.firstName;
    }

    @Generated
    public String getLastName() {
        return this.lastName;
    }

    @Generated
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Generated
    public Set<String> getRoles() {
        return this.roles;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Generated
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Generated
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Generated
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public UserResponseDto() {
    }

    @Generated
    public UserResponseDto(Long id, String username, String email, String firstName, String lastName, String phoneNumber, Set<String> roles, boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    @Generated
    public static class UserResponseDtoBuilder {
        @Generated
        private Long id;
        @Generated
        private String username;
        @Generated
        private String email;
        @Generated
        private String firstName;
        @Generated
        private String lastName;
        @Generated
        private String phoneNumber;
        @Generated
        private Set<String> roles;
        @Generated
        private boolean enabled;
        @Generated
        private LocalDateTime createdAt;

        @Generated
        UserResponseDtoBuilder() {
        }

        @Generated
        public UserResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Generated
        public UserResponseDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public UserResponseDto build() {
            return new UserResponseDto(this.id, this.username, this.email, this.firstName, this.lastName, this.phoneNumber, this.roles, this.enabled, this.createdAt);
        }

        @Generated
        public String toString() {
            return "UserResponseDto.UserResponseDtoBuilder(id=" + this.id + ", username=" + this.username + ", email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", phoneNumber=" + this.phoneNumber + ", roles=" + String.valueOf(this.roles) + ", enabled=" + this.enabled + ", createdAt=" + String.valueOf(this.createdAt) + ")";
        }
    }
}
