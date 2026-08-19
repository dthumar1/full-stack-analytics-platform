-- Sample Data for Full Stack Analytics & Automation Platform
-- This script inserts fictional operational data for demonstration purposes

-- Insert Departments
INSERT INTO departments (name, description, active) VALUES
('Receiving', 'Handles incoming shipments and inventory receiving', true),
('Inventory', 'Manages stock levels and inventory control', true),
('Picking', 'Order picking and fulfillment operations', true),
('Packing', 'Packaging operations for shipment', true),
('Shipping', 'Final shipping and delivery coordination', true)
ON CONFLICT (name) DO NOTHING;

-- Insert Users (passwords are BCrypt encoded)
-- Admin: admin@analytics.local / Admin123!
-- Manager: manager@analytics.local / Manager123!
-- Analyst: analyst@analytics.local / Analyst123!
INSERT INTO users (first_name, last_name, email, password, role, enabled) VALUES
('Admin', 'User', 'admin@analytics.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', true),
('Manager', 'User', 'manager@analytics.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MANAGER', true),
('Analyst', 'User', 'analyst@analytics.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ANALYST', true)
ON CONFLICT (email) DO NOTHING;

-- Get department IDs for foreign key references
DO $$
DECLARE
    receiving_id UUID;
    inventory_id UUID;
    picking_id UUID;
    packing_id UUID;
    shipping_id UUID;
BEGIN
    SELECT id INTO receiving_id FROM departments WHERE name = 'Receiving';
    SELECT id INTO inventory_id FROM departments WHERE name = 'Inventory';
    SELECT id INTO picking_id FROM departments WHERE name = 'Picking';
    SELECT id INTO packing_id FROM departments WHERE name = 'Packing';
    SELECT id INTO shipping_id FROM departments WHERE name = 'Shipping';
    
    -- Insert Employees
    INSERT INTO employees (employee_code, first_name, last_name, email, department_id, job_title, location, shift, status, hire_date) VALUES
    ('EMP001', 'John', 'Smith', 'john.smith@company.com', receiving_id, 'Receiving Associate', 'Warehouse A', 'DAY', 'ACTIVE', '2023-01-15'),
    ('EMP002', 'Jane', 'Doe', 'jane.doe@company.com', inventory_id, 'Inventory Specialist', 'Warehouse A', 'DAY', 'ACTIVE', '2023-02-20'),
    ('EMP003', 'Mike', 'Johnson', 'mike.johnson@company.com', picking_id, 'Picker', 'Warehouse B', 'NIGHT', 'ACTIVE', '2023-03-10'),
    ('EMP004', 'Sarah', 'Williams', 'sarah.williams@company.com', packing_id, 'Packer', 'Warehouse B', 'DAY', 'ACTIVE', '2023-04-05'),
    ('EMP005', 'David', 'Brown', 'david.brown@company.com', shipping_id, 'Shipping Coordinator', 'Warehouse A', 'DAY', 'ACTIVE', '2023-05-12'),
    ('EMP006', 'Emily', 'Davis', 'emily.davis@company.com', receiving_id, 'Receiving Lead', 'Warehouse A', 'DAY', 'ACTIVE', '2022-11-20'),
    ('EMP007', 'Robert', 'Miller', 'robert.miller@company.com', inventory_id, 'Inventory Manager', 'Warehouse B', 'NIGHT', 'ACTIVE', '2022-12-15'),
    ('EMP008', 'Lisa', 'Wilson', 'lisa.wilson@company.com', picking_id, 'Picker', 'Warehouse B', 'FLEX', 'ACTIVE', '2023-01-30'),
    ('EMP009', 'James', 'Taylor', 'james.taylor@company.com', packing_id, 'Packer', 'Warehouse A', 'NIGHT', 'ACTIVE', '2023-02-28'),
    ('EMP010', 'Jennifer', 'Anderson', 'jennifer.anderson@company.com', shipping_id, 'Shipping Associate', 'Warehouse B', 'DAY', 'ACTIVE', '2023-03-25'),
    ('EMP011', 'Daniel', 'Thomas', 'daniel.thomas@company.com', receiving_id, 'Receiving Associate', 'Warehouse B', 'DAY', 'ACTIVE', '2023-06-01'),
    ('EMP012', 'Michelle', 'Jackson', 'michelle.jackson@company.com', inventory_id, 'Inventory Specialist', 'Warehouse A', 'NIGHT', 'ACTIVE', '2023-06-15'),
    ('EMP013', 'Christopher', 'White', 'christopher.white@company.com', picking_id, 'Picker', 'Warehouse A', 'DAY', 'ACTIVE', '2023-07-20'),
    ('EMP014', 'Amanda', 'Harris', 'amanda.harris@company.com', packing_id, 'Packer', 'Warehouse B', 'DAY', 'ACTIVE', '2023-08-10'),
    ('EMP015', 'Matthew', 'Martin', 'matthew.martin@company.com', shipping_id, 'Shipping Coordinator', 'Warehouse B', 'NIGHT', 'ACTIVE', '2023-09-05')
    ON CONFLICT (employee_code) DO NOTHING;
