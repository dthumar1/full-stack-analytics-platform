package com.analytics.platform.controller;

import com.analytics.platform.dto.request.EmployeeRequest;
import com.analytics.platform.dto.response.EmployeeResponse;
import com.analytics.platform.service.EmployeeService;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<EmployeeResponse> response = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String shift,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<EmployeeResponse> response = employeeService.searchEmployees(
            search, departmentId, status, location, shift, pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String id, 
            @Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
