package com.analytics.platform.dto.request;

import com.analytics.platform.entity.Employee.EmployeeStatus;
import com.analytics.platform.entity.Employee.Shift;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotNull(message = "Department ID is required")
    private String departmentId;
    
    @NotBlank(message = "Job title is required")
    private String jobTitle;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Shift is required")
    private Shift shift;
    
    @NotNull(message = "Status is required")
    private EmployeeStatus status;
    
    private String hireDate;
}
