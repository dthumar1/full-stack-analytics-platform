package com.analytics.platform.controller;

import com.analytics.platform.dto.request.OperationalRecordRequest;
import com.analytics.platform.dto.response.OperationalRecordResponse;
import com.analytics.platform.service.OperationalRecordService;
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
@RequestMapping("/operational-records")
@RequiredArgsConstructor
public class OperationalRecordController {
    
    private final OperationalRecordService recordService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<OperationalRecordResponse> createRecord(@Valid @RequestBody OperationalRecordRequest request) {
        OperationalRecordResponse response = recordService.createRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<OperationalRecordResponse>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "workDate,desc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<OperationalRecordResponse> response = recordService.getAllRecords(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<Page<OperationalRecordResponse>> filterRecords(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String minProductivity,
            @RequestParam(required = false) String maxProductivity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "workDate,desc") String sort) {
        
        Sort.Direction direction = sort.contains(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        Page<OperationalRecordResponse> response = recordService.filterRecords(
            employeeId, departmentId, location, status, startDate, endDate,
            minProductivity, maxProductivity, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
    public ResponseEntity<OperationalRecordResponse> getRecordById(@PathVariable String id) {
        OperationalRecordResponse response = recordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<OperationalRecordResponse> updateRecord(
            @PathVariable String id, 
            @Valid @RequestBody OperationalRecordRequest request) {
        OperationalRecordResponse response = recordService.updateRecord(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable String id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
