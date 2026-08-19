import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import { EmployeesPage } from './EmployeesPage';
import { MemoryRouter } from 'react-router-dom';
import * as employeeService from '../api/employeeService';

describe('EmployeesPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders loading state initially', () => {
    vi.spyOn(employeeService, 'getAllEmployees').mockImplementation(() => new Promise(() => {}));

    render(
      <MemoryRouter>
        <EmployeesPage />
      </MemoryRouter>
    );

    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('renders employees table after loading', async () => {
    const mockData = {
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
    };

    vi.spyOn(employeeService, 'getAllEmployees').mockResolvedValue(mockData);

    render(
      <MemoryRouter>
        <EmployeesPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('Employees')).toBeInTheDocument();
    });
  });

  it('displays error message when API call fails', async () => {
    vi.spyOn(employeeService, 'getAllEmployees').mockRejectedValue(new Error('API Error'));

    render(
      <MemoryRouter>
        <EmployeesPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('Failed to load employees')).toBeInTheDocument();
    });
  });
});
