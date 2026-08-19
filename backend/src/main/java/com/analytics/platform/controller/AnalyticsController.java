package com.analytics.platform.controller;

import com.analytics.platform.dto.response.DashboardSummaryResponse;
import com.analytics.platform.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String departmentId) {
        DashboardSummaryResponse response = analyticsService.getDashboardSummary(startDate, endDate, departmentId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/productivity-trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Map<String, Object>> getProductivityTrend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String departmentId,
            @RequestParam(defaultValue = "day") String groupBy) {
        Map<String, Object> response = analyticsService.getProductivityTrend(startDate, endDate, departmentId, groupBy);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/department-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Map<String, Object>> getDepartmentPerformance(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Object> response = analyticsService.getDepartmentPerformance(startDate, endDate);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/error-trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Map<String, Object>> getErrorTrend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String departmentId) {
        Map<String, Object> response = analyticsService.getErrorTrend(startDate, endDate, departmentId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/top-performers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Map<String, Object>> getTopPerformers(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String departmentId,
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = analyticsService.getTopPerformers(startDate, endDate, departmentId, limit);
        return ResponseEntity.ok(response);
    }
}
