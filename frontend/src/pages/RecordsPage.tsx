import React, { useEffect, useState } from 'react';
import { operationalRecordService } from '../api';
import { OperationalRecord, PaginatedResponse } from '../types';

export const RecordsPage: React.FC = () => {
  const [records, setRecords] = useState<OperationalRecord[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadRecords = async () => {
      try {
        const data: PaginatedResponse<OperationalRecord> = await operationalRecordService.getAllRecords();
        setRecords(data.content);
      } catch (error) {
        console.error('Failed to load records:', error);
      } finally {
        setLoading(false);
      }
    };
    loadRecords();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Operational Records</h1>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Employee</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Process</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Units</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Productivity</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Errors</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {records.map((record) => (
              <tr key={record.id}>
                <td className="px-6 py-4 whitespace-nowrap">{record.employeeName}</td>
                <td className="px-6 py-4 whitespace-nowrap">{record.workDate}</td>
                <td className="px-6 py-4 whitespace-nowrap">{record.processType}</td>
                <td className="px-6 py-4 whitespace-nowrap">{record.unitsProcessed}</td>
                <td className="px-6 py-4 whitespace-nowrap">{record.productivityRate?.toFixed(2)}</td>
                <td className="px-6 py-4 whitespace-nowrap">{record.errors}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 rounded text-xs ${
                    record.status === 'VALID' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                  }`}>
                    {record.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
