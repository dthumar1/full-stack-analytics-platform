import { apiClient } from './client';
import { OperationalRecord, OperationalRecordRequest, PaginatedResponse } from '../types';

export const operationalRecordService = {
  async getAllRecords(page = 0, size = 20, sort = 'workDate,desc'): Promise<PaginatedResponse<OperationalRecord>> {
    const response = await apiClient.get<PaginatedResponse<OperationalRecord>>('/operational-records', {
      params: { page, size, sort }
    });
    return response.data;
  },

  async getRecordById(id: string): Promise<OperationalRecord> {
    const response = await apiClient.get<OperationalRecord>(`/operational-records/${id}`);
    return response.data;
  },

  async filterRecords(
    employeeId?: string,
    departmentId?: string,
    location?: string,
    status?: string,
    startDate?: string,
    endDate?: string,
    minProductivity?: string,
    maxProductivity?: string,
    page = 0,
    size = 20,
    sort = 'workDate,desc'
  ): Promise<PaginatedResponse<OperationalRecord>> {
    const response = await apiClient.get<PaginatedResponse<OperationalRecord>>('/operational-records/filter', {
      params: { employeeId, departmentId, location, status, startDate, endDate, minProductivity, maxProductivity, page, size, sort }
    });
    return response.data;
  },

  async createRecord(record: OperationalRecordRequest): Promise<OperationalRecord> {
    const response = await apiClient.post<OperationalRecord>('/operational-records', record);
    return response.data;
  },

  async updateRecord(id: string, record: OperationalRecordRequest): Promise<OperationalRecord> {
    const response = await apiClient.put<OperationalRecord>(`/operational-records/${id}`, record);
    return response.data;
  },

  async deleteRecord(id: string): Promise<void> {
    await apiClient.delete(`/operational-records/${id}`);
  }
};