END $$;

-- Insert Operational Records for the last 30 days
DO $$
DECLARE
    employee_ids UUID[];
    department_ids UUID[];
    process_types TEXT[] := ARRAY['RECEIVING', 'STOCKING', 'PICKING', 'PACKING', 'SHIPPING'];
    locations TEXT[] := ARRAY['Warehouse A', 'Warehouse B'];
    current_date DATE := CURRENT_DATE;
    i INTEGER;
    emp_index INTEGER;
    dept_index INTEGER;
    process_index INTEGER;
    location_index INTEGER;
    units INTEGER;
    hours DECIMAL;
    errors INTEGER;
BEGIN
    -- Get employee IDs
    SELECT ARRAY_AGG(id) INTO employee_ids FROM employees LIMIT 15;
    
    -- Get department IDs
    SELECT ARRAY_AGG(id) INTO department_ids FROM departments;
    
    -- Insert sample records for each employee for the last 30 days
    FOR day_offset IN 0..29 LOOP
        current_date := CURRENT_DATE - (day_offset || ' days')::INTERVAL;
        
        FOR i IN 1..15 LOOP
            emp_index := (i - 1) % array_length(employee_ids, 1) + 1;
            dept_index := (i - 1) % array_length(department_ids, 1) + 1;
            process_index := (i - 1) % array_length(process_types, 1) + 1;
            location_index := (i - 1) % array_length(locations, 1) + 1;
            
            -- Generate realistic values
            units := 50 + (random() * 150)::INTEGER; -- 50-200 units
            hours := (6 + (random() * 4))::DECIMAL(10,2); -- 6-10 hours
            errors := (random() * 5)::INTEGER; -- 0-5 errors
            
            INSERT INTO operational_records (
                employee_id, department_id, process_type, units_processed, 
                hours_worked, errors, location, work_date, status
            ) VALUES (
                employee_ids[emp_index],
                department_ids[dept_index],
                process_types[process_index],
                units,
                hours,
                errors,
                locations[location_index],
                current_date,
                'VALID'
            );
        END LOOP;
    END LOOP;
END $$;

-- Insert Workflows
DO $$
DECLARE
    department_ids UUID[];
    employee_ids UUID[];
    user_ids UUID[];
    priorities TEXT[] := ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
    statuses TEXT[] := ARRAY['NEW', 'IN_PROGRESS', 'COMPLETED', 'BLOCKED'];
    i INTEGER;
BEGIN
    SELECT ARRAY_AGG(id) INTO department_ids FROM departments;
    SELECT ARRAY_AGG(id) INTO employee_ids FROM employees LIMIT 10;
    SELECT ARRAY_AGG(id) INTO user_ids FROM users WHERE role = 'MANAGER';
    
    -- Insert sample workflows
    FOR i IN 1..20 LOOP
        INSERT INTO workflows (
            title, description, department_id, assigned_employee_id, assigned_user_id,
            priority, status, due_date
        ) VALUES (
            'Workflow Task ' || i,
            'Sample workflow task for demonstration purposes',
            department_ids[(i % array_length(department_ids, 1)) + 1],
            employee_ids[(i % array_length(employee_ids, 1)) + 1],
            user_ids[1],
            priorities[(i % array_length(priorities, 1)) + 1],
            statuses[(i % array_length(statuses, 1)) + 1],
            CURRENT_DATE + (random() * 14)::INTEGER
        );
    END LOOP;
