export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'MANAGER' | 'ANALYST';
  enabled: boolean;
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  expiration: number;
  user: User;
}
