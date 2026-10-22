# DevOps Lifecycle Diagram & Process Overview

This document visualizes the complete DevOps lifecycle for **HealthSure – Dockerized Health Insurance Claim Workflow**, showing how source code transitions from development, testing, and continuous integration through to automated deployment and monitoring.

```mermaid
flowchart TD
    subgraph Plan_and_Code ["1. Plan & Code (Agile / Git)"]
        A[Developer Workstation] -->|Feature Branch| B[Local Code & Unit Tests]
        B -->|Git Commit & Push| C[GitHub Repository]
        C -->|Pull Request to develop/main| D[Code Review & Approval]
    end

    subgraph CI_Pipeline ["2. Continuous Integration (Jenkins)"]
        D -->|Webhook Trigger / Poll| E[Jenkins CI Pipeline]
        E --> F[Compile & Static Analysis]
        F --> G[Run Unit & Service Tests]
        G --> H[Run Selenium E2E Tests]
        H --> I[Build Docker Multi-Stage Image]
        I --> J[Tag & Push to Docker Registry]
    end

    subgraph CD_Deployment ["3. Continuous Deployment (Docker / Ansible)"]
        J --> K[Ansible Orchestration / Compose Deploy]
        K --> L[Deploy MySQL Container]
        K --> M[Deploy HealthSure Spring Boot App]
        M --> N{Health Check /actuator/health}
        N -->|Success 200 OK| O[Active Production Release]
        N -->|Failure / Timeout| P[Automated Rollback to Previous Image]
    end

    subgraph Monitor_Operate ["4. Monitor & Operate (Observability)"]
        O --> Q[Spring Boot Actuator & Logs]
        O --> R[Policyholder & Reviewer Access]
        Q -->|Feedback / Incident Report| A
    end

    style Plan_and_Code fill:#f0f7ff,stroke:#0d6efd,stroke-width:2px
    style CI_Pipeline fill:#fff7ed,stroke:#fd7e14,stroke-width:2px
    style CD_Deployment fill:#f0fdf4,stroke:#198754,stroke-width:2px
    style Monitor_Operate fill:#fbfbfe,stroke:#6c757d,stroke-width:2px
```

## Lifecycle Phases Description

1. **Plan & Track:** Requirements and user stories are tracked across 15 MVP tasks using Agile/Scrum.
2. **Develop:** Java 21 / Spring Boot 3 with Thymeleaf frontend, developed using feature branches (`feature/*`).
3. **Commit & Push:** Enforced commit conventions (e.g., `feat:`, `fix:`, `test:`).
4. **Build & Test (CI):** Jenkins declarative pipeline executes compile, unit tests (JUnit 5 + Mockito), and end-to-end Selenium WebDriver suites.
5. **Containerize:** Docker multi-stage build creates minimal, secure, non-root runtime image tagged with build number and `latest`.
6. **Deploy (CD):** Ansible playbook or Docker Compose deploys containers into an isolated bridge network with health verification.
7. **Rollback:** Automated script verifies container health via `/actuator/health`; on failure, reverts to previous stable image.
8. **Operate & Monitor:** Observability enabled via Actuator endpoints (`/actuator/health`, `/actuator/info`) and structured application logging.
