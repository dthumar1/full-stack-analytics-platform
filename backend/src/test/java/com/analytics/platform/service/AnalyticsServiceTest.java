package com.analytics.platform.service;

import com.analytics.platform.dto.response.DashboardSummaryResponse;
import com.analytics.platform.repository.EmployeeRepository;
import com.analytics.platform.repository.OperationalRecordRepository;
import com.analytics.platform.repository.WorkflowRepository;
import com.analytics.platform.repository.AlertRepository;
import com.analytics.platform.repository.AutomationRunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OperationalRecordRepository recordRepository;

    @Mock
    private WorkflowRepository workflowRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AutomationRunRepository automationRunRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(employeeRepository, recordRepository, workflowRepository, alertRepository, automationRunRepository);
    }

    @Test
    void getDashboardSummary_Success() {
        when(employeeRepository.count()).thenReturn(100L);
        when(employeeRepository.countByStatus("ACTIVE")).thenReturn(95L);
        when(recordRepository.count()).thenReturn(10000L);
        when(recordRepository.sumUnitsProcessed()).thenReturn(500000L);
        when(recordRepository.averageProductivity()).thenReturn(BigDecimal.valueOf(50.0));
        when(recordRepository.sumErrors()).thenReturn(500L);
        when(recordRepository.averageErrorRate()).thenReturn(BigDecimal.valueOf(0.1));
        when(workflowRepository.countByStatusNot("COMPLETED")).thenReturn(25L);
        when(alertRepository.countByResolvedFalseAndSeverity("CRITICAL")).thenReturn(5L);
        when(automationRunRepository.count()).thenReturn(100L);

        DashboardSummaryResponse summary = analyticsService.getDashboardSummary(null, null, null);

        assertNotNull(summary);
        assertEquals(100, summary.getTotalEmployees());
        assertEquals(95, summary.getActiveEmployees());
        assertEquals(10000, summary.getTotalRecords());
        assertEquals(500000, summary.getUnitsProcessed());
        assertEquals(BigDecimal.valueOf(50.0), summary.getAverageProductivity());
        assertEquals(500, summary.getTotalErrors());
        assertEquals(BigDecimal.valueOf(0.1), summary.getAverageErrorRate());
        assertEquals(25, summary.getOpenWorkflows());
        assertEquals(5, summary.getCriticalAlerts());
        assertEquals(100, summary.getAutomationRuns());

        verify(employeeRepository).count();
        verify(employeeRepository).countByStatus("ACTIVE");
        verify(recordRepository).count();
        verify(recordRepository).sumUnitsProcessed();
        verify(recordRepository).averageProductivity();
        verify(recordRepository).sumErrors();
        verify(recordRepository).averageErrorRate();
        verify(workflowRepository).countByStatusNot("COMPLETED");
        verify(alertRepository).countByResolvedFalseAndSeverity("CRITICAL");
        verify(automationRunRepository).count();
    }
}
