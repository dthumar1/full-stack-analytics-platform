package com.analytics.platform.controller;

import com.analytics.platform.dto.request.LoginRequest;
import com.analytics.platform.dto.response.LoginResponse;
import com.analytics.platform.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@analytics.local");
        loginRequest.setPassword("Admin123!");

        loginResponse = new LoginResponse();
        loginResponse.setToken("jwt-token");
        loginResponse.setType("Bearer");
        loginResponse.setExpiration(System.currentTimeMillis() + 3600000);
        loginResponse.setUser(new com.analytics.platform.dto.response.UserResponse(
            UUID.randomUUID(), "Admin", "User", "admin@analytics.local", "ADMIN", true, "2024-01-01T00:00:00"
        ));
    }

    @Test
    void login_Success() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("admin@analytics.local"));
    }

    @Test
    void login_InvalidRequest_ThrowsException() throws Exception {
        loginRequest.setEmail(""); // Invalid email

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
