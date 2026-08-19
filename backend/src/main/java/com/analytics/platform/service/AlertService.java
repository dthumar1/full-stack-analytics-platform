package com.analytics.platform.service;

import com.analytics.platform.dto.response.AlertResponse;
import com.analytics.platform.entity.Alert;
import com.analytics.platform.entity.Alert.AlertType;
import com.analytics.platform.entity.Alert.Severity;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.OperationalRecord;
import com.analytics.platform.entity.User;
import com.analytics.platform.entity.Workflow;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.AlertRepository;
import com.analytics.platform.repository.DepartmentRepository;
import com.analytics.platform.repository.EmployeeRepository;
import com.analytics.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public AlertResponse createAlert(AlertType type, Severity severity, String message,
                                    String employeeId, String departmentId, 
                                    String operationalRecordId, String workflowId) {
        Alert.AlertBuilder builder = Alert.builder()
            .alertType(type)
            .severity(severity)
            .message(message)
            .resolved(false);
        
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
            builder.employee(employee);
        }
        
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
            builder.department(department);
        }
        
        Alert alert = builder.build();
        alert = alertRepository.save(alert);
        log.info("Created alert: {} - {}", type, message);
        
        return AlertResponse.from(alert);
    }
    
    @Transactional(readOnly = true)
    public Page<AlertResponse> getAllAlerts(Pageable pageable) {
        return alertRepository.findAll(pageable)
            .map(AlertResponse::from);
    }
    
    @Transactional(readOnly = true)
    public AlertResponse getAlertById(String id) {
        Alert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));
        return AlertResponse.from(alert);
    }
    
    @Transactional(readOnly = true)
    public Page<AlertResponse> filterAlerts(String employeeId, String departmentId, String type,
                                            String severity, Boolean resolved, String startDate,
                                            String endDate, Pageable pageable) {
        AlertType alertType = type != null ? AlertType.valueOf(type) : null;
        Severity alertSeverity = severity != null ? Severity.valueOf(severity) : null;
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        
        return alertRepository.filterAlerts(employeeId, departmentId, alertType, alertSeverity,
            resolved, start, end, pageable)
            .map(AlertResponse::from);
    }
    
    @Transactional
    public AlertResponse resolveAlert(String id, String resolvedByUserId) {
        Alert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));
        
        if (alert.getResolved()) {
            throw new RuntimeException("Alert is already resolved");
        }
        
        User resolvedBy = userRepository.findById(resolvedByUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", resolvedByUserId));
        
        alert.setResolved(true);
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedAt(LocalDateTime.now());
        
        alert = alertRepository.save(alert);
        log.info("Resolved alert: {}", id);
        
        return AlertResponse.from(alert);
    }
    
    @Transactional(readOnly = true)
    public long getCriticalAlertCount() {
        return alertRepository.countByResolvedAndSeverity(false, Severity.CRITICAL);
    }
    
    @Transactional(readOnly = true)
    public boolean hasUnresolvedAlertForEmployeeAndType(String employeeId, AlertType type, LocalDateTime since) {
        return !alertRepository.findUnresolvedAlertsForEmployeeAndTypeSince(employeeId, type, since).isEmpty();
    }
}
