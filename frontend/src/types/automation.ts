export interface AutomationRule {
  id: string;
  name: string;
  description: string;
  ruleType: 'LOW_PRODUCTIVITY' | 'HIGH_ERROR_RATE' | 'WORKFLOW_DEADLINE';
  threshold: number;
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface AutomationRun {
  id: string;
  automationRuleId: string;
  automationRuleName: string;
  automationType: 'PRODUCTIVITY_CHECK' | 'ERROR_RATE_CHECK' | 'DEADLINE_CHECK' | 'BATCH_PROCESSING';
  startTime: string;
  endTime: string;
  recordsProcessed: number;
  alertsCreated: number;
  status: 'STARTED' | 'COMPLETED' | 'FAILED';
  errorMessage: string;
}
