package com.analytics.platform.service;

import com.analytics.platform.dto.response.DashboardSummaryResponse;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final OperationalRecordService recordService;
    private final EmployeeService employeeService;
    private final WorkflowService workflowService;
    private final AlertService alertService;
    private final EmployeeRepository employeeRepository;
    
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummary(String startDate, String endDate, String departmentId) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeService.getActiveEmployeeCount();
        long totalRecords = recordService.getRecordCount(start, end, departmentId);
        Long unitsProcessed = recordService.getTotalUnitsProcessed(start, end, departmentId);
        BigDecimal averageProductivity = recordService.getAverageProductivity(start, end, departmentId);
        Long totalErrors = recordService.getTotalErrors(start, end, departmentId);
        BigDecimal averageErrorRate = recordService.getAverageErrorRate(start, end, departmentId);
        long openWorkflows = workflowService.getOpenWorkflowCount();
        long criticalAlerts = alertService.getCriticalAlertCount();
        
        return DashboardSummaryResponse.builder()
            .totalEmployees(totalEmployees)
            .activeEmployees(activeEmployees)
            .totalRecords(totalRecords)
            .unitsProcessed(unitsProcessed != null ? unitsProcessed : 0L)
            .averageProductivity(averageProductivity != null ? averageProductivity : BigDecimal.ZERO)
            .totalErrors(totalErrors != null ? totalErrors : 0L)
            .averageErrorRate(averageErrorRate != null ? averageErrorRate : BigDecimal.ZERO)
            .openWorkflows(openWorkflows)
            .criticalAlerts(criticalAlerts)
            .automationRuns(0L) // Will be implemented with automation service
            .build();
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getProductivityTrend(String startDate, String endDate, String departmentId, String groupBy) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        // Simplified implementation - in production, use proper SQL aggregation
        Map<String, Object> summary = new HashMap<>();
        summary.put("averageProductivity", recordService.getAverageProductivity(start, end, departmentId));
        summary.put("totalRecords", recordService.getRecordCount(start, end, departmentId));
        
        return Map.of(
            "metric", "productivity",
            "groupBy", groupBy != null ? groupBy : "day",
            "data", data,
            "summary", summary
        );
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getDepartmentPerformance(String startDate, String endDate) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        // Simplified implementation
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDepartments", 5); // Will be dynamic
        
        return Map.of(
            "metric", "departmentPerformance",
            "groupBy", "department",
            "data", data,
            "summary", summary
        );
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getErrorTrend(String startDate, String endDate, String departmentId) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("averageErrorRate", recordService.getAverageErrorRate(start, end, departmentId));
        summary.put("totalErrors", recordService.getTotalErrors(start, end, departmentId));
        
        return Map.of(
            "metric", "errorRate",
            "groupBy", "day",
            "data", data,
            "summary", summary
        );
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getTopPerformers(String startDate, String endDate, String departmentId, int limit) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        // Simplified implementation
        Map<String, Object> summary = new HashMap<>();
        summary.put("topPerformerCount", limit);
        
        return Map.of(
            "metric", "topPerformers",
            "groupBy", "employee",
            "data", data,
            "summary", summary
        );
    }
}
