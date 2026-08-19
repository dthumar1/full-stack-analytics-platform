package com.analytics.platform.service;

import com.analytics.platform.dto.response.DepartmentResponse;
import com.analytics.platform.entity.Department;
import com.analytics.platform.exception.DuplicateResourceException;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
            .map(DepartmentResponse::from)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(String id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return DepartmentResponse.from(department);
    }
    
    @Transactional
    public DepartmentResponse createDepartment(String name, String description) {
        if (departmentRepository.existsByName(name)) {
            throw new DuplicateResourceException("Department", "name", name);
        }
        
        Department department = Department.builder()
            .name(name)
            .description(description)
            .active(true)
            .build();
        
        department = departmentRepository.save(department);
        log.info("Created department: {}", name);
        
        return DepartmentResponse.from(department);
    }
    
    @Transactional
    public DepartmentResponse updateDepartment(String id, String name, String description, Boolean active) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        
        if (!department.getName().equals(name) && departmentRepository.existsByName(name)) {
            throw new DuplicateResourceException("Department", "name", name);
        }
        
        department.setName(name);
        department.setDescription(description);
        department.setActive(active != null ? active : department.getActive());
        
        department = departmentRepository.save(department);
        log.info("Updated department: {}", name);
        
        return DepartmentResponse.from(department);
    }
}
