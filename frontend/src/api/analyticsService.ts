import { apiClient } from './client';
import { DashboardSummary, AnalyticsData } from '../types';

export const analyticsService = {
  async getDashboardSummary(startDate?: string, endDate?: string, departmentId?: string): Promise<DashboardSummary> {
    const response = await apiClient.get<DashboardSummary>('/analytics/summary', {
      params: { startDate, endDate, departmentId }
    });
    return response.data;
  },

  async getProductivityTrend(startDate?: string, endDate?: string, departmentId?: string, groupBy = 'day'): Promise<AnalyticsData> {
    const response = await apiClient.get<AnalyticsData>('/analytics/productivity-trend', {
      params: { startDate, endDate, departmentId, groupBy }
    });
    return response.data;
  },

  async getDepartmentPerformance(startDate?: string, endDate?: string): Promise<AnalyticsData> {
    const response = await apiClient.get<AnalyticsData>('/analytics/department-performance', {
      params: { startDate, endDate }
    });
    return response.data;
  },

  async getErrorTrend(startDate?: string, endDate?: string, departmentId?: string): Promise<AnalyticsData> {
    const response = await apiClient.get<AnalyticsData>('/analytics/error-trend', {
      params: { startDate, endDate, departmentId }
    });
    return response.data;
  },

  async getTopPerformers(startDate?: string, endDate?: string, departmentId?: string, limit = 10): Promise<AnalyticsData> {
    const response = await apiClient.get<AnalyticsData>('/analytics/top-performers', {
      params: { startDate, endDate, departmentId, limit }
    });
    return response.data;
  }
};
