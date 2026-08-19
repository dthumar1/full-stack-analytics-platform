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

@Entity
@Table(name = "workflows", indexes = {
    @Index(name = "idx_workflows_department", columnList = "department_id"),
    @Index(name = "idx_workflows_assigned_employee", columnList = "assigned_employee_id"),
    @Index(name = "idx_workflows_assigned_user", columnList = "assigned_user_id"),
    @Index(name = "idx_workflows_status", columnList = "status"),
    @Index(name = "idx_workflows_priority", columnList = "priority"),
    @Index(name = "idx_workflows_due_date", columnList = "due_date"),
    @Index(name = "idx_workflows_status_due_date", columnList = "status,due_date")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private WorkflowStatus status = WorkflowStatus.NEW;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "completed_date")
    private LocalDate completedDate;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<Alert> alerts = new java.util.HashSet<>();
    
    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum WorkflowStatus {
        NEW, IN_PROGRESS, BLOCKED, COMPLETED, CANCELLED
    }
}
