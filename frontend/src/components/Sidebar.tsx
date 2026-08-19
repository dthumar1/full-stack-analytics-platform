import React from 'react';
import { Link, useLocation } from 'react-router-dom';

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({ isOpen, onClose }) => {
  const location = useLocation();

  const menuItems = [
    { path: '/dashboard', label: 'Dashboard' },
    { path: '/employees', label: 'Employees' },
    { path: '/records', label: 'Operational Records' },
    { path: '/workflows', label: 'Workflows' },
    { path: '/alerts', label: 'Alerts' },
    { path: '/automation', label: 'Automation' },
    { path: '/import', label: 'Import/Export' },
    { path: '/settings', label: 'Settings' },
  ];

  return (
    <>
      {isOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-40" onClick={onClose} />
      )}
      <aside
        className={`fixed left-0 top-0 h-full bg-gray-800 text-white w-64 transform transition-transform duration-300 ease-in-out z-50 ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="p-4">
          <h2 className="text-xl font-bold mb-6">Menu</h2>
          <nav className="space-y-2">
            {menuItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                onClick={onClose}
                className={`block px-4 py-2 rounded transition ${
                  location.pathname === item.path
                    ? 'bg-blue-600'
                    : 'hover:bg-gray-700'
                }`}
              >
                {item.label}
              </Link>
            ))}
          </nav>
        </div>
      </aside>
    </>
  );
};
