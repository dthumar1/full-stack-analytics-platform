export interface OperationalRecord {
  id: string;
  employeeId: string;
  employeeName: string;
  departmentId: string;
  departmentName: string;
  processType: 'RECEIVING' | 'STOCKING' | 'PICKING' | 'PACKING' | 'SHIPPING';
  unitsProcessed: number;
  hoursWorked: number;
  productivityRate: number;
  errors: number;
  errorRate: number;
  status: 'VALID' | 'FLAGGED' | 'UNDER_REVIEW';
  location: string;
  workDate: string;
  createdAt: string;
  updatedAt: string;
}

export interface OperationalRecordRequest {
  employeeId: string;
  departmentId: string;
  processType: 'RECEIVING' | 'STOCKING' | 'PICKING' | 'PACKING' | 'SHIPPING';
  unitsProcessed: number;
  hoursWorked: string;
  errors: number;
  status: 'VALID' | 'FLAGGED' | 'UNDER_REVIEW';
  location: string;
  workDate: string;
}
