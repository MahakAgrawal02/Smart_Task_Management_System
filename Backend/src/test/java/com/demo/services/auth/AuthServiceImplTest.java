package com.demo.services.auth;

import com.demo.dao.SignupRequest;
import com.demo.dao.UserDto;
import com.demo.entities.User;
import com.demo.enums.UserRole;
import com.demo.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthServiceImpl class using Mockito and JUnit 5.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    // Mocked dependencies
    @Mock private UserRepository userRepository;

    // Class under test with mocks injected
    @InjectMocks
    private AuthServiceImpl authService;

    // Common test data
    private SignupRequest signupRequest;
    private User user;

    /**
     * Setup test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Create a mock signup request
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setName("Test User");
        signupRequest.setPassword("password123");

        // Create a mock user entity
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setUserRole(UserRole.EMPLOYEE);
    }

    /**
     * Test: Admin account is created if it doesn't already exist.
     */
    @Test
    void testCreateAnAdminAccount_WhenAdminDoesNotExist() {
        when(userRepository.findByUserRole(UserRole.ADMIN)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.createAnAdminAccount();

        verify(userRepository).save(any(User.class));
    }

    /**
     * Test: Admin account is not created if one already exists.
     */
    @Test
    void testCreateAnAdminAccount_WhenAdminExists() {
        User admin = new User();
        admin.setUserRole(UserRole.ADMIN);

        when(userRepository.findByUserRole(UserRole.ADMIN)).thenReturn(Optional.of(admin));

        authService.createAnAdminAccount();

        verify(userRepository, never()).save(any());
    }

    /**
     * Test: A new user is successfully signed up.
     */
    @Test
    void testSignupUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = authService.signupUser(signupRequest);

        assertNotNull(userDto);
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("Test User", userDto.getName());
        assertEquals(1L, userDto.getId());
    }

    /**
     * Test: Returns true if a user exists with the given email.
     */
    @Test
    void testHasUserWithEmail_WhenExists() {
        when(userRepository.findFirstByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertTrue(authService.hasUserWithEmail("test@example.com"));
    }

    /**
     * Test: Returns false if no user exists with the given email.
     */
    @Test
    void testHasUserWithEmail_WhenNotExists() {
        when(userRepository.findFirstByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertFalse(authService.hasUserWithEmail("notfound@example.com"));
    }
}
