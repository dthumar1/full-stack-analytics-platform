package com.analytics.platform.controller;

import com.analytics.platform.dto.request.AutomationRuleRequest;
import com.analytics.platform.dto.response.AutomationRunResponse;
import com.analytics.platform.dto.response.AutomationRuleResponse;
import com.analytics.platform.entity.AutomationRun.AutomationType;
import com.analytics.platform.service.AutomationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/automation")
@RequiredArgsConstructor
public class AutomationController {
    
    private final AutomationService automationService;
    
    @GetMapping("/runs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Page<AutomationRunResponse>> getAutomationRuns(
            @RequestParam(required = false) String automationType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startTime,desc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<AutomationRunResponse> response = automationService.getAutomationRuns(
            automationType, status, startDate, endDate, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/rules")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Page<AutomationRuleResponse>> getAutomationRules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<AutomationRuleResponse> response = automationService.getAutomationRules(pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/rules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutomationRuleResponse> updateAutomationRule(
            @PathVariable String id,
            @RequestParam Boolean enabled) {
        AutomationRuleResponse response = automationService.updateAutomationRule(id, enabled);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/run")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutomationRunResponse> triggerAutomation(
            @RequestParam AutomationType automationType) {
        AutomationRunResponse response = automationService.runAutomation(automationType);
        return ResponseEntity.ok(response);
    }
}
