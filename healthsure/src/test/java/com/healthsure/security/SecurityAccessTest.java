package com.healthsure.security;

import com.healthsure.entity.User;
import com.healthsure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityAccessTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User adminUser;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder(10);
        adminUser = User.builder()
                .id(1L)
                .username("admin")
                .passwordHash(encoder.encode("Admin@1234"))
                .email("admin@healthsure.demo")
                .roles(Set.of("ADMIN", "REVIEWER"))
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should successfully load user details with ROLE_ prefixes")
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByUsernameOrEmailIgnoreCase("admin")).thenReturn(Optional.of(adminUser));

        UserDetails details = userDetailsService.loadUserByUsername("admin");

        assertNotNull(details);
        assertEquals("admin", details.getUsername());
        assertTrue(encoder.matches("Admin@1234", details.getPassword()));
        assertTrue(details.isEnabled());

        Set<String> authorities = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertTrue(authorities.contains("ROLE_REVIEWER"));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException for non-existent user")
    void testLoadUserNotFound() {
        when(userRepository.findByUsernameOrEmailIgnoreCase("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknown")
        );
    }

    @Test
    @DisplayName("BCrypt password encoder correctly verifies passwords")
    void testBCryptEncoding() {
        String rawPassword = "SecurePassword@2026";
        String encoded = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encoded));
        assertFalse(encoder.matches("WrongPassword", encoded));
    }
}
