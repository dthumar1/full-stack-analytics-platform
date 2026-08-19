package com.analytics.platform.dto.response;

import com.analytics.platform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Boolean enabled;
    private LocalDateTime createdAt;
    
    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .role(user.getRole().name())
            .enabled(user.getEnabled())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
