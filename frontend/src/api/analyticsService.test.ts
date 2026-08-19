import { describe, it, expect, vi, beforeEach } from 'vitest';
import { analyticsService } from './analyticsService';
import axios from 'axios';

vi.mock('axios');

describe('analyticsService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('fetches dashboard summary', async () => {
    const mockResponse = {
      data: {
        totalEmployees: 100,
        activeEmployees: 95,
        totalRecords: 10000,
        unitsProcessed: 500000,
        averageProductivity: 50.0,
        totalErrors: 500,
        averageErrorRate: 0.1,
        openWorkflows: 25,
        criticalAlerts: 5,
        automationRuns: 100
      }
    };

    (axios.get as any).mockResolvedValue(mockResponse);

    const result = await analyticsService.getDashboardSummary();

    expect(axios.get).toHaveBeenCalledWith('/api/analytics/dashboard');
    expect(result.totalEmployees).toBe(100);
    expect(result.activeEmployees).toBe(95);
  });

  it('fetches dashboard summary with filters', async () => {
    const mockResponse = {
      data: {
        totalEmployees: 50,
        activeEmployees: 45,
        totalRecords: 5000,
        unitsProcessed: 250000,
        averageProductivity: 50.0,
        totalErrors: 250,
        averageErrorRate: 0.1,
        openWorkflows: 10,
        criticalAlerts: 2,
        automationRuns: 50
      }
    };

    (axios.get as any).mockResolvedValue(mockResponse);

    const result = await analyticsService.getDashboardSummary('2024-01-01', '2024-12-31', 'dept-1');

    expect(axios.get).toHaveBeenCalledWith('/api/analytics/dashboard', {
      params: {
        startDate: '2024-01-01',
        endDate: '2024-12-31',
        departmentId: 'dept-1'
      }
    });
    expect(result.totalEmployees).toBe(50);
  });
});
