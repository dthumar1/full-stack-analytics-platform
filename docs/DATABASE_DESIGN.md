# Database Design Documentation

## Overview

The Analytics Platform uses PostgreSQL 15 as its relational database. The schema is designed to support employee management, operational tracking, workflow management, and automation features.

## Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────┐
│  departments│◄──────│  employees  │
└─────────────┘       └──────┬──────┘
                             │
                             │
                             ▼
                    ┌────────────────┐
                    │operational_records│
                    └────────────────┘
                             │
                             │
                             ▼
                    ┌────────────────┐
                    │     alerts     │
                    └────────────────┘

┌─────────────┐       ┌─────────────┐
│    users    │◄──────│  workflows  │
└─────────────┘       └──────┬──────┘
                             │
                             │
                             ▼
                    ┌────────────────┐
                    │     alerts     │
                    └────────────────┘

┌──────────────────────┐       ┌──────────────────────┐
│   automation_rules   │◄──────│   automation_runs    │
└──────────────────────┘       └──────────────────────┘
```

## Tables

### users

Application users for authentication and authorization.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| first_name | VARCHAR(100) | NOT NULL | User first name |
| last_name | VARCHAR(100) | NOT NULL | User last name |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User email |
| password | VARCHAR(255) | NOT NULL | Encrypted password |
| role | VARCHAR(20) | NOT NULL | User role (ADMIN, MANAGER, ANALYST) |
| enabled | BOOLEAN | DEFAULT TRUE | Account status |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_users_email` on `email`
- `idx_users_role` on `role`

### departments

Organizational departments.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Department name |
| description | TEXT | | Department description |
| active | BOOLEAN | DEFAULT TRUE | Active status |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_departments_name` on `name`
- `idx_departments_active` on `active`

### employees

Employee information and assignments.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| employee_code | VARCHAR(20) | UNIQUE, NOT NULL | Employee code |
| first_name | VARCHAR(100) | NOT NULL | First name |
| last_name | VARCHAR(100) | NOT NULL | Last name |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email |
| department_id | UUID | FK → departments.id | Department assignment |
| job_title | VARCHAR(100) | NOT NULL | Job title |
| location | VARCHAR(100) | NOT NULL | Work location |
| shift | VARCHAR(20) | NOT NULL | Shift (DAY, NIGHT, FLEX) |
| status | VARCHAR(20) | NOT NULL | Status (ACTIVE, INACTIVE, LEAVE) |
| hire_date | DATE | NOT NULL | Hire date |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_employee_code` on `employee_code`
- `idx_employee_email` on `email`
- `idx_employee_department` on `department_id`
- `idx_employee_status` on `status`
- `idx_employee_location` on `location`
- `idx_employee_shift` on `shift`

### operational_records

Daily operational metrics for employees.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| employee_id | UUID | FK → employees.id | Employee |
| department_id | UUID | FK → departments.id | Department |
| process_type | VARCHAR(50) | NOT NULL | Process type |
| units_processed | INTEGER | NOT NULL, >= 0 | Units processed |
| hours_worked | DECIMAL(5,2) | NOT NULL, > 0 | Hours worked |
| productivity_rate | DECIMAL(10,2) | COMPUTED | Units per hour |
| errors | INTEGER | DEFAULT 0, >= 0 | Error count |
| error_rate | DECIMAL(5,2) | COMPUTED | Errors per unit |
| status | VARCHAR(20) | NOT NULL | Record status |
| location | VARCHAR(100) | NOT NULL | Work location |
| work_date | DATE | NOT NULL | Work date |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_records_employee` on `employee_id`
- `idx_records_department` on `department_id`
- `idx_records_date` on `work_date`
- `idx_records_status` on `status`
- `idx_records_location` on `location`
- `idx_records_process` on `process_type`
- Composite: `idx_records_employee_date` on `(employee_id, work_date)`

### workflows

Task and workflow management.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| title | VARCHAR(200) | NOT NULL | Workflow title |
| description | TEXT | | Description |
| department_id | UUID | FK → departments.id | Department |
| assigned_employee_id | UUID | FK → employees.id | Assigned employee |
| assigned_user_id | UUID | FK → users.id | Assigned user |
| priority | VARCHAR(20) | NOT NULL | Priority level |
| status | VARCHAR(20) | NOT NULL | Workflow status |
| due_date | DATE | | Due date |
| completed_date | TIMESTAMP | | Completion date |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_workflows_department` on `department_id`
- `idx_workflows_employee` on `assigned_employee_id`
- `idx_workflows_user` on `assigned_user_id`
- `idx_workflows_status` on `status`
- `idx_workflows_priority` on `priority`
- `idx_workflows_due_date` on `due_date`

