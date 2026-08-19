package com.analytics.platform.service;

import com.analytics.platform.dto.response.AutomationRunResponse;
import com.analytics.platform.dto.response.AutomationRuleResponse;
import com.analytics.platform.entity.Alert;
import com.analytics.platform.entity.AutomationRule;
import com.analytics.platform.entity.AutomationRun;
import com.analytics.platform.entity.AutomationRun.AutomationStatus;
import com.analytics.platform.entity.AutomationRun.AutomationType;
import com.analytics.platform.entity.AutomationRule.RuleType;
import com.analytics.platform.entity.OperationalRecord;
import com.analytics.platform.entity.Workflow;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.AutomationRuleRepository;
import com.analytics.platform.repository.AutomationRunRepository;
import com.analytics.platform.repository.OperationalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutomationService {
    
    private final AutomationRuleRepository ruleRepository;
    private final AutomationRunRepository runRepository;
    private final OperationalRecordRepository recordRepository;
    private final AlertService alertService;
    private final WorkflowService workflowService;
    
    @Value("${automation.low-productivity-threshold:50.0}")
    private BigDecimal lowProductivityThreshold;
    
    @Value("${automation.high-error-rate-threshold:5.0}")
    private BigDecimal highErrorRateThreshold;
    
    @Value("${automation.deadline-warning-days:2}")
    private int deadlineWarningDays;
    
    @Transactional
    public AutomationRunResponse runAutomation(AutomationType automationType) {
        AutomationRun run = AutomationRun.builder()
            .automationType(automationType)
            .startTime(LocalDateTime.now())
            .status(AutomationStatus.STARTED)
            .recordsProcessed(0)
            .alertsCreated(0)
            .build();
        
        run = runRepository.save(run);
        
        try {
            switch (automationType) {
                case PRODUCTIVITY_CHECK:
                    processLowProductivityAlerts(run);
                    break;
                case ERROR_RATE_CHECK:
                    processHighErrorRateAlerts(run);
                    break;
                case DEADLINE_CHECK:
                    processDeadlineAlerts(run);
                    break;
                default:
                    log.warn("Unknown automation type: {}", automationType);
            }
            
            run.setStatus(AutomationStatus.COMPLETED);
            run.setEndTime(LocalDateTime.now());
            log.info("Automation run completed: {} - Records: {}, Alerts: {}", 
                automationType, run.getRecordsProcessed(), run.getAlertsCreated());
            
        } catch (Exception ex) {
            run.setStatus(AutomationStatus.FAILED);
            run.setErrorMessage(ex.getMessage());
            run.setEndTime(LocalDateTime.now());
            log.error("Automation run failed: {}", automationType, ex);
        }
        
        run = runRepository.save(run);
        return AutomationRunResponse.from(run);
    }
    
    private void processLowProductivityAlerts(AutomationRun run) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        List<OperationalRecord> records = recordRepository.findByWorkDateBetween(yesterday, today, 
            org.springframework.data.domain.Pageable.unpaged()).getContent();
        
        run.setRecordsProcessed(records.size());
        
        for (OperationalRecord record : records) {
            if (record.getProductivityRate() != null && 
                record.getProductivityRate().compareTo(lowProductivityThreshold) < 0) {
                
                // Check if alert already exists for this employee and type recently
                LocalDateTime since = LocalDateTime.now().minusHours(24);
                if (!alertService.hasUnresolvedAlertForEmployeeAndType(
                    record.getEmployee().getId(), Alert.AlertType.LOW_PRODUCTIVITY, since)) {
                    
                    alertService.createAlert(
                        Alert.AlertType.LOW_PRODUCTIVITY,
                        Alert.Severity.WARNING,
                        String.format("Low productivity detected: %.2f units/hour (threshold: %.2f)", 
                            record.getProductivityRate(), lowProductivityThreshold),
                        record.getEmployee().getId(),
                        record.getDepartment().getId(),
                        record.getId(),
                        null
                    );
                    run.setAlertsCreated(run.getAlertsCreated() + 1);
                }
            }
        }
    }
    
    private void processHighErrorRateAlerts(AutomationRun run) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        List<OperationalRecord> records = recordRepository.findByWorkDateBetween(yesterday, today,
            org.springframework.data.domain.Pageable.unpaged()).getContent();
        
        run.setRecordsProcessed(records.size());
        
        for (OperationalRecord record : records) {
            if (record.getErrorRate() != null && 
                record.getErrorRate().compareTo(highErrorRateThreshold) > 0) {
                
                LocalDateTime since = LocalDateTime.now().minusHours(24);
                if (!alertService.hasUnresolvedAlertForEmployeeAndType(
                    record.getEmployee().getId(), Alert.AlertType.HIGH_ERROR_RATE, since)) {
                    
                    alertService.createAlert(
                        Alert.AlertType.HIGH_ERROR_RATE,
                        Alert.Severity.HIGH,
                        String.format("High error rate detected: %.2f%% (threshold: %.2f%%)", 
                            record.getErrorRate(), highErrorRateThreshold),
                        record.getEmployee().getId(),
                        record.getDepartment().getId(),
                        record.getId(),
                        null
                    );
                    run.setAlertsCreated(run.getAlertsCreated() + 1);
                }
            }
        }
    }
    
    private void processDeadlineAlerts(AutomationRun run) {
        List<Workflow> approachingWorkflows = workflowService.findWorkflowsApproachingDeadline(deadlineWarningDays);
        List<Workflow> overdueWorkflows = workflowService.findOverdueWorkflows();
        
        run.setRecordsProcessed(approachingWorkflows.size() + overdueWorkflows.size());
        
        for (Workflow workflow : approachingWorkflows) {
            alertService.createAlert(
                Alert.AlertType.DEADLINE_APPROACHING,
                Alert.Severity.WARNING,
                String.format("Workflow deadline approaching: %s (due: %s)", 
                    workflow.getTitle(), workflow.getDueDate()),
                workflow.getAssignedEmployee() != null ? workflow.getAssignedEmployee().getId() : null,
                workflow.getDepartment().getId(),
                null,
                workflow.getId()
            );
            run.setAlertsCreated(run.getAlertsCreated() + 1);
        }
        
        for (Workflow workflow : overdueWorkflows) {
            alertService.createAlert(
                Alert.AlertType.WORKFLOW_OVERDUE,
                Alert.Severity.CRITICAL,
                String.format("Workflow overdue: %s (due: %s)", 
                    workflow.getTitle(), workflow.getDueDate()),
                workflow.getAssignedEmployee() != null ? workflow.getAssignedEmployee().getId() : null,
                workflow.getDepartment().getId(),
                null,
                workflow.getId()
            );
            run.setAlertsCreated(run.getAlertsCreated() + 1);
        }
    }
    
    @Transactional(readOnly = true)
    public Page<AutomationRunResponse> getAutomationRuns(String automationType, String status, 
                                                         String startDate, String endDate, Pageable pageable) {
        AutomationType type = automationType != null ? AutomationType.valueOf(automationType) : null;
        AutomationStatus runStatus = status != null ? AutomationStatus.valueOf(status) : null;
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        
        return runRepository.filterAutomationRuns(type, runStatus, start, end, pageable)
            .map(AutomationRunResponse::from);
    }
    
    @Transactional(readOnly = true)
    public Page<AutomationRuleResponse> getAutomationRules(Pageable pageable) {
        return ruleRepository.findAll(pageable)
            .map(AutomationRuleResponse::from);
    }
    
    @Transactional
    public AutomationRuleResponse updateAutomationRule(String id, Boolean enabled) {
        AutomationRule rule = ruleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AutomationRule", "id", id));
        
        rule.setEnabled(enabled != null ? enabled : !rule.getEnabled());
        rule = ruleRepository.save(rule);
        
        log.info("Updated automation rule: {} - enabled: {}", rule.getName(), rule.getEnabled());
        
        return AutomationRuleResponse.from(rule);
    }
}
