package com.analytics.platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryResponse {
    
    private Long totalEmployees;
    private Long activeEmployees;
    private Long totalRecords;
    private Long unitsProcessed;
    private BigDecimal averageProductivity;
    private Long totalErrors;
    private BigDecimal averageErrorRate;
    private Long openWorkflows;
    private Long criticalAlerts;
    private Long automationRuns;
}
