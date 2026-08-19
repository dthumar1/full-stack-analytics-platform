package com.analytics.platform.dto.response;

import com.analytics.platform.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponse {
    
    private String id;
    private String alertType;
    private String severity;
    private String message;
    private String employeeId;
    private String employeeName;
    private String departmentId;
    private String departmentName;
    private String operationalRecordId;
    private String workflowId;
    private Boolean resolved;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    
    public static AlertResponse from(Alert alert) {
        return AlertResponse.builder()
            .id(alert.getId())
            .alertType(alert.getAlertType() != null ? alert.getAlertType().name() : null)
            .severity(alert.getSeverity() != null ? alert.getSeverity().name() : null)
            .message(alert.getMessage())
            .employeeId(alert.getEmployee() != null ? alert.getEmployee().getId() : null)
            .employeeName(alert.getEmployee() != null ? 
                alert.getEmployee().getFirstName() + " " + alert.getEmployee().getLastName() : null)
            .departmentId(alert.getDepartment() != null ? alert.getDepartment().getId() : null)
            .departmentName(alert.getDepartment() != null ? alert.getDepartment().getName() : null)
            .operationalRecordId(alert.getOperationalRecord() != null ? alert.getOperationalRecord().getId() : null)
            .workflowId(alert.getWorkflow() != null ? alert.getWorkflow().getId() : null)
            .resolved(alert.getResolved())
            .resolvedBy(alert.getResolvedBy() != null ? 
                alert.getResolvedBy().getFirstName() + " " + alert.getResolvedBy().getLastName() : null)
            .resolvedAt(alert.getResolvedAt())
            .createdAt(alert.getCreatedAt())
            .build();
    }
}
