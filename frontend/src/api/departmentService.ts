import { apiClient } from './client';
import { Department } from '../types';

export const departmentService = {
  async getAllDepartments(): Promise<Department[]> {
    const response = await apiClient.get<Department[]>('/departments');
    return response.data;
  },

  async getDepartmentById(id: string): Promise<Department> {
    const response = await apiClient.get<Department>(`/departments/${id}`);
    return response.data;
  },

  async createDepartment(name: string, description?: string): Promise<Department> {
    const response = await apiClient.post<Department>('/departments', { name, description });
    return response.data;
  },

  async updateDepartment(id: string, name: string, description?: string, active?: boolean): Promise<Department> {
    const response = await apiClient.put<Department>(`/departments/${id}`, { name, description, active });
    return response.data;
  }
};
