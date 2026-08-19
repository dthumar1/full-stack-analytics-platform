import { apiClient } from './client';
import { Workflow, WorkflowRequest, WorkflowStatusUpdateRequest, PaginatedResponse } from '../types';

export const workflowService = {
  async getAllWorkflows(page = 0, size = 20, sort = 'createdAt,desc'): Promise<PaginatedResponse<Workflow>> {
    const response = await apiClient.get<PaginatedResponse<Workflow>>('/workflows', {
      params: { page, size, sort }
    });
    return response.data;
  },

  async getWorkflowById(id: string): Promise<Workflow> {
    const response = await apiClient.get<Workflow>(`/workflows/${id}`);
    return response.data;
  },

  async filterWorkflows(
    departmentId?: string,
    status?: string,
    priority?: string,
    assignedUserId?: string,
    dueDateBefore?: string,
    dueDateAfter?: string,
    page = 0,
    size = 20,
    sort = 'createdAt,desc'
  ): Promise<PaginatedResponse<Workflow>> {
    const response = await apiClient.get<PaginatedResponse<Workflow>>('/workflows/filter', {
      params: { departmentId, status, priority, assignedUserId, dueDateBefore, dueDateAfter, page, size, sort }
    });
    return response.data;
  },

  async createWorkflow(workflow: WorkflowRequest): Promise<Workflow> {
    const response = await apiClient.post<Workflow>('/workflows', workflow);
    return response.data;
  },

  async updateWorkflow(id: string, workflow: WorkflowRequest): Promise<Workflow> {
    const response = await apiClient.put<Workflow>(`/workflows/${id}`, workflow);
    return response.data;
  },

  async updateWorkflowStatus(id: string, statusUpdate: WorkflowStatusUpdateRequest): Promise<Workflow> {
    const response = await apiClient.patch<Workflow>(`/workflows/${id}/status`, statusUpdate);
    return response.data;
  },

  async deleteWorkflow(id: string): Promise<void> {
    await apiClient.delete(`/workflows/${id}`);
  }
};
