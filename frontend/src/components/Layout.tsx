import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Navbar } from './Navbar';
import { Sidebar } from './Sidebar';

export const Layout: React.FC = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar title="Analytics Platform" />
      <div className="flex">
        <button
          onClick={() => setSidebarOpen(true)}
          className="fixed top-20 left-4 z-30 bg-blue-600 text-white p-2 rounded shadow hover:bg-blue-700"
        >
          ☰
        </button>
        <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
        <main className="flex-1 p-6 ml-0">
          <Outlet />
        </main>
      </div>
    </div>
  );
};
