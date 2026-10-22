# 11. REST API Specification & Swagger Documentation

HealthSure exposes documented REST endpoints for headless programmatic integration and testing alongside the Thymeleaf web interface.

- **Interactive Swagger UI:** `http://localhost:8081/swagger-ui.html`
- **OpenAPI 3 JSON Spec:** `http://localhost:8081/api-docs`

---

## Core REST Endpoints

### 1. Authentication & Profile
- **`GET /api/auth/me`**
  - **Description:** Returns profile details for current authenticated user.
  - **Response:** `200 OK` (JSON: `UserResponseDto`)
  - **Security:** Authenticated

### 2. Claims Management
- **`GET /api/claims?page=0&size=10`**
  - **Description:** Paginated listing of claims based on user role (Policyholder sees own, Reviewer sees pending, Admin sees all).
  - **Response:** `200 OK` (JSON: `Page<ClaimResponseDto>`)

- **`POST /api/claims`**
  - **Description:** Creates a new claim draft.
  - **Request Body:**
    ```json
    {
      "policyId": 1,
      "claimType": "HOSPITALIZATION",
      "hospitalName": "Apollo City Hospital",
      "doctorName": "Dr. Sarah Rao",
      "treatmentStartDate": "2026-05-10",
      "treatmentEndDate": "2026-05-14",
      "claimedAmount": 28500.00,
      "description": "Emergency appendectomy and hospital ward stay.",
      "submitNow": true
    }
    ```
  - **Response:** `201 Created` (JSON: `ClaimResponseDto`)

- **`GET /api/claims/{id}`**
  - **Description:** Returns full claim details.
  - **Response:** `200 OK` / `403 Forbidden` / `404 Not Found`

- **`POST /api/claims/{id}/submit`**
  - **Description:** Submits a draft or revised claim for automated business validation.
  - **Response:** `200 OK`

- **`POST /api/claims/{id}/validate`**
  - **Description:** Runs the business validation engine and returns validation errors or success.
  - **Response:** `200 OK` (`ValidationResult`)

- **`POST /api/claims/{id}/review`**
  - **Description:** Records reviewer decision (`APPROVED`, `REJECTED`, `ADDITIONAL_INFO_REQUIRED`).
  - **Security:** `hasAnyRole('REVIEWER', 'ADMIN')`
  - **Request Body:**
    ```json
    {
      "decision": "APPROVED",
      "notes": "Verified against attached hospital invoices. Eligible procedure.",
      "recommendedAmount": 28500.00
    }
    ```
  - **Response:** `200 OK`

- **`GET /api/claims/{id}/audit`**
  - **Description:** Returns chronological list of immutable audit events for this claim.
  - **Response:** `200 OK` (`List<ClaimAuditEvent>`)

### 3. Actuator Health Probes
- **`GET /actuator/health`**
  - **Response:** `200 OK` (`{"status":"UP"}`)
