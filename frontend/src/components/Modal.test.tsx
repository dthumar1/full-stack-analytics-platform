import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { Modal } from './Modal';

describe('Modal', () => {
  it('renders when isOpen is true', () => {
    render(
      <Modal isOpen={true} onClose={vi.fn()} title="Test Modal">
        <p>Modal content</p>
      </Modal>
    );

    expect(screen.getByText('Test Modal')).toBeInTheDocument();
    expect(screen.getByText('Modal content')).toBeInTheDocument();
  });

  it('does not render when isOpen is false', () => {
    const { container } = render(
      <Modal isOpen={false} onClose={vi.fn()} title="Test Modal">
        <p>Modal content</p>
      </Modal>
    );

    expect(container.firstChild).toBeNull();
  });

  it('calls onClose when close button is clicked', () => {
    const onClose = vi.fn();
    render(
      <Modal isOpen={true} onClose={onClose} title="Test Modal">
        <p>Modal content</p>
      </Modal>
    );

    const closeButton = screen.getByText('×');
    fireEvent.click(closeButton);
    expect(onClose).toHaveBeenCalled();
  });

  it('renders children content', () => {
    render(
      <Modal isOpen={true} onClose={vi.fn()} title="Test Modal">
        <div data-testid="child-content">Child Content</div>
      </Modal>
    );

    expect(screen.getByTestId('child-content')).toBeInTheDocument();
  });

  it('applies correct CSS classes', () => {
    const { container } = render(
      <Modal isOpen={true} onClose={vi.fn()} title="Test Modal">
        <p>Modal content</p>
      </Modal>
    );

    const modalContainer = container.querySelector('.fixed.inset-0');
    expect(modalContainer).toBeInTheDocument();

    const modalContent = container.querySelector('.bg-white.rounded-lg');
    expect(modalContent).toBeInTheDocument();
  });
});
