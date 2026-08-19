import React from 'react';
import { authService } from '../api';

interface NavbarProps {
  title: string;
}

export const Navbar: React.FC<NavbarProps> = ({ title }) => {
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    window.location.href = '/login';
  };

  return (
    <nav className="bg-blue-600 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center">
            <h1 className="text-xl font-bold">{title}</h1>
          </div>
          <div className="flex items-center space-x-4">
            <span className="text-sm">
              {user?.firstName} {user?.lastName}
            </span>
            <span className="text-xs bg-blue-700 px-2 py-1 rounded">
              {user?.role}
            </span>
            <button
              onClick={handleLogout}
              className="text-sm hover:text-blue-200 transition"
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};
