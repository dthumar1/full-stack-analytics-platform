import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import { DashboardPage } from './DashboardPage';
import { MemoryRouter } from 'react-router-dom';
import * as analyticsService from '../api/analyticsService';

describe('DashboardPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders loading state initially', () => {
    vi.spyOn(analyticsService, 'getDashboardSummary').mockImplementation(() => new Promise(() => {}));

    render(
      <MemoryRouter>
        <DashboardPage />
      </MemoryRouter>
    );

    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('renders dashboard data after loading', async () => {
    const mockData = {
      totalEmployees: 100,
      activeEmployees: 95,
      totalRecords: 10000,
      unitsProcessed: 500000,
      averageProductivity: 50.0,
      totalErrors: 500,
      averageErrorRate: 0.1,
      openWorkflows: 25,
      criticalAlerts: 5,
      automationRuns: 100
    };

    vi.spyOn(analyticsService, 'getDashboardSummary').mockResolvedValue(mockData);

    render(
      <MemoryRouter>
        <DashboardPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('Dashboard')).toBeInTheDocument();
    });
  });

  it('displays error message when API call fails', async () => {
    vi.spyOn(analyticsService, 'getDashboardSummary').mockRejectedValue(new Error('API Error'));

    render(
      <MemoryRouter>
        <DashboardPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('Failed to load dashboard data')).toBeInTheDocument();
    });
  });
});
