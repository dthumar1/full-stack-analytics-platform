export interface DashboardSummary {
  totalEmployees: number;
  activeEmployees: number;
  totalRecords: number;
  unitsProcessed: number;
  averageProductivity: number;
  totalErrors: number;
  averageErrorRate: number;
  openWorkflows: number;
  criticalAlerts: number;
  automationRuns: number;
}

export interface AnalyticsData {
  metric: string;
  groupBy: string;
  data: Array<Record<string, any>>;
  summary: Record<string, any>;
}
