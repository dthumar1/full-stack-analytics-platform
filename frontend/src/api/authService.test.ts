import { describe, it, expect, vi, beforeEach } from 'vitest';
import { authService } from './authService';
import axios from 'axios';

vi.mock('axios');

describe('authService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('stores token in localStorage after successful login', async () => {
    const mockResponse = {
      data: {
        token: 'test-jwt-token',
        type: 'Bearer',
        user: {
          id: '1',
          firstName: 'John',
          lastName: 'Doe',
          email: 'john@example.com',
          role: 'ADMIN',
          enabled: true,
          createdAt: '2024-01-01'
        }
      }
    };

    (axios.post as any).mockResolvedValue(mockResponse);

    await authService.login({ email: 'test@example.com', password: 'password' });

    expect(localStorage.getItem('token')).toBe('test-jwt-token');
  });

  it('removes token from localStorage on logout', () => {
    localStorage.setItem('token', 'test-token');
    authService.logout();

    expect(localStorage.getItem('token')).toBeNull();
  });

  it('returns current user from localStorage', () => {
    const mockUser = {
      id: '1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      role: 'ADMIN',
      enabled: true,
      createdAt: '2024-01-01'
    };
    localStorage.setItem('user', JSON.stringify(mockUser));

    const user = authService.getCurrentUser();

    expect(user).toEqual(mockUser);
  });

  it('returns null when no user in localStorage', () => {
    const user = authService.getCurrentUser();
    expect(user).toBeNull();
  });

  it('returns token from localStorage', () => {
    localStorage.setItem('token', 'test-token');
    const token = authService.getToken();
    expect(token).toBe('test-token');
  });

  it('returns null when no token in localStorage', () => {
    const token = authService.getToken();
    expect(token).toBeNull();
  });
});
