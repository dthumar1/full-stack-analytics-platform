import React, { useEffect, useState } from 'react';
import { analyticsService } from '../api';
import { DashboardSummary } from '../types';

export const DashboardPage: React.FC = () => {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadSummary = async () => {
      try {
        const data = await analyticsService.getDashboardSummary();
        setSummary(data);
      } catch (error) {
        console.error('Failed to load dashboard summary:', error);
      } finally {
        setLoading(false);
      }
    };
    loadSummary();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Total Employees</h3>
          <p className="text-3xl font-bold">{summary?.totalEmployees || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Active Employees</h3>
          <p className="text-3xl font-bold text-green-600">{summary?.activeEmployees || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Total Records</h3>
          <p className="text-3xl font-bold">{summary?.totalRecords || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Units Processed</h3>
          <p className="text-3xl font-bold text-blue-600">{summary?.unitsProcessed || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Avg Productivity</h3>
          <p className="text-3xl font-bold">{summary?.averageProductivity?.toFixed(2) || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Total Errors</h3>
          <p className="text-3xl font-bold text-red-600">{summary?.totalErrors || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Avg Error Rate</h3>
          <p className="text-3xl font-bold">{summary?.averageErrorRate?.toFixed(2) || 0}%</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Open Workflows</h3>
          <p className="text-3xl font-bold text-yellow-600">{summary?.openWorkflows || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Critical Alerts</h3>
          <p className="text-3xl font-bold text-red-600">{summary?.criticalAlerts || 0}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-gray-500 text-sm">Automation Runs</h3>
          <p className="text-3xl font-bold">{summary?.automationRuns || 0}</p>
        </div>
      </div>
    </div>
  );
};
