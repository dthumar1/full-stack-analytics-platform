package com.analytics.platform.service;

import com.analytics.platform.dto.request.EmployeeRequest;
import com.analytics.platform.dto.response.EmployeeResponse;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.Employee.EmployeeStatus;
import com.analytics.platform.entity.Employee.Shift;
import com.analytics.platform.exception.DuplicateResourceException;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.DepartmentRepository;
import com.analytics.platform.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee", "employeeCode", request.getEmployeeCode());
        }
        
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", request.getEmail());
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Employee employee = Employee.builder()
            .employeeCode(request.getEmployeeCode())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .department(department)
            .jobTitle(request.getJobTitle())
            .location(request.getLocation())
            .shift(request.getShift())
            .status(request.getStatus())
            .hireDate(request.getHireDate() != null ? LocalDate.parse(request.getHireDate()) : null)
            .build();
        
        employee = employeeRepository.save(employee);
        log.info("Created employee: {}", request.getEmployeeCode());
        
        return EmployeeResponse.from(employee);
    }
    
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
            .map(EmployeeResponse::from);
    }
    
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return EmployeeResponse.from(employee);
    }
    
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> searchEmployees(String search, String departmentId, 
                                                 String status, String location, String shift, 
                                                 Pageable pageable) {
        Department department = null;
        if (departmentId != null) {
            department = departmentRepository.findById(departmentId).orElse(null);
        }
        
        EmployeeStatus employeeStatus = status != null ? EmployeeStatus.valueOf(status) : null;
        Shift employeeShift = shift != null ? Shift.valueOf(shift) : null;
        
        return employeeRepository.searchEmployees(search, department, employeeStatus, 
            location, employeeShift, pageable)
            .map(EmployeeResponse::from);
    }
    
    @Transactional
    public EmployeeResponse updateEmployee(String id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        if (!employee.getEmployeeCode().equals(request.getEmployeeCode()) && 
            employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee", "employeeCode", request.getEmployeeCode());
        }
        
        if (!employee.getEmail().equals(request.getEmail()) && 
            employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", request.getEmail());
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);
        employee.setJobTitle(request.getJobTitle());
        employee.setLocation(request.getLocation());
        employee.setShift(request.getShift());
        employee.setStatus(request.getStatus());
        employee.setHireDate(request.getHireDate() != null ? LocalDate.parse(request.getHireDate()) : null);
        
        employee = employeeRepository.save(employee);
        log.info("Updated employee: {}", request.getEmployeeCode());
        
        return EmployeeResponse.from(employee);
    }
    
    @Transactional
    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        if (!employee.getOperationalRecords().isEmpty()) {
            throw new RuntimeException("Cannot delete employee with operational records");
        }
        
        employeeRepository.delete(employee);
        log.info("Deleted employee: {}", employee.getEmployeeCode());
    }
    
    @Transactional(readOnly = true)
    public long getActiveEmployeeCount() {
        return employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
    }
}
