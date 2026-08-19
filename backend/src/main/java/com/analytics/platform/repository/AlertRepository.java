package com.analytics.platform.repository;

import com.analytics.platform.entity.Alert;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.Alert.AlertType;
import com.analytics.platform.entity.Alert.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {
    
    Page<Alert> findByEmployee(Employee employee, Pageable pageable);
    Page<Alert> findByDepartment(Department department, Pageable pageable);
    Page<Alert> findByAlertType(AlertType alertType, Pageable pageable);
    Page<Alert> findBySeverity(Severity severity, Pageable pageable);
    Page<Alert> findByResolved(Boolean resolved, Pageable pageable);
    
    @Query("SELECT a FROM Alert a WHERE " +
           "(:employeeId IS NULL OR a.employee.id = :employeeId) AND " +
           "(:departmentId IS NULL OR a.department.id = :departmentId) AND " +
           "(:type IS NULL OR a.alertType = :type) AND " +
           "(:severity IS NULL OR a.severity = :severity) AND " +
           "(:resolved IS NULL OR a.resolved = :resolved) AND " +
           "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR a.createdAt <= :endDate)")
    Page<Alert> filterAlerts(
        @Param("employeeId") String employeeId,
        @Param("departmentId") String departmentId,
        @Param("type") AlertType type,
        @Param("severity") Severity severity,
        @Param("resolved") Boolean resolved,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.resolved = :resolved AND a.severity = :severity")
    long countByResolvedAndSeverity(@Param("resolved") Boolean resolved, @Param("severity") Severity severity);
    
    @Query("SELECT a FROM Alert a WHERE a.resolved = false AND a.alertType = :type AND " +
           "a.employee.id = :employeeId AND a.createdAt > :sinceDate")
    java.util.List<Alert> findUnresolvedAlertsForEmployeeAndTypeSince(
        @Param("employeeId") String employeeId,
        @Param("type") AlertType type,
        @Param("sinceDate") LocalDateTime sinceDate
    );
}
