-- V1__init_schema.sql
-- HealthSure Database Schema
-- Created: Initial schema

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_users_username (username),
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS insurance_policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_number VARCHAR(20) NOT NULL UNIQUE,
    policyholder_id BIGINT NOT NULL,
    policy_type VARCHAR(30) NOT NULL,
    coverage_amount DECIMAL(15,2) NOT NULL,
    used_coverage DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    description VARCHAR(500),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_policy_number (policy_number),
    INDEX idx_policy_holder (policyholder_id),
    CONSTRAINT fk_policy_holder FOREIGN KEY (policyholder_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS insurance_claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_reference VARCHAR(20) NOT NULL UNIQUE,
    policy_id BIGINT NOT NULL,
    submitted_by_id BIGINT NOT NULL,
    claim_type VARCHAR(30) NOT NULL,
    hospital_name VARCHAR(200) NOT NULL,
    doctor_name VARCHAR(200) NOT NULL,
    treatment_start_date DATE NOT NULL,
    treatment_end_date DATE,
    claimed_amount DECIMAL(15,2) NOT NULL,
    approved_amount DECIMAL(15,2),
    description VARCHAR(2000),
    additional_notes VARCHAR(2000),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    submitted_at DATETIME(6),
    reviewed_at DATETIME(6),
    assigned_reviewer_id BIGINT,
    reviewer_notes VARCHAR(2000),
    rejection_reason VARCHAR(2000),
    additional_info_request VARCHAR(2000),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_claim_ref (claim_reference),
    INDEX idx_claim_policy (policy_id),
    INDEX idx_claim_submitter (submitted_by_id),
    INDEX idx_claim_status (status),
    CONSTRAINT fk_claim_policy FOREIGN KEY (policy_id) REFERENCES insurance_policies(id),
    CONSTRAINT fk_claim_submitter FOREIGN KEY (submitted_by_id) REFERENCES users(id),
    CONSTRAINT fk_claim_reviewer FOREIGN KEY (assigned_reviewer_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS claim_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_id BIGINT NOT NULL,
    uploaded_by_id BIGINT NOT NULL,
    original_filename VARCHAR(200) NOT NULL,
    stored_filename VARCHAR(200) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    description VARCHAR(200),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_doc_claim (claim_id),
    CONSTRAINT fk_doc_claim FOREIGN KEY (claim_id) REFERENCES insurance_claims(id),
    CONSTRAINT fk_doc_uploader FOREIGN KEY (uploaded_by_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS claim_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    previous_status VARCHAR(30) NOT NULL,
    new_status VARCHAR(30) NOT NULL,
    notes VARCHAR(2000),
    decision_reason VARCHAR(2000),
    recommended_amount DECIMAL(15,2),
    decision_type VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_review_claim (claim_id),
    INDEX idx_review_reviewer (reviewer_id),
    CONSTRAINT fk_review_claim FOREIGN KEY (claim_id) REFERENCES insurance_claims(id),
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS claim_audit_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_id BIGINT NOT NULL,
    actor_id BIGINT NOT NULL,
    actor_username VARCHAR(100) NOT NULL,
    event_type VARCHAR(30) NOT NULL,
    previous_status VARCHAR(30),
    new_status VARCHAR(30),
    description VARCHAR(2000),
    ip_address VARCHAR(50),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_audit_claim (claim_id),
    INDEX idx_audit_actor (actor_id),
    CONSTRAINT fk_audit_claim FOREIGN KEY (claim_id) REFERENCES insurance_claims(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
