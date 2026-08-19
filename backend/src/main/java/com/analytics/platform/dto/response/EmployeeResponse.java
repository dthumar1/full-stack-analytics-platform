package com.analytics.platform.dto.response;

import com.analytics.platform.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    
    private String id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentId;
    private String departmentName;
    private String jobTitle;
    private String location;
    private String shift;
    private String status;
    private LocalDate hireDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static EmployeeResponse from(Employee employee) {
        return EmployeeResponse.builder()
            .id(employee.getId())
            .employeeCode(employee.getEmployeeCode())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .email(employee.getEmail())
            .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
            .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
            .jobTitle(employee.getJobTitle())
            .location(employee.getLocation())
            .shift(employee.getShift() != null ? employee.getShift().name() : null)
            .status(employee.getStatus() != null ? employee.getStatus().name() : null)
            .hireDate(employee.getHireDate())
            .createdAt(employee.getCreatedAt())
            .updatedAt(employee.getUpdatedAt())
            .build();
    }
}
