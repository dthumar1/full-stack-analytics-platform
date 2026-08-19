import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { authService } from './api';
import { Layout } from './components/Layout';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { EmployeesPage } from './pages/EmployeesPage';
import { RecordsPage } from './pages/RecordsPage';
import { WorkflowsPage } from './pages/WorkflowsPage';
import { AlertsPage } from './pages/AlertsPage';
import { AutomationPage } from './pages/AutomationPage';
import { ImportPage } from './pages/ImportPage';
import { SettingsPage } from './pages/SettingsPage';

function App() {
  const isAuthenticated = authService.isAuthenticated();

  if (!isAuthenticated) {
    return (
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  return (
    <Layout>
      <Routes>
        <Route path="/login" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/employees" element={<EmployeesPage />} />
        <Route path="/records" element={<RecordsPage />} />
        <Route path="/workflows" element={<WorkflowsPage />} />
        <Route path="/alerts" element={<AlertsPage />} />
        <Route path="/automation" element={<AutomationPage />} />
        <Route path="/import" element={<ImportPage />} />
        <Route path="/settings" element={<SettingsPage />} />
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Layout>
  );
}

export default App;
