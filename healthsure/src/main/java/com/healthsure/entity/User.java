/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.CascadeType
 *  jakarta.persistence.CollectionTable
 *  jakarta.persistence.Column
 *  jakarta.persistence.ElementCollection
 *  jakarta.persistence.Entity
 *  jakarta.persistence.FetchType
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.JoinColumn
 *  jakarta.persistence.OneToMany
 *  jakarta.persistence.Table
 *  jakarta.persistence.UniqueConstraint
 *  lombok.Generated
 */
package com.healthsure.entity;

import com.healthsure.entity.BaseEntity;
import com.healthsure.entity.InsurancePolicy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;

@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"username"}), @UniqueConstraint(columnNames={"email"})})
public class User
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=50)
    private String username;
    @Column(nullable=false)
    private String passwordHash;
    @Column(nullable=false, length=100)
    private String email;
    @Column(nullable=false, length=50)
    private String firstName;
    @Column(nullable=false, length=50)
    private String lastName;
    @Column(length=20)
    private String phone;
    @Column(length=255)
    private String address;
    @Column(nullable=false)
    private boolean enabled;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="user_roles", joinColumns={@JoinColumn(name="user_id")})
    @Column(name="role")
    private Set<String> roles;
    @OneToMany(mappedBy="policyholder", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    private Set<InsurancePolicy> policies;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Generated
    private static boolean $default$enabled() {
        return true;
    }

    @Generated
    private static Set<String> $default$roles() {
        return new HashSet<String>();
    }

    @Generated
    private static Set<InsurancePolicy> $default$policies() {
        return new HashSet<InsurancePolicy>();
    }

    @Generated
    public static UserBuilder builder() {
        return new UserBuilder();
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
    public String getPasswordHash() {
        return this.passwordHash;
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
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public Set<String> getRoles() {
        return this.roles;
    }

    @Generated
    public Set<InsurancePolicy> getPolicies() {
        return this.policies;
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
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Generated
    public void setPolicies(Set<InsurancePolicy> policies) {
        this.policies = policies;
    }

    @Generated
    public User() {
        this.enabled = User.$default$enabled();
        this.roles = User.$default$roles();
        this.policies = User.$default$policies();
    }

    @Generated
    public User(Long id, String username, String passwordHash, String email, String firstName, String lastName, String phone, String address, boolean enabled, Set<String> roles, Set<InsurancePolicy> policies) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.enabled = enabled;
        this.roles = roles;
        this.policies = policies;
    }

    @Generated
    public static class UserBuilder {
        @Generated
        private Long id;
        @Generated
        private String username;
        @Generated
        private String passwordHash;
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
        private boolean enabled$set;
        @Generated
        private boolean enabled$value;
        @Generated
        private boolean roles$set;
        @Generated
        private Set<String> roles$value;
        @Generated
        private boolean policies$set;
        @Generated
        private Set<InsurancePolicy> policies$value;

        @Generated
        UserBuilder() {
        }

        @Generated
        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        @Generated
        public UserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        @Generated
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        @Generated
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        @Generated
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        @Generated
        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Generated
        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        @Generated
        public UserBuilder enabled(boolean enabled) {
            this.enabled$value = enabled;
            this.enabled$set = true;
            return this;
        }

        @Generated
        public UserBuilder roles(Set<String> roles) {
            this.roles$value = roles;
            this.roles$set = true;
            return this;
        }

        @Generated
        public UserBuilder policies(Set<InsurancePolicy> policies) {
            this.policies$value = policies;
            this.policies$set = true;
            return this;
        }

        @Generated
        public User build() {
            boolean enabled$value = this.enabled$value;
            if (!this.enabled$set) {
                enabled$value = User.$default$enabled();
            }
            Set<String> roles$value = this.roles$value;
            if (!this.roles$set) {
                roles$value = User.$default$roles();
            }
            Set<InsurancePolicy> policies$value = this.policies$value;
            if (!this.policies$set) {
                policies$value = User.$default$policies();
            }
            return new User(this.id, this.username, this.passwordHash, this.email, this.firstName, this.lastName, this.phone, this.address, enabled$value, roles$value, policies$value);
        }

        @Generated
        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", username=" + this.username + ", passwordHash=" + this.passwordHash + ", email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", phone=" + this.phone + ", address=" + this.address + ", enabled$value=" + this.enabled$value + ", roles$value=" + String.valueOf(this.roles$value) + ", policies$value=" + String.valueOf(this.policies$value) + ")";
        }
    }
}
