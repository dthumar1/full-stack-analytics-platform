# API Documentation

## Base URL
`http://localhost:8080/api`

## Authentication

All endpoints except `/auth/login` require a JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Endpoints

### Authentication

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@analytics.local",
  "password": "Admin123!"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiration": 1700000000000,
  "user": {
    "id": "uuid",
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@analytics.local",
    "role": "ADMIN",
    "enabled": true,
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

### Employees

#### Get All Employees
```http
GET /employees?page=0&size=20&sort=lastName,asc
```

#### Search Employees
```http
GET /employees/search?search=john&departmentId=uuid&status=ACTIVE
```

#### Create Employee
```http
POST /employees
Content-Type: application/json

{
  "employeeCode": "EMP001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "departmentId": "uuid",
  "jobTitle": "Analyst",
  "location": "New York",
  "shift": "DAY",
  "status": "ACTIVE",
  "hireDate": "2024-01-01"
}
```

#### Update Employee
```http
PUT /employees/{id}
Content-Type: application/json
```

#### Delete Employee
```http
DELETE /employees/{id}
```

### Operational Records

#### Get All Records
```http
GET /operational-records?page=0&size=20&sort=workDate,desc
```

#### Filter Records
```http
GET /operational-records/filter?employeeId=uuid&departmentId=uuid&startDate=2024-01-01&endDate=2024-12-31
```

#### Create Record
```http
POST /operational-records
Content-Type: application/json

{
  "employeeId": "uuid",
  "departmentId": "uuid",
  "processType": "PICKING",
  "unitsProcessed": 100,
  "hoursWorked": "8.0",
  "errors": 2,
  "status": "VALID",
  "location": "Warehouse A",
  "workDate": "2024-01-01"
}
```

### Workflows

#### Get All Workflows
```http
GET /workflows?page=0&size=20&sort=createdAt,desc
```

#### Filter Workflows
```http
GET /workflows/filter?departmentId=uuid&status=IN_PROGRESS&priority=HIGH
```

#### Create Workflow
```http
POST /workflows
Content-Type: application/json

{
  "title": "Monthly Report",
  "description": "Generate monthly analytics report",
  "departmentId": "uuid",
  "assignedEmployeeId": "uuid",
  "assignedUserId": "uuid",
  "priority": "HIGH",
  "status": "NEW",
  "dueDate": "2024-12-31"
}
```

#### Update Workflow Status
```http
PATCH /workflows/{id}/status
Content-Type: application/json

{
  "status": "COMPLETED",
  "assignedUserId": "uuid"
}
```

### Alerts

#### Get Alerts
```http
GET /alerts?employeeId=uuid&departmentId=uuid&type=LOW_PRODUCTIVITY&severity=HIGH&resolved=false
```

#### Resolve Alert
```http
PATCH /alerts/{id}/resolve?resolvedByUserId=uuid
```

### Analytics

#### Dashboard Summary
```http
GET /analytics/summary?startDate=2024-01-01&endDate=2024-12-31&departmentId=uuid
```

**Response:**
```json
{
  "totalEmployees": 100,
  "activeEmployees": 95,
  "totalRecords": 10000,
  "unitsProcessed": 500000,
  "averageProductivity": 50.0,
  "totalErrors": 500,
  "averageErrorRate": 0.1,
  "openWorkflows": 25,
  "criticalAlerts": 5,
  "automationRuns": 100
}
```

#### Productivity Trend
```http
GET /analytics/productivity-trend?startDate=2024-01-01&endDate=2024-12-31&groupBy=day
```

#### Department Performance
```http
GET /analytics/department-performance?startDate=2024-01-01&endDate=2024-12-31
```

#### Error Trend
```http
GET /analytics/error-trend?startDate=2024-01-01&endDate=2024-12-31
```

#### Top Performers
```http
GET /analytics/top-performers?startDate=2024-01-01&endDate=2024-12-31&limit=10
```

### Automation

#### Get Automation Runs
```http
GET /automation/runs?automationType=PRODUCTIVITY_CHECK&status=COMPLETED
```

#### Get Automation Rules
```http
GET /automation/rules?page=0&size=20
```

#### Update Automation Rule
```http
PUT /automation/rules/{id}?enabled=true
```

#### Trigger Automation
```http
POST /automation/run?automationType=PRODUCTIVITY_CHECK
```

### Departments

#### Get All Departments
```http
GET /departments
```

#### Create Department
```http
POST /departments?name=Operations&description=Operations%20Department
```

#### Update Department
```http
PUT /departments/{id}?name=Operations&description=Updated%20Description&active=true
```

## Error Responses

All endpoints may return error responses in the following format:

```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: uuid",
  "path": "/api/employees/uuid"
}
```

## Pagination

Paginated endpoints return:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false
}
```
