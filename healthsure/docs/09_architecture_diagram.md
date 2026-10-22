# 09. System Architecture Diagram

HealthSure follows a production-grade, maintainable layered enterprise architecture.

```mermaid
graph TB
    subgraph "Client Layer (Presentation)"
        B[Web Browser / Mobile Client]
        API_CLI[REST Clients / Automated Test Suite]
    end

    subgraph "Docker Container: healthsure-app (Port 8080)"
        subgraph "Web & Security Layer"
            SC[Spring Security 6 - Session & BCrypt Auth]
            MVC[Spring MVC Controllers: Auth, Claim, Reviewer, Admin]
            REST[REST API Controller: OpenAPI / Swagger]
            TH[Thymeleaf Template Engine - Bootstrap 5]
        end

        subgraph "Business Logic Service Layer"
            US[UserService]
            CS[ClaimService - State Machine]
            PS[PolicyService - Coverage Engine]
            VS[ClaimValidationService - Business Rules]
            DS[DocumentService - Safe Storage]
            AS[AuditService - Immutable Event Logger]
        end

        subgraph "Data Access Layer (JPA / Hibernate)"
            UR[UserRepository]
            CR[ClaimRepository]
            PR[PolicyRepository]
            DR[ClaimDocumentRepository]
            RR[ClaimReviewRepository]
            AR[ClaimAuditEventRepository]
        end

        subgraph "Observability & Lifecycle"
            ACT[Spring Boot Actuator: /actuator/health]
        end
    end

    subgraph "Storage & Infrastructure"
        MYSQL[(MySQL 8 Database Container: healthsure-mysql)]
        VOL[(Docker Volume: app_uploads)]
    end

    B -->|HTTPS/HTTP 8081| SC
    API_CLI -->|REST /api/**| SC
    SC --> MVC
    SC --> REST
    MVC --> TH
    MVC --> CS
    MVC --> US
    MVC --> PS
    REST --> CS
    CS --> VS
    CS --> AS
    CS --> PS
    CS --> DS
    US --> UR
    CS --> CR
    PS --> PR
    DS --> DR
    AS --> AR
    UR --> MYSQL
    CR --> MYSQL
    PR --> MYSQL
    DR --> MYSQL
    RR --> MYSQL
    AR --> MYSQL
    DS -->|Secure File I/O| VOL
    ACT -->|DB Health Ping| MYSQL
```

## Layer Responsibilities
1. **Security & Web Layer:** Intercepts requests, validates CSRF tokens, authorizes URLs against user roles, and renders responsive Thymeleaf HTML or JSON DTO responses.
2. **Service Layer:** Houses the claim lifecycle finite state machine, duplicate detection algorithms, monetary balance calculations, and audit logging.
3. **Repository Layer:** Spring Data JPA interfaces backed by Hibernate ORM with optimized queries.
4. **Data & Storage Layer:** MySQL 8 storing normalized relational data, with persistent volume mounts for file uploads.
