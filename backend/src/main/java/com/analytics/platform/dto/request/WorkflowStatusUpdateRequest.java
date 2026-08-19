package com.analytics.platform.dto.request;

import com.analytics.platform.entity.Workflow.WorkflowStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStatusUpdateRequest {
    
    @NotNull(message = "Status is required")
    private WorkflowStatus status;
    
    private String assignedUserId;
}
