-- Full Stack Analytics & Automation Platform Database Schema
-- PostgreSQL Schema

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Departments Table
CREATE TABLE IF NOT EXISTS departments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for departments
CREATE INDEX IF NOT EXISTS idx_departments_name ON departments(name);

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'MANAGER', 'ANALYST')),
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for users
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Employees Table
CREATE TABLE IF NOT EXISTS employees (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    department_id UUID NOT NULL REFERENCES departments(id) ON DELETE RESTRICT,
    job_title VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    shift VARCHAR(20) NOT NULL CHECK (shift IN ('DAY', 'NIGHT', 'FLEX')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'LEAVE')),
    hire_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for employees
CREATE INDEX IF NOT EXISTS idx_employees_code ON employees(employee_code);
CREATE INDEX IF NOT EXISTS idx_employees_email ON employees(email);
CREATE INDEX IF NOT EXISTS idx_employees_department ON employees(department_id);
CREATE INDEX IF NOT EXISTS idx_employees_status ON employees(status);
CREATE INDEX IF NOT EXISTS idx_employees_location ON employees(location);
CREATE INDEX IF NOT EXISTS idx_employees_department_status ON employees(department_id, status);

-- Operational Records Table
CREATE TABLE IF NOT EXISTS operational_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    employee_id UUID NOT NULL REFERENCES employees(id) ON DELETE RESTRICT,
    department_id UUID NOT NULL REFERENCES departments(id) ON DELETE RESTRICT,
    process_type VARCHAR(50) NOT NULL CHECK (process_type IN ('RECEIVING', 'STOCKING', 'PICKING', 'PACKING', 'SHIPPING')),
    units_processed INTEGER NOT NULL CHECK (units_processed > 0),
    hours_worked DECIMAL(10, 2) NOT NULL CHECK (hours_worked > 0),
    productivity_rate DECIMAL(10, 2),
    errors INTEGER NOT NULL DEFAULT 0 CHECK (errors >= 0),
    error_rate DECIMAL(5, 2),
    status VARCHAR(20) NOT NULL DEFAULT 'VALID' CHECK (status IN ('VALID', 'FLAGGED', 'UNDER_REVIEW')),
    location VARCHAR(100) NOT NULL,
    work_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for operational_records
CREATE INDEX IF NOT EXISTS idx_records_employee ON operational_records(employee_id);
CREATE INDEX IF NOT EXISTS idx_records_department ON operational_records(department_id);
CREATE INDEX IF NOT EXISTS idx_records_work_date ON operational_records(work_date);
CREATE INDEX IF NOT EXISTS idx_records_status ON operational_records(status);
CREATE INDEX IF NOT EXISTS idx_records_location ON operational_records(location);
CREATE INDEX IF NOT EXISTS idx_records_employee_date ON operational_records(employee_id, work_date);
CREATE INDEX IF NOT EXISTS idx_records_department_date ON operational_records(department_id, work_date);

