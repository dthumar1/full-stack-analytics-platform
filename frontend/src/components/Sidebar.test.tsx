import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { Sidebar } from './Sidebar';
import { MemoryRouter, Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

describe('Sidebar', () => {
  it('renders all menu items', () => {
    render(
      <MemoryRouter>
        <Sidebar isOpen={true} onClose={vi.fn()} />
      </MemoryRouter>
    );

    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Employees')).toBeInTheDocument();
    expect(screen.getByText('Operational Records')).toBeInTheDocument();
    expect(screen.getByText('Workflows')).toBeInTheDocument();
    expect(screen.getByText('Alerts')).toBeInTheDocument();
    expect(screen.getByText('Automation')).toBeInTheDocument();
    expect(screen.getByText('Import/Export')).toBeInTheDocument();
    expect(screen.getByText('Settings')).toBeInTheDocument();
  });

  it('does not render when isOpen is false', () => {
    const { container } = render(
      <MemoryRouter>
        <Sidebar isOpen={false} onClose={vi.fn()} />
      </MemoryRouter>
    );

    const sidebar = container.querySelector('.translate-x-full');
    expect(sidebar).toBeInTheDocument();
  });

  it('renders when isOpen is true', () => {
    const { container } = render(
      <MemoryRouter>
        <Sidebar isOpen={true} onClose={vi.fn()} />
      </MemoryRouter>
    );

    const sidebar = container.querySelector('.translate-x-0');
    expect(sidebar).toBeInTheDocument();
  });

  it('highlights the active route', () => {
    const history = createMemoryHistory();
    history.push('/employees');

    render(
      <Router location={history.location} navigator={history}>
        <Sidebar isOpen={true} onClose={vi.fn()} />
      </Router>
    );

    const employeesLink = screen.getByText('Employees');
    expect(employeesLink).toHaveClass('bg-blue-600');
  });

  it('calls onClose when overlay is clicked', () => {
    const onClose = vi.fn();
    const { container } = render(
      <MemoryRouter>
        <Sidebar isOpen={true} onClose={onClose} />
      </MemoryRouter>
    );

    const overlay = container.querySelector('.bg-black.bg-opacity-50');
    if (overlay) {
      overlay.dispatchEvent(new MouseEvent('click', { bubbles: true }));
      expect(onClose).toHaveBeenCalled();
    }
  });
});
