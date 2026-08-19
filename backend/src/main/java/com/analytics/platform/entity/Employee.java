package com.analytics.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employees_code", columnList = "employee_code", unique = true),
    @Index(name = "idx_employees_email", columnList = "email"),
    @Index(name = "idx_employees_department", columnList = "department_id"),
    @Index(name = "idx_employees_status", columnList = "status"),
    @Index(name = "idx_employees_location", columnList = "location")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @Column(name = "job_title", nullable = false)
    private String jobTitle;
    
    @Column(name = "location", nullable = false)
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false)
    private Shift shift;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmployeeStatus status;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OperationalRecord> operationalRecords = new HashSet<>();
    
    @OneToMany(mappedBy = "assignedEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Workflow> assignedWorkflows = new HashSet<>();
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Alert> alerts = new HashSet<>();
    
    public enum Shift {
        DAY, NIGHT, FLEX
    }
    
    public enum EmployeeStatus {
        ACTIVE, INACTIVE, LEAVE
    }
}
