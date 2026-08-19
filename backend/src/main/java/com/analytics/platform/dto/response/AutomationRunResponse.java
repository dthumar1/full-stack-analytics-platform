package com.analytics.platform.dto.response;

import com.analytics.platform.entity.AutomationRun;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRunResponse {
    
    private String id;
    private String automationRuleId;
    private String automationRuleName;
    private String automationType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer recordsProcessed;
    private Integer alertsCreated;
    private String status;
    private String errorMessage;
    
    public static AutomationRunResponse from(AutomationRun run) {
        return AutomationRunResponse.builder()
            .id(run.getId())
            .automationRuleId(run.getAutomationRule() != null ? run.getAutomationRule().getId() : null)
            .automationRuleName(run.getAutomationRule() != null ? run.getAutomationRule().getName() : null)
            .automationType(run.getAutomationType() != null ? run.getAutomationType().name() : null)
            .startTime(run.getStartTime())
            .endTime(run.getEndTime())
            .recordsProcessed(run.getRecordsProcessed())
            .alertsCreated(run.getAlertsCreated())
            .status(run.getStatus() != null ? run.getStatus().name() : null)
            .errorMessage(run.getErrorMessage())
            .build();
    }
}
