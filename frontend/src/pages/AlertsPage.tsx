import React, { useEffect, useState } from 'react';
import { alertService } from '../api';
import { Alert, PaginatedResponse } from '../types';

export const AlertsPage: React.FC = () => {
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadAlerts = async () => {
      try {
        const data: PaginatedResponse<Alert> = await alertService.getAllAlerts();
        setAlerts(data.content);
      } catch (error) {
        console.error('Failed to load alerts:', error);
      } finally {
        setLoading(false);
      }
    };
    loadAlerts();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Alerts</h1>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Severity</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Message</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Employee</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Resolved</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Created</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {alerts.map((alert) => (
              <tr key={alert.id}>
                <td className="px-6 py-4 whitespace-nowrap">{alert.alertType}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 rounded text-xs ${
                    alert.severity === 'CRITICAL' ? 'bg-red-100 text-red-800' :
                    alert.severity === 'HIGH' ? 'bg-orange-100 text-orange-800' :
                    alert.severity === 'WARNING' ? 'bg-yellow-100 text-yellow-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {alert.severity}
                  </span>
                </td>
                <td className="px-6 py-4 max-w-md truncate">{alert.message}</td>
                <td className="px-6 py-4 whitespace-nowrap">{alert.employeeName || '-'}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 rounded text-xs ${
                    alert.resolved ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {alert.resolved ? 'Yes' : 'No'}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">{new Date(alert.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
