import { describe, it, expect, vi, beforeEach } from 'vitest';
import { employeeService } from './employeeService';
import axios from 'axios';

vi.mock('axios');

describe('employeeService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('fetches all employees with pagination', async () => {
    const mockResponse = {
      data: {
        content: [
          {
            id: '1',
            employeeCode: 'EMP001',
            firstName: 'John',
            lastName: 'Doe',
            email: 'john@example.com',
            departmentName: 'Operations',
            jobTitle: 'Analyst',
            status: 'ACTIVE'
          }
        ],
        totalPages: 1,
        totalElements: 1,
        number: 0,
        size: 20
      }
    };

    (axios.get as any).mockResolvedValue(mockResponse);

    const result = await employeeService.getAllEmployees(0, 20);

    expect(axios.get).toHaveBeenCalledWith('/api/employees', {
      params: { page: 0, size: 20, sort: 'lastName,asc' }
    });
    expect(result.content).toHaveLength(1);
  });

  it('fetches employee by ID', async () => {
    const mockResponse = {
      data: {
        id: '1',
        employeeCode: 'EMP001',
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        departmentName: 'Operations',
        jobTitle: 'Analyst',
        status: 'ACTIVE'
      }
    };

    (axios.get as any).mockResolvedValue(mockResponse);

    const result = await employeeService.getEmployeeById('1');

    expect(axios.get).toHaveBeenCalledWith('/api/employees/1');
    expect(result.firstName).toBe('John');
  });

  it('creates a new employee', async () => {
    const mockRequest = {
      employeeCode: 'EMP001',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      departmentId: 'dept-1',
      jobTitle: 'Analyst',
      location: 'New York',
      shift: 'DAY' as const,
      status: 'ACTIVE' as const,
      hireDate: '2024-01-01'
    };

    const mockResponse = {
      data: { id: '1', ...mockRequest }
    };

    (axios.post as any).mockResolvedValue(mockResponse);

    const result = await employeeService.createEmployee(mockRequest);

    expect(axios.post).toHaveBeenCalledWith('/api/employees', mockRequest);
    expect(result.id).toBe('1');
  });

  it('updates an existing employee', async () => {
    const mockRequest = {
      employeeCode: 'EMP001',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      departmentId: 'dept-1',
      jobTitle: 'Analyst',
      location: 'New York',
      shift: 'DAY' as const,
      status: 'ACTIVE' as const,
      hireDate: '2024-01-01'
    };

    const mockResponse = {
      data: { id: '1', ...mockRequest }
    };

    (axios.put as any).mockResolvedValue(mockResponse);

    const result = await employeeService.updateEmployee('1', mockRequest);

    expect(axios.put).toHaveBeenCalledWith('/api/employees/1', mockRequest);
    expect(result.id).toBe('1');
  });

  it('deletes an employee', async () => {
    (axios.delete as any).mockResolvedValue({});

    await employeeService.deleteEmployee('1');

    expect(axios.delete).toHaveBeenCalledWith('/api/employees/1');
  });
});
