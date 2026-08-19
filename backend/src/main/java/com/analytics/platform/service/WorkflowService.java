package com.analytics.platform.service;

import com.analytics.platform.dto.request.WorkflowRequest;
import com.analytics.platform.dto.request.WorkflowStatusUpdateRequest;
import com.analytics.platform.dto.response.WorkflowResponse;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.entity.User;
import com.analytics.platform.entity.Workflow;
import com.analytics.platform.entity.Workflow.Priority;
import com.analytics.platform.entity.Workflow.WorkflowStatus;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.DepartmentRepository;
import com.analytics.platform.repository.EmployeeRepository;
import com.analytics.platform.repository.UserRepository;
import com.analytics.platform.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowService {
    
    private final WorkflowRepository workflowRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public WorkflowResponse createWorkflow(WorkflowRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Employee assignedEmployee = null;
        if (request.getAssignedEmployeeId() != null) {
            assignedEmployee = employeeRepository.findById(request.getAssignedEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getAssignedEmployeeId()));
        }
        
        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
        }
        
        Workflow workflow = Workflow.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .department(department)
            .assignedEmployee(assignedEmployee)
            .assignedUser(assignedUser)
            .priority(request.getPriority())
            .status(request.getStatus() != null ? request.getStatus() : WorkflowStatus.NEW)
            .dueDate(request.getDueDate())
            .build();
        
        workflow = workflowRepository.save(workflow);
        log.info("Created workflow: {}", request.getTitle());
        
        return WorkflowResponse.from(workflow);
    }
    
    @Transactional(readOnly = true)
    public Page<WorkflowResponse> getAllWorkflows(Pageable pageable) {
        return workflowRepository.findAll(pageable)
            .map(WorkflowResponse::from);
    }
    
    @Transactional(readOnly = true)
    public WorkflowResponse getWorkflowById(String id) {
        Workflow workflow = workflowRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", id));
        return WorkflowResponse.from(workflow);
    }
    
    @Transactional(readOnly = true)
    public Page<WorkflowResponse> filterWorkflows(String departmentId, String status, String priority,
                                                 String assignedUserId, String dueDateBefore, 
                                                 String dueDateAfter, Pageable pageable) {
        WorkflowStatus workflowStatus = status != null ? WorkflowStatus.valueOf(status) : null;
        Priority workflowPriority = priority != null ? Priority.valueOf(priority) : null;
        LocalDate before = dueDateBefore != null ? LocalDate.parse(dueDateBefore) : null;
        LocalDate after = dueDateAfter != null ? LocalDate.parse(dueDateAfter) : null;
        
        return workflowRepository.filterWorkflows(departmentId, workflowStatus, workflowPriority,
            assignedUserId, before, after, pageable)
            .map(WorkflowResponse::from);
    }
    
    @Transactional
    public WorkflowResponse updateWorkflow(String id, WorkflowRequest request) {
        Workflow workflow = workflowRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", id));
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        
        Employee assignedEmployee = null;
        if (request.getAssignedEmployeeId() != null) {
            assignedEmployee = employeeRepository.findById(request.getAssignedEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getAssignedEmployeeId()));
        }
        
        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
        }
        
        workflow.setTitle(request.getTitle());
        workflow.setDescription(request.getDescription());
        workflow.setDepartment(department);
        workflow.setAssignedEmployee(assignedEmployee);
        workflow.setAssignedUser(assignedUser);
        workflow.setPriority(request.getPriority());
        workflow.setDueDate(request.getDueDate());
        
        workflow = workflowRepository.save(workflow);
        log.info("Updated workflow: {}", id);
        
        return WorkflowResponse.from(workflow);
    }
    
    @Transactional
    public WorkflowResponse updateWorkflowStatus(String id, WorkflowStatusUpdateRequest request) {
        Workflow workflow = workflowRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", id));
        
        workflow.setStatus(request.getStatus());
        
        if (request.getStatus() == WorkflowStatus.COMPLETED) {
            workflow.setCompletedDate(LocalDate.now());
        } else {
            workflow.setCompletedDate(null);
        }
        
        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
            workflow.setAssignedUser(assignedUser);
        }
        
        workflow = workflowRepository.save(workflow);
        log.info("Updated workflow status: {} to {}", id, request.getStatus());
        
        return WorkflowResponse.from(workflow);
    }
    
    @Transactional
    public void deleteWorkflow(String id) {
        Workflow workflow = workflowRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", id));
        workflowRepository.delete(workflow);
        log.info("Deleted workflow: {}", id);
    }
    
    @Transactional(readOnly = true)
    public List<Workflow> findWorkflowsApproachingDeadline(int days) {
        LocalDate warningDate = LocalDate.now().plusDays(days);
        return workflowRepository.findWorkflowsApproachingDeadline(
            List.of(WorkflowStatus.NEW, WorkflowStatus.IN_PROGRESS), warningDate);
    }
    
    @Transactional(readOnly = true)
    public List<Workflow> findOverdueWorkflows() {
        return workflowRepository.findOverdueWorkflows(
            List.of(WorkflowStatus.NEW, WorkflowStatus.IN_PROGRESS), LocalDate.now());
    }
    
    @Transactional(readOnly = true)
    public long getOpenWorkflowCount() {
        return workflowRepository.countByStatus(WorkflowStatus.NEW) + 
               workflowRepository.countByStatus(WorkflowStatus.IN_PROGRESS);
    }
}
