package com.analytics.platform.controller;

import com.analytics.platform.dto.request.LoginRequest;
import com.analytics.platform.dto.response.LoginResponse;
import com.analytics.platform.dto.response.UserResponse;
import com.analytics.platform.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestParam String email,
                                               @RequestParam String password,
                                               @RequestParam String firstName,
                                               @RequestParam String lastName,
                                               @RequestParam(defaultValue = "ANALYST") String role) {
        User.Role userRole = User.Role.valueOf(role.toUpperCase());
        UserResponse response = authService.registerUser(email, password, firstName, lastName, userRole);
        return ResponseEntity.ok(response);
    }
}
