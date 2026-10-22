/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.healthsure.repository;

import com.healthsure.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String var1);

    public Optional<User> findByEmail(String var1);

    @Query(value="SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:identifier) OR LOWER(u.email) = LOWER(:identifier)")
    public Optional<User> findByUsernameOrEmailIgnoreCase(@Param(value="identifier") String var1);

    public boolean existsByUsername(String var1);

    public boolean existsByEmail(String var1);

    @Query(value="SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    public List<User> findByRole(String var1);

    @Query(value="SELECT u FROM User u WHERE u.enabled = true AND u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    public List<User> searchByName(String var1);
}
