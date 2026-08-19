# Architecture Documentation

## System Overview

The Analytics Platform is a full-stack application following a layered architecture pattern with clear separation of concerns.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                         Frontend                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │  Pages   │  │Components│  │   API    │  │  Types   │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      API Gateway (Nginx)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                         Backend                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │Controller│  │ Service  │  │Repository│  │  Entity  │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                  │
│  │ Security │  │Exception│  │ Scheduler│                  │
│  └──────────┘  └──────────┘  └──────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      PostgreSQL Database                       │
└─────────────────────────────────────────────────────────────┘
```

## Backend Architecture

### Layered Architecture

The backend follows a classic layered architecture:

1. **Controller Layer** (`controller/`)
   - REST API endpoints
   - Request validation
   - Response formatting
   - Role-based access control

2. **Service Layer** (`service/`)
   - Business logic
   - Transaction management
   - Data transformation
   - Orchestration of repository calls

3. **Repository Layer** (`repository/`)
   - Data access using Spring Data JPA
   - Custom query methods
   - Database interaction abstraction

4. **Entity Layer** (`entity/`)
   - JPA entities representing database tables
   - Relationships and constraints

### Cross-Cutting Concerns

- **Security** (`security/`)
  - JWT token generation and validation
  - Spring Security configuration
  - User authentication and authorization

- **Exception Handling** (`exception/`)
  - Global exception handler
  - Custom exception types
  - Consistent error responses

- **Scheduling** (`scheduler/`)
  - Automated tasks
  - Cron-based execution
  - Automation rule processing

### Data Transfer Objects (DTO)

DTOs separate API contracts from domain entities:
- Request DTOs: Input validation and transformation
- Response DTOs: Output formatting and data hiding

## Frontend Architecture

### Component Structure

```
src/
├── components/     # Reusable UI components
│   ├── Navbar.tsx
│   ├── Sidebar.tsx
│   ├── Modal.tsx
│   └── Layout.tsx
├── pages/         # Page-level components
│   ├── LoginPage.tsx
│   ├── DashboardPage.tsx
│   └── ...
├── api/           # API client and services
│   ├── client.ts
│   ├── authService.ts
│   └── ...
└── types/         # TypeScript type definitions
```

### State Management

- Component-level state using React hooks
- Authentication state in localStorage
- API state managed through service calls

### Routing

- React Router for client-side routing
- Protected routes for authenticated users
- Layout wrapper for authenticated pages

## Database Design

### Schema Overview

- **users**: Application users and authentication
- **departments**: Organizational departments
- **employees**: Employee records
- **operational_records**: Daily operational metrics
- **workflows**: Task and workflow tracking
- **alerts**: System-generated alerts
- **automation_rules**: Automation configuration
- **automation_runs**: Automation execution history

### Key Relationships

- Employees belong to Departments
- Operational Records belong to Employees and Departments
- Workflows belong to Departments and are assigned to Employees/Users
- Alerts are linked to Employees, Departments, Records, or Workflows
- Automation Runs are linked to Automation Rules

### Indexes and Performance

- Foreign key indexes on all relationships
- Composite indexes on frequently queried columns
- Triggers for automatic timestamp updates

## Security Architecture

### Authentication Flow

1. User submits credentials to `/auth/login`
2. Backend validates credentials using UserDetailsService
3. JWT token generated and returned
4. Frontend stores token in localStorage
5. Subsequent requests include token in Authorization header
6. JwtAuthenticationFilter validates token on each request

### Authorization

- Role-based access control (RBAC)
- Roles: ADMIN, MANAGER, ANALYST
- Method-level security annotations
- Endpoint-level security configuration

### Security Features

- Password encoding with BCrypt
- JWT token expiration
- CORS configuration
- SQL injection prevention via JPA

## Automation System

### Automation Types

1. **Productivity Check**: Monitors employee productivity rates
2. **Error Rate Check**: Monitors error rates across operations
3. **Deadline Check**: Monitors workflow deadlines

### Execution Flow

1. Scheduler triggers automation (cron-based)
2. AutomationService processes enabled rules
3. Business logic evaluates thresholds
4. Alerts generated for violations
5. AutomationRun records execution details

## Deployment Architecture

### Docker Compose

- PostgreSQL: Database container
- Backend: Spring Boot application
- Frontend: Nginx serving React build
- Health checks for service dependencies

### CI/CD Pipeline

- Backend: Maven build and test
- Frontend: npm install and test
- Docker image builds
- Deployment on main branch

## Scalability Considerations

### Horizontal Scaling

- Stateless backend design
- Session-less JWT authentication
- Database connection pooling
- Load balancer ready

### Performance Optimization

- Database indexing
- Pagination for large datasets
- Caching strategies (future enhancement)
- Lazy loading for JPA relationships

## Technology Rationale

### Backend: Spring Boot
- Mature ecosystem
- Convention over configuration
- Built-in security
- Easy testing
- Production-ready

### Frontend: React + TypeScript
- Component reusability
- Type safety
- Large ecosystem
- Performance optimization

### Database: PostgreSQL
- ACID compliance
- Advanced features (JSON, indexes)
- Scalability
- Open source
