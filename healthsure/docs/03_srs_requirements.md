# 03. Software Requirements Specification (SRS) Summary

## 1. Functional Requirements (FR)

### FR-01: Authentication & Access Control
- The system shall authenticate users using session cookies with BCrypt password hashing (strength 10/12).
- The system shall enforce Role-Based Access Control (RBAC) across three distinct roles: `ROLE_POLICYHOLDER`, `ROLE_REVIEWER`, `ROLE_ADMIN`.
- The system shall prevent brute force session hijacking through single active session concurrency (`maximumSessions(1)`).

### FR-02: Policy Management
- Administrators shall create and manage insurance policies specifying coverage amount, policy type, policyholder ID, start date, and expiry date.
- The system shall calculate available coverage as: `availableCoverage = coverageAmount - usedCoverage`.

### FR-03: Claim Submission & Draft Creation
- Policyholders shall create claim drafts and submit claims against their active policies.
- Mandatory fields: associated policy, claim type, hospital name, doctor name, treatment start date, claimed amount, and description (>= 20 characters).
- Optional fields: treatment end date and additional notes.

### FR-04: Automated Validation Engine
- The system shall validate claim completeness, policy ownership, policy active status, date validity, and coverage limit before advancing a claim to review.
- The system shall flag potential duplicates if a claim with identical policy, claim type, and treatment date was submitted within the previous 30 days.

### FR-05: Reviewer Decision Workflow
- Reviewers shall view claims in status `UNDER_REVIEW`, inspect claim details and documents, and record decisions: `APPROVED`, `REJECTED`, or `ADDITIONAL_INFO_REQUIRED`.
- Rejections shall require a mandatory explanation reason.
- Additional information requests shall transition the claim to `ADDITIONAL_INFO_REQUIRED` and notify the policyholder for revision.

### FR-06: Document Management
- The system shall support uploading safe file formats (PDF, JPEG, PNG) up to 10MB per file.
- Uploaded files shall be stored outside the web root with server-generated UUID filenames.
- Document downloads shall be restricted to the claim owner, reviewers, and administrators.

### FR-07: Immutable Audit Trail
- The system shall record trusted audit log entries for all lifecycle events (`CLAIM_CREATED`, `CLAIM_SUBMITTED`, `CLAIM_VALIDATED`, `VALIDATION_FAILED`, `CLAIM_APPROVED`, `CLAIM_REJECTED`, `ADDITIONAL_INFO_REQUESTED`, `DOCUMENT_UPLOADED`).

---

## 2. Non-Functional Requirements (NFR)

| ID | Requirement | Target Metric / Implementation |
|---|---|---|
| **NFR-01** | **Security** | CSRF protection on state-changing endpoints, SQL injection prevention via Spring Data JPA ORM, password hashing via BCrypt, path-traversal prevention on file uploads. |
| **NFR-02** | **Performance** | Page response latency under 300ms for database queries with proper indexing on foreign keys and reference codes. |
| **NFR-03** | **Availability & Health** | Actuator liveness and readiness probes (`/actuator/health`) for container health monitoring. |
| **NFR-04** | **Maintainability** | Layered architecture (Controller, Service, Repository, Entity, DTO) and database migration versioning via Flyway. |
| **NFR-05** | **Portability** | Multi-stage Docker containerization running under non-root user `appuser` on any compliant OCI container runtime. |
