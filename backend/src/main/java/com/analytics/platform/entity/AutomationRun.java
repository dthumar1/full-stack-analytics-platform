package com.analytics.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "automation_runs", indexes = {
    @Index(name = "idx_automation_runs_rule", columnList = "automation_rule_id"),
    @Index(name = "idx_automation_runs_type", columnList = "automation_type"),
    @Index(name = "idx_automation_runs_start_time", columnList = "start_time"),
    @Index(name = "idx_automation_runs_status", columnList = "status"),
    @Index(name = "idx_automation_runs_type_start_time", columnList = "automation_type,start_time")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRun {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "automation_rule_id")
    private AutomationRule automationRule;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "automation_type", nullable = false)
    private AutomationType automationType;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "records_processed", nullable = false)
    @Builder.Default
    private Integer recordsProcessed = 0;
    
    @Column(name = "alerts_created", nullable = false)
    @Builder.Default
    private Integer alertsCreated = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AutomationStatus status = AutomationStatus.STARTED;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    public enum AutomationType {
        PRODUCTIVITY_CHECK, ERROR_RATE_CHECK, DEADLINE_CHECK, BATCH_PROCESSING
    }
    
    public enum AutomationStatus {
        STARTED, COMPLETED, FAILED
    }
}
