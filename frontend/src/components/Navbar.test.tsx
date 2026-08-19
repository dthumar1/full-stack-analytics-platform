import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { Navbar } from './Navbar';
import * as authService from '../api/authService';

describe('Navbar', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders the title correctly', () => {
    render(<Navbar title="Test App" />);
    expect(screen.getByText('Test App')).toBeInTheDocument();
  });

  it('displays user information when user is logged in', () => {
    vi.spyOn(authService, 'getCurrentUser').mockReturnValue({
      id: '1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      role: 'ADMIN',
      enabled: true,
      createdAt: '2024-01-01'
    });

    render(<Navbar title="Test App" />);
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('ADMIN')).toBeInTheDocument();
  });

  it('displays empty user info when no user is logged in', () => {
    vi.spyOn(authService, 'getCurrentUser').mockReturnValue(null);

    render(<Navbar title="Test App" />);
    expect(screen.queryByText(/John/)).not.toBeInTheDocument();
  });

  it('calls logout and redirects when logout button is clicked', () => {
    vi.spyOn(authService, 'getCurrentUser').mockReturnValue({
      id: '1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      role: 'ADMIN',
      enabled: true,
      createdAt: '2024-01-01'
    });

    const logoutSpy = vi.spyOn(authService, 'logout').mockImplementation(() => {});
    delete (window as any).location;
    (window as any).location = { href: '' };

    render(<Navbar title="Test App" />);
    const logoutButton = screen.getByText('Logout');
    fireEvent.click(logoutButton);

    expect(logoutSpy).toHaveBeenCalled();
    expect(window.location.href).toBe('/login');
  });
});
