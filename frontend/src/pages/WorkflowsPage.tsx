import React, { useEffect, useState } from 'react';
import { workflowService } from '../api';
import { Workflow, PaginatedResponse } from '../types';

export const WorkflowsPage: React.FC = () => {
  const [workflows, setWorkflows] = useState<Workflow[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadWorkflows = async () => {
      try {
        const data: PaginatedResponse<Workflow> = await workflowService.getAllWorkflows();
        setWorkflows(data.content);
      } catch (error) {
        console.error('Failed to load workflows:', error);
      } finally {
        setLoading(false);
      }
    };
    loadWorkflows();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Workflows</h1>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Title</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Department</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Assigned To</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Priority</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Due Date</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {workflows.map((workflow) => (
              <tr key={workflow.id}>
                <td className="px-6 py-4 whitespace-nowrap">{workflow.title}</td>
                <td className="px-6 py-4 whitespace-nowrap">{workflow.departmentName}</td>
                <td className="px-6 py-4 whitespace-nowrap">{workflow.assignedEmployeeName || workflow.assignedUserName || '-'}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 rounded text-xs ${
                    workflow.priority === 'CRITICAL' ? 'bg-red-100 text-red-800' :
                    workflow.priority === 'HIGH' ? 'bg-orange-100 text-orange-800' :
                    workflow.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {workflow.priority}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 rounded text-xs ${
                    workflow.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                    workflow.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
                    workflow.status === 'BLOCKED' ? 'bg-red-100 text-red-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {workflow.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">{workflow.dueDate || '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
