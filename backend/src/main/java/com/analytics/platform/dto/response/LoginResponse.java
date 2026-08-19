package com.analytics.platform.dto.response;

import com.analytics.platform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    
    private String token;
    private final String type = "Bearer";
    private Long expiration;
    private UserResponse user;
    
    public static LoginResponse from(String token, Long expiration, User user) {
        return LoginResponse.builder()
            .token(token)
            .expiration(expiration)
            .user(UserResponse.from(user))
            .build();
    }
}
