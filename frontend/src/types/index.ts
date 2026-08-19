export * from './user';
export * from './employee';
export * from './department';
export * from './operationalRecord';
export * from './workflow';
export * from './alert';
export * from './analytics';
export * from './automation';

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}
