package com.analytics.platform.repository;

import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee.EmployeeStatus;
import com.analytics.platform.entity.Employee.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    Optional<Employee> findByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByEmail(String email);
    
    Page<Employee> findByDepartment(Department department, Pageable pageable);
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);
    Page<Employee> findByLocation(String location, Pageable pageable);
    Page<Employee> findByShift(Shift shift, Pageable pageable);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "(:search IS NULL OR " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:department IS NULL OR e.department = :department) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:location IS NULL OR e.location = :location) AND " +
           "(:shift IS NULL OR e.shift = :shift)")
    Page<Employee> searchEmployees(
        @Param("search") String search,
        @Param("department") Department department,
        @Param("status") EmployeeStatus status,
        @Param("location") String location,
        @Param("shift") Shift shift,
        Pageable pageable
    );
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    long countByStatus(@Param("status") EmployeeStatus status);
}
