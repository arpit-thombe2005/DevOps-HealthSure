package com.healthsure.service;

import com.healthsure.dto.RegistrationRequest;
import com.healthsure.entity.User;
import com.healthsure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = RegistrationRequest.builder()
                .username("newuser")
                .email("newuser@demo.com")
                .password("Password@123")
                .confirmPassword("Password@123")
                .firstName("Alice")
                .lastName("Wonderland")
                .phone("+91-9999999999")
                .address("Wonderland Lane 42")
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new policyholder with encrypted password")
    void testRegisterSuccess() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@demo.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedHashMock");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(10L);
            return u;
        });

        User user = userService.register(validRequest);

        assertNotNull(user);
        assertEquals("newuser", user.getUsername());
        assertEquals("$2a$10$encodedHashMock", user.getPasswordHash());
        assertTrue(user.getRoles().contains("POLICYHOLDER"));
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should throw exception if username already exists")
    void testRegisterDuplicateUsername() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(validRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if password and confirmation do not match")
    void testRegisterPasswordMismatch() {
        validRequest.setConfirmPassword("DifferentPassword");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@demo.com")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.register(validRequest));
    }

    @Test
    @DisplayName("Should change password successfully when current password matches")
    void testChangePasswordSuccess() {
        User user = User.builder()
                .id(1L)
                .username("john.doe")
                .passwordHash("$2a$10$oldHash")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("CurrentPassword@123", "$2a$10$oldHash")).thenReturn(true);
        when(passwordEncoder.encode("NewPassword@123")).thenReturn("$2a$10$newHash");

        userService.changePassword(1L, "CurrentPassword@123", "NewPassword@123");

        assertEquals("$2a$10$newHash", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should fail password change when current password is invalid")
    void testChangePasswordInvalidCurrent() {
        User user = User.builder()
                .id(1L)
                .username("john.doe")
                .passwordHash("$2a$10$oldHash")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPassword", "$2a$10$oldHash")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                userService.changePassword(1L, "WrongPassword", "NewPassword@123")
        );
    }
}
