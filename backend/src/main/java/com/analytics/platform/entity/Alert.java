package com.analytics.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts", indexes = {
    @Index(name = "idx_alerts_employee", columnList = "employee_id"),
    @Index(name = "idx_alerts_department", columnList = "department_id"),
    @Index(name = "idx_alerts_type", columnList = "alert_type"),
    @Index(name = "idx_alerts_severity", columnList = "severity"),
    @Index(name = "idx_alerts_resolved", columnList = "resolved"),
    @Index(name = "idx_alerts_resolved_created", columnList = "resolved,created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;
    
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operational_record_id")
    private OperationalRecord operationalRecord;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;
    
    @Column(name = "resolved", nullable = false)
    @Builder.Default
    private Boolean resolved = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_id")
    private User resolvedBy;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum AlertType {
        LOW_PRODUCTIVITY, HIGH_ERROR_RATE, DEADLINE_APPROACHING, WORKFLOW_OVERDUE, DATA_ANOMALY
    }
    
    public enum Severity {
        INFO, WARNING, HIGH, CRITICAL
    }
}
