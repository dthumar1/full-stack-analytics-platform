import React, { useEffect, useState } from 'react';
import { employeeService } from '../api';
import { Employee, PaginatedResponse } from '../types';

export const EmployeesPage: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);

  useEffect(() => {
    const loadEmployees = async () => {
      try {
        const data: PaginatedResponse<Employee> = await employeeService.getAllEmployees(page);
        setEmployees(data.content);
      } catch (error) {
        console.error('Failed to load employees:', error);
      } finally {
        setLoading(false);
      }
    };
    loadEmployees();
  }, [page]);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Employees</h1>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Code</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Department</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Location</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {employees.map((emp) => (
              <tr key={emp.id}>
                <td className="px-6 py-4 whitespace-nowrap">{emp.employeeCode}</td>
                <td className="px-6 py-4 whitespace-nowrap">{emp.firstName} {emp.lastName}</td>
                <td className="px-6 py-4 whitespace-nowrap">{emp.departmentName}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 rounded text-xs ${
                    emp.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                  }`}>
                    {emp.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">{emp.location}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
