package com.analytics.platform.repository;

import com.analytics.platform.entity.OperationalRecord;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.OperationalRecord.ProcessType;
import com.analytics.platform.entity.OperationalRecord.RecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OperationalRecordRepository extends JpaRepository<OperationalRecord, String> {
    
    Page<OperationalRecord> findByEmployee(Employee employee, Pageable pageable);
    Page<OperationalRecord> findByDepartment(Department department, Pageable pageable);
    Page<OperationalRecord> findByWorkDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<OperationalRecord> findByStatus(RecordStatus status, Pageable pageable);
    Page<OperationalRecord> findByLocation(String location, Pageable pageable);
    Page<OperationalRecord> findByProcessType(ProcessType processType, Pageable pageable);
    
    @Query("SELECT r FROM OperationalRecord r WHERE " +
           "(:employeeId IS NULL OR r.employee.id = :employeeId) AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId) AND " +
           "(:location IS NULL OR r.location = :location) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:startDate IS NULL OR r.workDate >= :startDate) AND " +
           "(:endDate IS NULL OR r.workDate <= :endDate) AND " +
           "(:minProductivity IS NULL OR r.productivityRate >= :minProductivity) AND " +
           "(:maxProductivity IS NULL OR r.productivityRate <= :maxProductivity)")
    Page<OperationalRecord> filterRecords(
        @Param("employeeId") String employeeId,
        @Param("departmentId") String departmentId,
        @Param("location") String location,
        @Param("status") RecordStatus status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("minProductivity") BigDecimal minProductivity,
        @Param("maxProductivity") BigDecimal maxProductivity,
        Pageable pageable
    );
    
    @Query("SELECT AVG(r.productivityRate) FROM OperationalRecord r WHERE " +
           "r.workDate BETWEEN :startDate AND :endDate AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId)")
    BigDecimal getAverageProductivity(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("departmentId") String departmentId
    );
    
    @Query("SELECT AVG(r.errorRate) FROM OperationalRecord r WHERE " +
           "r.workDate BETWEEN :startDate AND :endDate AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId)")
    BigDecimal getAverageErrorRate(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("departmentId") String departmentId
    );
    
    @Query("SELECT SUM(r.unitsProcessed) FROM OperationalRecord r WHERE " +
           "r.workDate BETWEEN :startDate AND :endDate AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId)")
    Long getTotalUnitsProcessed(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("departmentId") String departmentId
    );
    
    @Query("SELECT SUM(r.errors) FROM OperationalRecord r WHERE " +
           "r.workDate BETWEEN :startDate AND :endDate AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId)")
    Long getTotalErrors(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("departmentId") String departmentId
    );
    
    @Query("SELECT COUNT(r) FROM OperationalRecord r WHERE " +
           "r.workDate BETWEEN :startDate AND :endDate AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId)")
    long countRecords(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("departmentId") String departmentId
    );
    
    @Query("SELECT r.employee.id, r.employee.firstName, r.employee.lastName, " +
           "AVG(r.productivityRate) as avgProductivity, SUM(r.unitsProcessed) as totalUnits " +
           "FROM OperationalRecord r WHERE " +
           "r.workDate BETWEEN :startDate AND :endDate AND " +
           "(:departmentId IS NULL OR r.department.id = :departmentId) " +
           "GROUP BY r.employee.id, r.employee.firstName, r.employee.lastName " +
           "ORDER BY avgProductivity DESC")
    java.util.List<Object[]> findTopPerformers(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("departmentId") String departmentId,
        Pageable pageable
    );
}