### alerts

System-generated alerts for various conditions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| alert_type | VARCHAR(50) | NOT NULL | Alert type |
| severity | VARCHAR(20) | NOT NULL | Severity level |
| message | TEXT | NOT NULL | Alert message |
| employee_id | UUID | FK → employees.id | Related employee |
| department_id | UUID | FK → departments.id | Related department |
| operational_record_id | UUID | FK → operational_records.id | Related record |
| workflow_id | UUID | FK → workflows.id | Related workflow |
| resolved | BOOLEAN | DEFAULT FALSE | Resolution status |
| resolved_by | UUID | FK → users.id | Resolved by user |
| resolved_at | TIMESTAMP | | Resolution timestamp |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |

**Indexes:**
- `idx_alerts_employee` on `employee_id`
- `idx_alerts_department` on `department_id`
- `idx_alerts_type` on `alert_type`
- `idx_alerts_severity` on `severity`
- `idx_alerts_resolved` on `resolved`
- `idx_alerts_created` on `created_at`

### automation_rules

Configuration for automation checks.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(200) | NOT NULL | Rule name |
| description | TEXT | | Description |
| rule_type | VARCHAR(50) | NOT NULL | Rule type |
| threshold | DECIMAL(10,2) | | Threshold value |
| enabled | BOOLEAN | DEFAULT TRUE | Enabled status |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_rules_type` on `rule_type`
- `idx_rules_enabled` on `enabled`

### automation_runs

History of automation executions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| automation_rule_id | UUID | FK → automation_rules.id | Related rule |
| automation_type | VARCHAR(50) | NOT NULL | Automation type |
| start_time | TIMESTAMP | NOT NULL | Start time |
| end_time | TIMESTAMP | | End time |
| records_processed | INTEGER | DEFAULT 0 | Records processed |
| alerts_created | INTEGER | DEFAULT 0 | Alerts generated |
| status | VARCHAR(20) | NOT NULL | Execution status |
| error_message | TEXT | | Error details |

**Indexes:**
- `idx_runs_type` on `automation_type`
- `idx_runs_status` on `status`
- `idx_runs_start_time` on `start_time`

## Triggers

### update_timestamp_trigger

Automatically updates `updated_at` timestamp on row modification for all tables that have this column.

## Data Integrity

### Foreign Key Constraints

All foreign key relationships are enforced with:
- `ON DELETE RESTRICT` - Prevents deletion of referenced records
- `ON UPDATE CASCADE` - Propagates ID updates

### Check Constraints

- `units_processed >= 0`
- `hours_worked > 0`
- `errors >= 0`

## Performance Considerations

### Indexing Strategy

- All foreign keys indexed
- Frequently filtered columns indexed
- Composite indexes for common query patterns
- Date range queries indexed

### Query Optimization

- Use indexed columns in WHERE clauses
- Limit result sets with pagination
- Avoid SELECT * in production queries
- Use JOINs efficiently with proper indexes

## Migration Strategy

Schema changes should be managed through:
1. Versioned migration scripts
2. Rollback procedures
3. Data backup before migrations
4. Testing in staging environment
