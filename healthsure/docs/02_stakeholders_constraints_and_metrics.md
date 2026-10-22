# 02. Stakeholders, Constraints & Measurable Success Criteria

## 1. Project Stakeholders
| Stakeholder Group | Primary Responsibilities & Interests | System Interaction Point |
|---|---|---|
| **Policyholders (Patients/Insured)** | Submits claims, attaches bills/reports, tracks review timeline, responds to reviewer inquiries. | Policyholder Dashboard, `/claims/new`, `/claims/{id}` |
| **Claims Reviewers (Medical Officers)** | Evaluates claims, verifies diagnostics, requests additional info, records approve/reject decisions. | Reviewer Queue (`/reviewer/queue`), Review Details (`/reviewer/claims/{id}`) |
| **System Administrators** | Manages user accounts, assigns roles, configures insurance policies, reviews system audit logs. | Admin Dashboard (`/admin/dashboard`), User & Policy Management |
| **DevOps Engineers / Academics** | Evaluates build health, container orchestration, Jenkins pipeline execution, and rollback strategies. | Docker Compose, Jenkins Pipeline, Spring Boot Actuator |

## 2. Technical & Academic Constraints
- **Zero Paid Dependencies:** No cloud subscription requirements, proprietary database drivers, or paid third-party APIs.
- **Local Reproducibility:** Entire stack must be runnable locally on Windows 11 with Docker Desktop, Java 21+, and Maven.
- **Stateless Application Server:** Application state is persisted strictly in MySQL and designated upload volumes.
- **Fail-Safe Rollback:** Deployment must verify health endpoint before declaring release success.

## 3. Measurable Success Criteria
| Metric | Target | Verification Method |
|---|---|---|
| **Build Reproducibility** | Clean package in < 60s | `mvn clean package -DskipTests=true` |
| **Automated Test Coverage** | 100% core state transitions passing | `mvn test` (JUnit 5 + Mockito) |
| **Browser Journey Automation** | 5 complete journeys passing | `mvn test -Pselenium` (Selenium WebDriver) |
| **Container Startup Time** | Application healthy within 45s | Docker Compose healthcheck (`/actuator/health`) |
| **Duplicate Claim Prevention** | 100% duplicate detection in window | `ClaimValidationServiceTest` |
| **Role Authorization Integrity** | 100% 403 Access Denied for unauthorized access | Selenium Journey 5 & Spring Security filters |
