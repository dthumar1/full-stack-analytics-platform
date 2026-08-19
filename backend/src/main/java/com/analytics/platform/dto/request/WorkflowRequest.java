package com.analytics.platform.dto.request;

import com.analytics.platform.entity.Workflow.Priority;
import com.analytics.platform.entity.Workflow.WorkflowStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Department ID is required")
    private String departmentId;
    
    private String assignedEmployeeId;
    
    private String assignedUserId;
    
    @NotNull(message = "Priority is required")
    private Priority priority;
    
    private WorkflowStatus status;
    
    private LocalDate dueDate;
}
