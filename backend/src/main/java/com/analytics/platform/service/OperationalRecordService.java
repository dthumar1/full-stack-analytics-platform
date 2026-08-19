package com.analytics.platform.service;

import com.analytics.platform.dto.request.OperationalRecordRequest;
import com.analytics.platform.dto.response.OperationalRecordResponse;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.OperationalRecord;
import com.analytics.platform.entity.OperationalRecord.ProcessType;
import com.analytics.platform.entity.OperationalRecord.RecordStatus;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.DepartmentRepository;
import com.analytics.platform.repository.EmployeeRepository;
import com.analytics.platform.repository.OperationalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationalRecordService {
    
    private final OperationalRecordRepository recordRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    
    @Transactional
    public OperationalRecordResponse createRecord(OperationalRecordRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getEmployeeId()));
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        OperationalRecord record = OperationalRecord.builder()
            .employee(employee)
            .department(department)
            .processType(request.getProcessType())
            .unitsProcessed(request.getUnitsProcessed())
            .hoursWorked(new BigDecimal(request.getHoursWorked()))
            .errors(request.getErrors())
            .status(request.getStatus() != null ? request.getStatus() : RecordStatus.VALID)
            .location(request.getLocation())
            .workDate(request.getWorkDate())
            .build();
        
        record = recordRepository.save(record);
        log.info("Created operational record for employee: {}", employee.getEmployeeCode());
        
        return OperationalRecordResponse.from(record);
    }
    
    @Transactional(readOnly = true)
    public Page<OperationalRecordResponse> getAllRecords(Pageable pageable) {
        return recordRepository.findAll(pageable)
            .map(OperationalRecordResponse::from);
    }
    
    @Transactional(readOnly = true)
    public OperationalRecordResponse getRecordById(String id) {
        OperationalRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("OperationalRecord", "id", id));
        return OperationalRecordResponse.from(record);
    }
    
    @Transactional(readOnly = true)
    public Page<OperationalRecordResponse> filterRecords(String employeeId, String departmentId, 
                                                        String location, String status, 
                                                        String startDate, String endDate,
                                                        String minProductivity, String maxProductivity,
                                                        Pageable pageable) {
        RecordStatus recordStatus = status != null ? RecordStatus.valueOf(status) : null;
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        BigDecimal minProd = minProductivity != null ? new BigDecimal(minProductivity) : null;
        BigDecimal maxProd = maxProductivity != null ? new BigDecimal(maxProductivity) : null;
        
        return recordRepository.filterRecords(employeeId, departmentId, location, 
            recordStatus, start, end, minProd, maxProd, pageable)
            .map(OperationalRecordResponse::from);
    }
    
    @Transactional
    public OperationalRecordResponse updateRecord(String id, OperationalRecordRequest request) {
        OperationalRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("OperationalRecord", "id", id));
        
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getEmployeeId()));
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        record.setEmployee(employee);
        record.setDepartment(department);
        record.setProcessType(request.getProcessType());
        record.setUnitsProcessed(request.getUnitsProcessed());
        record.setHoursWorked(new BigDecimal(request.getHoursWorked()));
        record.setErrors(request.getErrors());
        record.setStatus(request.getStatus() != null ? request.getStatus() : RecordStatus.VALID);
        record.setLocation(request.getLocation());
        record.setWorkDate(request.getWorkDate());
        
        record = recordRepository.save(record);
        log.info("Updated operational record: {}", id);
        
        return OperationalRecordResponse.from(record);
    }
    
    @Transactional
    public void deleteRecord(String id) {
        OperationalRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("OperationalRecord", "id", id));
        recordRepository.delete(record);
        log.info("Deleted operational record: {}", id);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getAverageProductivity(LocalDate startDate, LocalDate endDate, String departmentId) {
        return recordRepository.getAverageProductivity(startDate, endDate, departmentId);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getAverageErrorRate(LocalDate startDate, LocalDate endDate, String departmentId) {
        return recordRepository.getAverageErrorRate(startDate, endDate, departmentId);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalUnitsProcessed(LocalDate startDate, LocalDate endDate, String departmentId) {
        return recordRepository.getTotalUnitsProcessed(startDate, endDate, departmentId);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalErrors(LocalDate startDate, LocalDate endDate, String departmentId) {
        return recordRepository.getTotalErrors(startDate, endDate, departmentId);
    }
    
    @Transactional(readOnly = true)
    public long getRecordCount(LocalDate startDate, LocalDate endDate, String departmentId) {
        return recordRepository.countRecords(startDate, endDate, departmentId);
    }
}
