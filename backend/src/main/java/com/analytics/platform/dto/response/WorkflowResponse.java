package com.analytics.platform.dto.response;

import com.analytics.platform.entity.Workflow;
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
public class WorkflowResponse {
    
    private String id;
    private String title;
    private String description;
    private String departmentId;
    private String departmentName;
    private String assignedEmployeeId;
    private String assignedEmployeeName;
    private String assignedUserId;
    private String assignedUserName;
    private String priority;
    private String status;
    private LocalDate dueDate;
    private LocalDate completedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static WorkflowResponse from(Workflow workflow) {
        return WorkflowResponse.builder()
            .id(workflow.getId())
            .title(workflow.getTitle())
            .description(workflow.getDescription())
            .departmentId(workflow.getDepartment() != null ? workflow.getDepartment().getId() : null)
            .departmentName(workflow.getDepartment() != null ? workflow.getDepartment().getName() : null)
            .assignedEmployeeId(workflow.getAssignedEmployee() != null ? workflow.getAssignedEmployee().getId() : null)
            .assignedEmployeeName(workflow.getAssignedEmployee() != null ? 
                workflow.getAssignedEmployee().getFirstName() + " " + workflow.getAssignedEmployee().getLastName() : null)
            .assignedUserId(workflow.getAssignedUser() != null ? workflow.getAssignedUser().getId() : null)
            .assignedUserName(workflow.getAssignedUser() != null ? 
                workflow.getAssignedUser().getFirstName() + " " + workflow.getAssignedUser().getLastName() : null)
            .priority(workflow.getPriority() != null ? workflow.getPriority().name() : null)
            .status(workflow.getStatus() != null ? workflow.getStatus().name() : null)
            .dueDate(workflow.getDueDate())
            .completedDate(workflow.getCompletedDate())
            .createdAt(workflow.getCreatedAt())
            .updatedAt(workflow.getUpdatedAt())
            .build();
    }
}