END $$;

-- Insert Automation Rules
INSERT INTO automation_rules (name, description, rule_type, threshold, enabled) VALUES
('Low Productivity Alert', 'Alert when employee productivity falls below threshold', 'LOW_PRODUCTIVITY', 50.0, true),
('High Error Rate Alert', 'Alert when error rate exceeds threshold', 'HIGH_ERROR_RATE', 5.0, true),
('Workflow Deadline Warning', 'Alert when workflow deadline is approaching', 'WORKFLOW_DEADLINE', NULL, true)
ON CONFLICT DO NOTHING;

-- Insert Sample Alerts
DO $$
DECLARE
    employee_ids UUID[];
    department_ids UUID[];
    record_ids UUID[];
    workflow_ids UUID[];
    i INTEGER;
BEGIN
    SELECT ARRAY_AGG(id) INTO employee_ids FROM employees LIMIT 5;
    SELECT ARRAY_AGG(id) INTO department_ids FROM departments;
    SELECT ARRAY_AGG(id) INTO record_ids FROM operational_records LIMIT 10;
    SELECT ARRAY_AGG(id) INTO workflow_ids FROM workflows WHERE status != 'COMPLETED' LIMIT 5;
    
    -- Insert sample alerts
    FOR i IN 1..15 LOOP
        INSERT INTO alerts (
            alert_type, severity, message, employee_id, department_id,
            operational_record_id, workflow_id, resolved
        ) VALUES (
            CASE (i % 3)
                WHEN 0 THEN 'LOW_PRODUCTIVITY'
                WHEN 1 THEN 'HIGH_ERROR_RATE'
                ELSE 'DEADLINE_APPROACHING'
            END,
            CASE (i % 4)
                WHEN 0 THEN 'INFO'
                WHEN 1 THEN 'WARNING'
                WHEN 2 THEN 'HIGH'
                ELSE 'CRITICAL'
            END,
            'Sample alert message for demonstration purposes',
            employee_ids[(i % array_length(employee_ids, 1)) + 1],
            department_ids[(i % array_length(department_ids, 1)) + 1],
            CASE WHEN i % 2 = 0 THEN record_ids[(i % array_length(record_ids, 1)) + 1] ELSE NULL END,
            CASE WHEN i % 2 != 0 THEN workflow_ids[(i % array_length(workflow_ids, 1)) + 1] ELSE NULL END,
            (i % 3 = 0) -- Every third alert is resolved
        );
    END LOOP;
END $$;

-- Insert Sample Automation Runs
DO $$
DECLARE
    rule_ids UUID[];
    automation_types TEXT[] := ARRAY['PRODUCTIVITY_CHECK', 'ERROR_RATE_CHECK', 'DEADLINE_CHECK'];
    i INTEGER;
BEGIN
    SELECT ARRAY_AGG(id) INTO rule_ids FROM automation_rules;
    
    -- Insert sample automation runs for the last 7 days
    FOR day_offset IN 0..6 LOOP
       	FOR i IN 1..3 LOOP
            INSERT INTO automation_runs (
                automation_rule_id, automation_type, start_time, end_time,
                records_processed, alerts_created, status
            ) VALUES (
                rule_ids[(i % array_length(rule_ids, 1)) + 1],
                automation_types[i],
                (CURRENT_DATE - (day_offset || ' days')::INTERVAL) || ' 09:00:00'::TIMESTAMP,
                (CURRENT_DATE - (day_offset || ' days')::INTERVAL) || ' 09:15:00'::TIMESTAMP,
                50 + (random() * 50)::INTEGER,
                (random() * 10)::INTEGER,
                'COMPLETED'
            );
        END LOOP;
    END LOOP;
END $$;
