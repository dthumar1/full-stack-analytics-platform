package com.analytics.platform.controller;

import com.analytics.platform.dto.request.WorkflowRequest;
import com.analytics.platform.dto.request.WorkflowStatusUpdateRequest;
import com.analytics.platform.dto.response.WorkflowResponse;
import com.analytics.platform.service.WorkflowService;
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
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class WorkflowController {
    
    private final WorkflowService workflowService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<WorkflowResponse> createWorkflow(@Valid @RequestBody WorkflowRequest request) {
        WorkflowResponse response = workflowService.createWorkflow(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<WorkflowResponse>> getAllWorkflows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<WorkflowResponse> response = workflowService.getAllWorkflows(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<WorkflowResponse>> filterWorkflows(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String assignedUserId,
            @RequestParam(required = false) String dueDateBefore,
            @RequestParam(required = false) String dueDateAfter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<WorkflowResponse> response = workflowService.filterWorkflows(
            departmentId, status, priority, assignedUserId, dueDateBefore, dueDateAfter, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<WorkflowResponse> getWorkflowById(@PathVariable String id) {
        WorkflowResponse response = workflowService.getWorkflowById(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<WorkflowResponse> updateWorkflow(
            @PathVariable String id, 
            @Valid @RequestBody WorkflowRequest request) {
        WorkflowResponse response = workflowService.updateWorkflow(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<WorkflowResponse> updateWorkflowStatus(
            @PathVariable String id, 
            @Valid @RequestBody WorkflowStatusUpdateRequest request) {
        WorkflowResponse response = workflowService.updateWorkflowStatus(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable String id) {
        workflowService.deleteWorkflow(id);
        return ResponseEntity.noContent().build();
    }
}
