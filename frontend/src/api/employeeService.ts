import { apiClient } from './client';
import { Employee, EmployeeRequest, PaginatedResponse } from '../types';

export const employeeService = {
  async getAllEmployees(page = 0, size = 20, sort = 'lastName,asc'): Promise<PaginatedResponse<Employee>> {
    const response = await apiClient.get<PaginatedResponse<Employee>>('/employees', {
      params: { page, size, sort }
    });
    return response.data;
  },

  async getEmployeeById(id: string): Promise<Employee> {
    const response = await apiClient.get<Employee>(`/employees/${id}`);
    return response.data;
  },

  async searchEmployees(
    search?: string,
    departmentId?: string,
    status?: string,
    location?: string,
    shift?: string,
    page = 0,
    size = 20,
    sort = 'lastName,asc'
  ): Promise<PaginatedResponse<Employee>> {
    const response = await apiClient.get<PaginatedResponse<Employee>>('/employees/search', {
      params: { search, departmentId, status, location, shift, page, size, sort }
    });
    return response.data;
  },

  async createEmployee(employee: EmployeeRequest): Promise<Employee> {
    const response = await apiClient.post<Employee>('/employees', employee);
    return response.data;
  },

  async updateEmployee(id: string, employee: EmployeeRequest): Promise<Employee> {
    const response = await apiClient.put<Employee>(`/employees/${id}`, employee);
    return response.data;
  },

  async deleteEmployee(id: string): Promise<void> {
    await apiClient.delete(`/employees/${id}`);
  }
};
