import { apiClient } from './client';
import { Alert, PaginatedResponse } from '../types';

export const alertService = {
  async getAllAlerts(
    employeeId?: string,
    departmentId?: string,
    type?: string,
    severity?: string,
    resolved?: boolean,
    startDate?: string,
    endDate?: string,
    page = 0,
    size = 20,
    sort = 'createdAt,desc'
  ): Promise<PaginatedResponse<Alert>> {
    const response = await apiClient.get<PaginatedResponse<Alert>>('/alerts', {
      params: { employeeId, departmentId, type, severity, resolved, startDate, endDate, page, size, sort }
    });
    return response.data;
  },

  async getAlertById(id: string): Promise<Alert> {
    const response = await apiClient.get<Alert>(`/alerts/${id}`);
    return response.data;
  },

  async resolveAlert(id: string, resolvedByUserId: string): Promise<Alert> {
    const response = await apiClient.patch<Alert>(`/alerts/${id}/resolve`, null, {
      params: { resolvedByUserId }
    });
    return response.data;
  }
};
