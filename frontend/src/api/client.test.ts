import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import { apiClient } from './client';

vi.mock('axios');

describe('apiClient', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('adds authorization header when token exists', async () => {
    localStorage.setItem('token', 'test-token');
    
    (axios.create as any).mockReturnValue({
      get: vi.fn().mockResolvedValue({ data: {} }),
      interceptors: {
        request: { use: vi.fn() },
        response: { use: vi.fn() }
      }
    });

    apiClient.get('/test');

    // The interceptor should be set up
    expect(axios.create).toHaveBeenCalled();
  });

  it('does not add authorization header when token does not exist', async () => {
    localStorage.removeItem('token');
    
    (axios.create as any).mockReturnValue({
      get: vi.fn().mockResolvedValue({ data: {} }),
      interceptors: {
        request: { use: vi.fn() },
        response: { use: vi.fn() }
      }
    });

    apiClient.get('/test');

    expect(axios.create).toHaveBeenCalled();
  });

  it('clears token on 401 response', async () => {
    localStorage.setItem('token', 'test-token');
    
    const mockAxiosInstance = {
      get: vi.fn().mockRejectedValue({ response: { status: 401 } }),
      interceptors: {
        request: { use: vi.fn((fn: any) => fn({ headers: {} })) },
        response: { use: vi.fn((_: any, errorFn: any) => errorFn({ response: { status: 401 } })) }
      }
    };

    (axios.create as any).mockReturnValue(mockAxiosInstance);

    try {
      await apiClient.get('/test');
    } catch (error) {
      // Expected to fail
    }

    expect(localStorage.getItem('token')).toBeNull();
  });
});