-- Workflows Table
CREATE TABLE IF NOT EXISTS workflows (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    department_id UUID NOT NULL REFERENCES departments(id) ON DELETE RESTRICT,
    assigned_employee_id UUID REFERENCES employees(id) ON DELETE SET NULL,
    assigned_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    status VARCHAR(20) NOT NULL DEFAULT 'NEW' CHECK (status IN ('NEW', 'IN_PROGRESS', 'BLOCKED', 'COMPLETED', 'CANCELLED')),
    due_date DATE,
    completed_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for workflows
CREATE INDEX IF NOT EXISTS idx_workflows_department ON workflows(department_id);
CREATE INDEX IF NOT EXISTS idx_workflows_assigned_employee ON workflows(assigned_employee_id);
CREATE INDEX IF NOT EXISTS idx_workflows_assigned_user ON workflows(assigned_user_id);
CREATE INDEX IF NOT EXISTS idx_workflows_status ON workflows(status);
CREATE INDEX IF NOT EXISTS idx_workflows_priority ON workflows(priority);
CREATE INDEX IF NOT EXISTS idx_workflows_due_date ON workflows(due_date);
CREATE INDEX IF NOT EXISTS idx_workflows_status_due_date ON workflows(status, due_date);

-- Alerts Table
CREATE TABLE IF NOT EXISTS alerts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    alert_type VARCHAR(50) NOT NULL CHECK (alert_type IN ('LOW_PRODUCTIVITY', 'HIGH_ERROR_RATE', 'DEADLINE_APPROACHING', 'WORKFLOW_OVERDUE', 'DATA_ANOMALY')),
    severity VARCHAR(20) NOT NULL CHECK (severity IN ('INFO', 'WARNING', 'HIGH', 'CRITICAL')),
    message TEXT NOT NULL,
    employee_id UUID REFERENCES employees(id) ON DELETE SET NULL,
    department_id UUID REFERENCES departments(id) ON DELETE SET NULL,
    operational_record_id UUID REFERENCES operational_records(id) ON DELETE SET NULL,
    workflow_id UUID REFERENCES workflows(id) ON DELETE SET NULL,
    resolved BOOLEAN NOT NULL DEFAULT false,
    resolved_by_id UUID REFERENCES users(id) ON DELETE SET NULL,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for alerts
CREATE INDEX IF NOT EXISTS idx_alerts_employee ON alerts(employee_id);
CREATE INDEX IF NOT EXISTS idx_alerts_department ON alerts(department_id);
CREATE INDEX IF NOT EXISTS idx_alerts_type ON alerts(alert_type);
CREATE INDEX IF NOT EXISTS idx_alerts_severity ON alerts(severity);
CREATE INDEX IF NOT EXISTS idx_alerts_resolved ON alerts(resolved);
CREATE INDEX IF NOT EXISTS idx_alerts_resolved_created ON alerts(resolved, created_at);

-- Automation Rules Table
CREATE TABLE IF NOT EXISTS automation_rules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    rule_type VARCHAR(50) NOT NULL CHECK (rule_type IN ('LOW_PRODUCTIVITY', 'HIGH_ERROR_RATE', 'WORKFLOW_DEADLINE')),
    threshold DECIMAL(10, 2),
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for automation_rules
CREATE INDEX IF NOT EXISTS idx_automation_rules_type ON automation_rules(rule_type);
CREATE INDEX IF NOT EXISTS idx_automation_rules_enabled ON automation_rules(enabled);

-- Automation Runs Table
CREATE TABLE IF NOT EXISTS automation_runs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    automation_rule_id UUID REFERENCES automation_rules(id) ON DELETE SET NULL,
    automation_type VARCHAR(50) NOT NULL CHECK (automation_type IN ('PRODUCTIVITY_CHECK', 'ERROR_RATE_CHECK', 'DEADLINE_CHECK', 'BATCH_PROCESSING')),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    records_processed INTEGER NOT NULL DEFAULT 0,
    alerts_created INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'STARTED' CHECK (status IN ('STARTED', 'COMPLETED', 'FAILED')),
    error_message TEXT
);

-- Create indexes for automation_runs
CREATE INDEX IF NOT EXISTS idx_automation_runs_rule ON automation_runs(automation_rule_id);
CREATE INDEX IF NOT EXISTS idx_automation_runs_type ON automation_runs(automation_type);
CREATE INDEX IF NOT EXISTS idx_automation_runs_start_time ON automation_runs(start_time);
CREATE INDEX IF NOT EXISTS idx_automation_runs_status ON automation_runs(status);
CREATE INDEX IF NOT EXISTS idx_automation_runs_type_start_time ON automation_runs(automation_type, start_time);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_departments_updated_at BEFORE UPDATE ON departments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_employees_updated_at BEFORE UPDATE ON employees
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_operational_records_updated_at BEFORE UPDATE ON operational_records
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_workflows_updated_at BEFORE UPDATE ON workflows
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_automation_rules_updated_at BEFORE UPDATE ON automation_rules
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
