export interface Alert {
  id: string;
  alertType: 'LOW_PRODUCTIVITY' | 'HIGH_ERROR_RATE' | 'DEADLINE_APPROACHING' | 'WORKFLOW_OVERDUE' | 'DATA_ANOMALY';
  severity: 'INFO' | 'WARNING' | 'HIGH' | 'CRITICAL';
  message: string;
  employeeId: string;
  employeeName: string;
  departmentId: string;
  departmentName: string;
  operationalRecordId: string;
  workflowId: string;
  resolved: boolean;
  resolvedBy: string;
  resolvedAt: string;
  createdAt: string;
}
