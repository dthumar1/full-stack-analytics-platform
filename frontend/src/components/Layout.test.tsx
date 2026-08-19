import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { Layout } from './Layout';
import { MemoryRouter } from 'react-router-dom';

describe('Layout', () => {
  it('renders Navbar with title', () => {
    render(
      <MemoryRouter>
        <Layout title="Test App">
          <div>Test Content</div>
        </Layout>
      </MemoryRouter>
    );

    expect(screen.getByText('Test App')).toBeInTheDocument();
  });

  it('renders children content', () => {
    render(
      <MemoryRouter>
        <Layout title="Test App">
          <div data-testid="child-content">Test Content</div>
        </Layout>
      </MemoryRouter>
    );

    expect(screen.getByTestId('child-content')).toBeInTheDocument();
  });

  it('renders Sidebar', () => {
    render(
      <MemoryRouter>
        <Layout title="Test App">
          <div>Test Content</div>
        </Layout>
      </MemoryRouter>
    );

    expect(screen.getByText('Menu')).toBeInTheDocument();
  });
});
