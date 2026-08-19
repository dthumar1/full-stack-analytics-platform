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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "automation_rules", indexes = {
    @Index(name = "idx_automation_rules_type", columnList = "rule_type"),
    @Index(name = "idx_automation_rules_enabled", columnList = "enabled")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType;
    
    @Column(name = "threshold", precision = 10, scale = 2)
    private BigDecimal threshold;
    
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "automationRule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AutomationRun> automationRuns = new HashSet<>();
    
    public enum RuleType {
        LOW_PRODUCTIVITY, HIGH_ERROR_RATE, WORKFLOW_DEADLINE
    }
}
