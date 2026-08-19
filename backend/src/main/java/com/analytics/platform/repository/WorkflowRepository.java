package com.analytics.platform.repository;

import com.analytics.platform.entity.Workflow;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.User;
import com.analytics.platform.entity.Workflow.Priority;
import com.analytics.platform.entity.Workflow.WorkflowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, String> {
    
    Page<Workflow> findByDepartment(Department department, Pageable pageable);
    Page<Workflow> findByAssignedEmployee(Employee employee, Pageable pageable);
    Page<Workflow> findByAssignedUser(User user, Pageable pageable);
    Page<Workflow> findByStatus(WorkflowStatus status, Pageable pageable);
    Page<Workflow> findByPriority(Priority priority, Pageable pageable);
    Page<Workflow> findByDueDateBefore(LocalDate date, Pageable pageable);
    
    @Query("SELECT w FROM Workflow w WHERE " +
           "(:departmentId IS NULL OR w.department.id = :departmentId) AND " +
           "(:status IS NULL OR w.status = :status) AND " +
           "(:priority IS NULL OR w.priority = :priority) AND " +
           "(:assignedUserId IS NULL OR w.assignedUser.id = :assignedUserId) AND " +
           "(:dueDateBefore IS NULL OR w.dueDate <= :dueDateBefore) AND " +
           "(:dueDateAfter IS NULL OR w.dueDate >= :dueDateAfter)")
    Page<Workflow> filterWorkflows(
        @Param("departmentId") String departmentId,
        @Param("status") WorkflowStatus status,
        @Param("priority") Priority priority,
        @Param("assignedUserId") String assignedUserId,
        @Param("dueDateBefore") LocalDate dueDateBefore,
        @Param("dueDateAfter") LocalDate dueDateAfter,
        Pageable pageable
    );
    
    @Query("SELECT COUNT(w) FROM Workflow w WHERE w.status = :status")
    long countByStatus(@Param("status") WorkflowStatus status);
    
    @Query("SELECT w FROM Workflow w WHERE w.status IN :statuses AND w.dueDate <= :warningDate")
    List<Workflow> findWorkflowsApproachingDeadline(
        @Param("statuses") List<WorkflowStatus> statuses,
        @Param("warningDate") LocalDate warningDate
    );
    
    @Query("SELECT w FROM Workflow w WHERE w.status IN :statuses AND w.dueDate < :overdueDate")
    List<Workflow> findOverdueWorkflows(
        @Param("statuses") List<WorkflowStatus> statuses,
        @Param("overdueDate") LocalDate overdueDate
    );
}
