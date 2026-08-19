package com.analytics.platform.service;

import com.analytics.platform.dto.request.LoginRequest;
import com.analytics.platform.dto.response.LoginResponse;
import com.analytics.platform.entity.User;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.UserRepository;
import com.analytics.platform.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$encodedPassword");
        testUser.setRole("ANALYST");
        testUser.setEnabled(true);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
    }

    @Test
    void login_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.generateToken(any(), any())).thenReturn("jwt-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Test", response.getUser().getFirstName());
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail("test@example.com");
        verify(jwtTokenProvider).generateToken(any(), any());
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(authenticationManager).authenticate(any());
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequest));
    }
}
