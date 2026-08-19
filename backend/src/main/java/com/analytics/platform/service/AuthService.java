package com.analytics.platform.service;

import com.analytics.platform.dto.request.LoginRequest;
import com.analytics.platform.dto.response.LoginResponse;
import com.analytics.platform.dto.response.UserResponse;
import com.analytics.platform.entity.User;
import com.analytics.platform.repository.UserRepository;
import com.analytics.platform.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
            
            String token = tokenProvider.generateToken(authentication);
            Long expiration = tokenProvider.getExpirationTime();
            
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            log.info("User logged in successfully: {}", request.getEmail());
            
            return LoginResponse.from(token, expiration, user);
            
        } catch (AuthenticationException ex) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new RuntimeException("Invalid email or password");
        }
    }
    
    @Transactional
    public UserResponse registerUser(String email, String password, String firstName, 
                                    String lastName, User.Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        
        User user = User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(role)
            .enabled(true)
            .build();
        
        user = userRepository.save(user);
        log.info("New user registered: {}", email);
        
        return UserResponse.from(user);
    }
}
