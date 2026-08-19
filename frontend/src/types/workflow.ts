export interface Workflow {
  id: string;
  title: string;
  description: string;
  departmentId: string;
  departmentName: string;
  assignedEmployeeId: string;
  assignedEmployeeName: string;
  assignedUserId: string;
  assignedUserName: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  status: 'NEW' | 'IN_PROGRESS' | 'BLOCKED' | 'COMPLETED' | 'CANCELLED';
  dueDate: string;
  completedDate: string;
  createdAt: string;
  updatedAt: string;
}

export interface WorkflowRequest {
  title: string;
  description: string;
  departmentId: string;
  assignedEmployeeId: string;
  assignedUserId: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  status: 'NEW' | 'IN_PROGRESS' | 'BLOCKED' | 'COMPLETED' | 'CANCELLED';
  dueDate: string;
}

export interface WorkflowStatusUpdateRequest {
  status: 'NEW' | 'IN_PROGRESS' | 'BLOCKED' | 'COMPLETED' | 'CANCELLED';
  assignedUserId: string;
}
