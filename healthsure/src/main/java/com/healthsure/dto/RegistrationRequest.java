/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.constraints.Email
 *  jakarta.validation.constraints.NotBlank
 *  jakarta.validation.constraints.Pattern
 *  jakarta.validation.constraints.Size
 *  lombok.Generated
 */
package com.healthsure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class RegistrationRequest {
    @NotBlank(message="Username is required")
    @Size(min=3, max=50, message="Username must be 3-50 characters")
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Username can only contain letters, numbers, and underscores")
    private @NotBlank(message="Username is required") @Size(min=3, max=50, message="Username must be 3-50 characters") @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Username can only contain letters, numbers, and underscores") String username;
    @NotBlank(message="Password is required")
    @Size(min=8, max=100, message="Password must be at least 8 characters")
    private @NotBlank(message="Password is required") @Size(min=8, max=100, message="Password must be at least 8 characters") String password;
    @NotBlank(message="Please confirm your password")
    private @NotBlank(message="Please confirm your password") String confirmPassword;
    @NotBlank(message="Email is required")
    @Email(message="Please enter a valid email address")
    @Size(max=100)
    private @NotBlank(message="Email is required") @Email(message="Please enter a valid email address") @Size(max=100) String email;
    @NotBlank(message="First name is required")
    @Size(max=50)
    private @NotBlank(message="First name is required") @Size(max=50) String firstName;
    @NotBlank(message="Last name is required")
    @Size(max=50)
    private @NotBlank(message="Last name is required") @Size(max=50) String lastName;
    @Pattern(regexp="^[+]?[0-9\\-\\s()]{7,20}$", message="Invalid phone number format")
    private @Pattern(regexp="^[+]?[0-9\\-\\s()]{7,20}$", message="Invalid phone number format") String phone;
    @Size(max=255)
    private @Size(max=255) String address;
    private String role;

    @Generated
    public static RegistrationRequestBuilder builder() {
        return new RegistrationRequestBuilder();
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public String getConfirmPassword() {
        return this.confirmPassword;
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
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getRole() {
        return this.role;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setRole(String role) {
        this.role = role;
    }

    @Generated
    public RegistrationRequest() {
    }

    @Generated
    public RegistrationRequest(String username, String password, String confirmPassword, String email, String firstName, String lastName, String phone, String address, String role) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    @Generated
    public static class RegistrationRequestBuilder {
        @Generated
        private String username;
        @Generated
        private String password;
        @Generated
        private String confirmPassword;
        @Generated
        private String email;
        @Generated
        private String firstName;
        @Generated
        private String lastName;
        @Generated
        private String phone;
        @Generated
        private String address;
        @Generated
        private String role;

        @Generated
        RegistrationRequestBuilder() {
        }

        @Generated
        public RegistrationRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder address(String address) {
            this.address = address;
            return this;
        }

        @Generated
        public RegistrationRequestBuilder role(String role) {
            this.role = role;
            return this;
        }

        @Generated
        public RegistrationRequest build() {
            return new RegistrationRequest(this.username, this.password, this.confirmPassword, this.email, this.firstName, this.lastName, this.phone, this.address, this.role);
        }

        @Generated
        public String toString() {
            return "RegistrationRequest.RegistrationRequestBuilder(username=" + this.username + ", password=" + this.password + ", confirmPassword=" + this.confirmPassword + ", email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", phone=" + this.phone + ", address=" + this.address + ", role=" + this.role + ")";
        }
    }
}
