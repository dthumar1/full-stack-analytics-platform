package com.analytics.platform.dto.response;

import com.analytics.platform.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    
    private String id;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static DepartmentResponse from(Department department) {
        return DepartmentResponse.builder()
            .id(department.getId())
            .name(department.getName())
            .description(department.getDescription())
            .active(department.getActive())
            .createdAt(department.getCreatedAt())
            .updatedAt(department.getUpdatedAt())
            .build();
    }
}
