-- V2__seed_data.sql
-- Development seed data (loaded only in dev/test with SEED_DATA_ENABLED=true)
-- WARNING: These are demo credentials. NEVER use in production.
-- Admin:     admin / Admin@1234
-- Reviewer1: reviewer1 / Review@1234
-- Reviewer2: reviewer2 / Review@1234
-- User1:     john.doe / User@1234
-- User2:     jane.smith / User@1234

-- Seed only when the table is empty (idempotent)
INSERT IGNORE INTO users (username, password_hash, email, first_name, last_name, phone, address, enabled, created_at, updated_at)
VALUES
  ('admin', '$2a$10$FvcEO9/hTFoKqeNAv3fCdehqew3XEeoyW71GzV3Vq.5lZiBBpzV8u',
   'admin@healthsure.demo', 'Admin', 'User', '+91-9000000001', '1 Admin Street, Mumbai', true, NOW(), NOW()),
  ('reviewer1', '$2a$10$BXFNmbCWlmWDLph7DeWA5eE7Z2o9HpP5EVKRWwCuFBmRCmpEYxjea',
   'reviewer1@healthsure.demo', 'Dr. Sarah', 'Johnson', '+91-9000000002', '2 Review Lane, Delhi', true, NOW(), NOW()),
  ('reviewer2', '$2a$10$BXFNmbCWlmWDLph7DeWA5eE7Z2o9HpP5EVKRWwCuFBmRCmpEYxjea',
   'reviewer2@healthsure.demo', 'Dr. Michael', 'Chen', '+91-9000000003', '3 Review Lane, Bangalore', true, NOW(), NOW()),
  ('john.doe', '$2a$10$iKFZLlgT4/.avUviX3/dWuYS7tEYDVYoGVSnebJ2er/DljgqGqM7O',
   'john.doe@demo.com', 'John', 'Doe', '+91-9000000004', '10 Green Park, Pune', true, NOW(), NOW()),
  ('jane.smith', '$2a$10$iKFZLlgT4/.avUviX3/dWuYS7tEYDVYoGVSnebJ2er/DljgqGqM7O',
   'jane.smith@demo.com', 'Jane', 'Smith', '+91-9000000005', '22 Blue Hills, Chennai', true, NOW(), NOW());

INSERT IGNORE INTO user_roles (user_id, role)
SELECT id, 'ADMIN' FROM users WHERE username = 'admin'
UNION ALL
SELECT id, 'REVIEWER' FROM users WHERE username = 'reviewer1'
UNION ALL
SELECT id, 'REVIEWER' FROM users WHERE username = 'reviewer2'
UNION ALL
SELECT id, 'POLICYHOLDER' FROM users WHERE username = 'john.doe'
UNION ALL
SELECT id, 'POLICYHOLDER' FROM users WHERE username = 'jane.smith';

-- Seed policies for john.doe
INSERT IGNORE INTO insurance_policies
  (policy_number, policyholder_id, policy_type, coverage_amount, used_coverage, start_date, expiry_date, status, description, created_at, updated_at)
SELECT
  'POL-JOHN0001',
  u.id,
  'INDIVIDUAL',
  500000.00,
  0.00,
  '2024-01-01',
  '2026-12-31',
  'ACTIVE',
  'Individual Health Policy - Standard Coverage',
  NOW(),
  NOW()
FROM users u WHERE u.username = 'john.doe';

INSERT IGNORE INTO insurance_policies
  (policy_number, policyholder_id, policy_type, coverage_amount, used_coverage, start_date, expiry_date, status, description, created_at, updated_at)
SELECT
  'POL-JOHN0002',
  u.id,
  'CRITICAL_ILLNESS',
  1000000.00,
  75000.00,
  '2023-06-01',
  '2026-05-31',
  'ACTIVE',
  'Critical Illness Coverage Plan',
  NOW(),
  NOW()
FROM users u WHERE u.username = 'john.doe';

-- Seed policy for jane.smith
INSERT IGNORE INTO insurance_policies
  (policy_number, policyholder_id, policy_type, coverage_amount, used_coverage, start_date, expiry_date, status, description, created_at, updated_at)
SELECT
  'POL-JANE0001',
  u.id,
  'FAMILY',
  750000.00,
  25000.00,
  '2024-03-01',
  '2026-02-28',
  'ACTIVE',
  'Family Health Insurance Plan',
  NOW(),
  NOW()
FROM users u WHERE u.username = 'jane.smith';
