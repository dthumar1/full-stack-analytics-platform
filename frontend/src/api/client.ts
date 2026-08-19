import axios, { AxiosInstance, AxiosError } from 'axios';
import { ErrorResponse } from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor to add auth token
    this.client.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor to handle errors
    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError<ErrorResponse>) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  public get<T = any>(url: string, params?: any) {
    return this.client.get<T>(url, { params });
  }

  public post<T = any>(url: string, data?: any) {
    return this.client.post<T>(url, data);
  }

  public put<T = any>(url: string, data?: any) {
    return this.client.put<T>(url, data);
  }

  public patch<T = any>(url: string, data?: any) {
    return this.client.patch<T>(url, data);
  }

  public delete<T = any>(url: string) {
    return this.client.delete<T>(url);
  }
}

export const apiClient = new ApiClient();
