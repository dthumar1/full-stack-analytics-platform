export interface Employee {
  id: string;
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  departmentId: string;
  departmentName: string;
  jobTitle: string;
  location: string;
  shift: 'DAY' | 'NIGHT' | 'FLEX';
  status: 'ACTIVE' | 'INACTIVE' | 'LEAVE';
  hireDate: string;
  createdAt: string;
  updatedAt: string;
}

export interface EmployeeRequest {
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  departmentId: string;
  jobTitle: string;
  location: string;
  shift: 'DAY' | 'NIGHT' | 'FLEX';
  status: 'ACTIVE' | 'INACTIVE' | 'LEAVE';
  hireDate: string;
}
