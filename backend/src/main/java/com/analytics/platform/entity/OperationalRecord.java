package com.analytics.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "operational_records", indexes = {
    @Index(name = "idx_records_employee", columnList = "employee_id"),
    @Index(name = "idx_records_department", columnList = "department_id"),
    @Index(name = "idx_records_work_date", columnList = "work_date"),
    @Index(name = "idx_records_status", columnList = "status"),
    @Index(name = "idx_records_location", columnList = "location"),
    @Index(name = "idx_records_employee_date", columnList = "employee_id,work_date"),
    @Index(name = "idx_records_department_date", columnList = "department_id,work_date")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationalRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "process_type", nullable = false)
    private ProcessType processType;
    
    @Column(name = "units_processed", nullable = false)
    private Integer unitsProcessed;
    
    @Column(name = "hours_worked", nullable = false)
    private BigDecimal hoursWorked;
    
    @Column(name = "productivity_rate", precision = 10, scale = 2)
    private BigDecimal productivityRate;
    
    @Column(name = "errors", nullable = false)
    @Builder.Default
    private Integer errors = 0;
    
    @Column(name = "error_rate", precision = 5, scale = 2)
    private BigDecimal errorRate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private RecordStatus status = RecordStatus.VALID;
    
    @Column(name = "location", nullable = false)
    private String location;
    
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "operationalRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<Alert> alerts = new java.util.HashSet<>();
    
    @PrePersist
    @PreUpdate
    public void calculateMetrics() {
        if (hoursWorked != null && hoursWorked.compareTo(BigDecimal.ZERO) > 0) {
            productivityRate = new BigDecimal(unitsProcessed)
                .divide(hoursWorked, 2, BigDecimal.ROUND_HALF_UP);
        }
        
        if (unitsProcessed != null && unitsProcessed > 0) {
            errorRate = new BigDecimal(errors)
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(unitsProcessed), 2, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    public enum ProcessType {
        RECEIVING, STOCKING, PICKING, PACKING, SHIPPING
    }
    
    public enum RecordStatus {
        VALID, FLAGGED, UNDER_REVIEW
    }
}
