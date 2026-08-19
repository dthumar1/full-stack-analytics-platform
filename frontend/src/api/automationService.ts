import { apiClient } from './client';
import { AutomationRule, AutomationRun, PaginatedResponse } from '../types';

export const automationService = {
  async getAutomationRuns(
    automationType?: string,
    status?: string,
    startDate?: string,
    endDate?: string,
    page = 0,
    size = 20,
    sort = 'startTime,desc'
  ): Promise<PaginatedResponse<AutomationRun>> {
    const response = await apiClient.get<PaginatedResponse<AutomationRun>>('/automation/runs', {
      params: { automationType, status, startDate, endDate, page, size, sort }
    });
    return response.data;
  },

  async getAutomationRules(page = 0, size = 20, sort = 'name,asc'): Promise<PaginatedResponse<AutomationRule>> {
    const response = await apiClient.get<PaginatedResponse<AutomationRule>>('/automation/rules', {
      params: { page, size, sort }
    });
    return response.data;
  },

  async updateAutomationRule(id: string, enabled: boolean): Promise<AutomationRule> {
    const response = await apiClient.put<AutomationRule>(`/automation/rules/${id}`, null, {
      params: { enabled }
    });
    return response.data;
  },

  async triggerAutomation(automationType: string): Promise<AutomationRun> {
    const response = await apiClient.post<AutomationRun>('/automation/run', null, {
      params: { automationType }
    });
    return response.data;
  }
};
