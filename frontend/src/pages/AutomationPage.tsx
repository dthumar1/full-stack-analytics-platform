import React, { useEffect, useState } from 'react';
import { automationService } from '../api';
import { AutomationRun, AutomationRule, PaginatedResponse } from '../types';

export const AutomationPage: React.FC = () => {
  const [runs, setRuns] = useState<AutomationRun[]>([]);
  const [rules, setRules] = useState<AutomationRule[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [runsData, rulesData]: [PaginatedResponse<AutomationRun>, PaginatedResponse<AutomationRule>] = await Promise.all([
          automationService.getAutomationRuns(),
          automationService.getAutomationRules()
        ]);
        setRuns(runsData.content);
        setRules(rulesData.content);
      } catch (error) {
        console.error('Failed to load automation data:', error);
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, []);

  const handleToggleRule = async (id: string, enabled: boolean) => {
    try {
      await automationService.updateAutomationRule(id, enabled);
      const rulesData = await automationService.getAutomationRules();
      setRules(rulesData.content);
    } catch (error) {
      console.error('Failed to update rule:', error);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Automation</h1>
      
      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">Automation Rules</h2>
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Threshold</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Enabled</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {rules.map((rule) => (
                <tr key={rule.id}>
                  <td className="px-6 py-4 whitespace-nowrap">{rule.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{rule.ruleType}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{rule.threshold || '-'}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      onClick={() => handleToggleRule(rule.id, !rule.enabled)}
                      className={`px-3 py-1 rounded text-xs ${
                        rule.enabled ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                      }`}
                    >
                      {rule.enabled ? 'Enabled' : 'Disabled'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div>
        <h2 className="text-xl font-semibold mb-4">Recent Runs</h2>
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Start Time</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Records</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Alerts</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {runs.map((run) => (
                <tr key={run.id}>
                  <td className="px-6 py-4 whitespace-nowrap">{run.automationType}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{new Date(run.startTime).toLocaleString()}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{run.recordsProcessed}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{run.alertsCreated}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 rounded text-xs ${
                      run.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                      run.status === 'FAILED' ? 'bg-red-100 text-red-800' :
                      'bg-yellow-100 text-yellow-800'
                    }`}>
                      {run.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
