package com.analytics.platform.controller;

import com.analytics.platform.dto.response.AlertResponse;
import com.analytics.platform.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {
    
    private final AlertService alertService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<AlertResponse>> getAllAlerts(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) Boolean resolved,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<AlertResponse> response = alertService.filterAlerts(
            employeeId, departmentId, type, severity, resolved, startDate, endDate, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable String id) {
        AlertResponse response = alertService.getAlertById(id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<AlertResponse> resolveAlert(
            @PathVariable String id,
            @RequestParam String resolvedByUserId) {
        AlertResponse response = alertService.resolveAlert(id, resolvedByUserId);
        return ResponseEntity.ok(response);
    }
}
