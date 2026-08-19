package com.analytics.platform.controller;

import com.analytics.platform.dto.response.DepartmentResponse;
import com.analytics.platform.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        List<DepartmentResponse> response = departmentService.getAllDepartments();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable String id) {
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        DepartmentResponse response = departmentService.createDepartment(name, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean active) {
        DepartmentResponse response = departmentService.updateDepartment(id, name, description, active);
        return ResponseEntity.ok(response);
    }
}
