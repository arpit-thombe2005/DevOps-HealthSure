# 07. Definition of Done (DoD)

For any task, user story, or deployment release to be marked as **DONE** in HealthSure, it must satisfy all criteria across the development, quality assurance, security, and DevOps dimensions:

## 1. Code & Architecture Quality
- [x] Code strictly complies with clean architecture guidelines (Layered: Controller -> Service -> Repository -> Entity).
- [x] Zero direct database entity exposure in REST controllers; DTOs utilized for all ingress/egress data.
- [x] Database migrations created as idempotent Flyway SQL scripts (`V1__...`, `V2__...`).
- [x] All backend routes protected with appropriate `@PreAuthorize` or Spring Security filter rules.

## 2. Automated Testing Quality
- [x] Unit tests cover all business validation rules and state transitions.
- [x] Zero test failures (`mvn test` passes 100% of test cases).
- [x] Selenium WebDriver automated journeys cover core user roles (Policyholder, Reviewer, Admin).
- [x] Selenium test failures automatically record timestamped PNG screenshots in `target/selenium-screenshots/`.

## 3. Container & Deployment Readiness
- [x] Application compiles cleanly into an executable Spring Boot JAR.
- [x] Multi-stage Docker image builds without caching errors and runs as non-root user `appuser`.
- [x] Docker Compose stack starts application and MySQL 8 with healthy dependency resolution.
- [x] Actuator health probe (`/actuator/health`) returns HTTP 200 `UP`.

## 4. Documentation & Assignment Compliance
- [x] Environment variable placeholders documented in `.env.example`.
- [x] Full operational runbook and troubleshooting steps documented in `docs/`.
- [x] All 15 DevOps tasks mapped to concrete workspace artifacts.
