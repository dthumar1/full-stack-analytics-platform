import React from 'react';
import { authService } from '../api';

export const SettingsPage: React.FC = () => {
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    window.location.href = '/login';
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Settings</h1>
      
      <div className="bg-white p-6 rounded-lg shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">User Profile</h2>
        <div className="space-y-2">
          <p><span className="font-medium">Name:</span> {user?.firstName} {user?.lastName}</p>
          <p><span className="font-medium">Email:</span> {user?.email}</p>
          <p><span className="font-medium">Role:</span> {user?.role}</p>
        </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Application Settings</h2>
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <span>Dark Mode</span>
            <input type="checkbox" className="toggle" />
          </div>
          <div className="flex items-center justify-between">
            <span>Notifications</span>
            <input type="checkbox" className="toggle" defaultChecked />
          </div>
        </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow">
        <h2 className="text-xl font-semibold mb-4">Actions</h2>
        <button
          onClick={handleLogout}
          className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700"
        >
          Logout
        </button>
      </div>
    </div>
  );
};
