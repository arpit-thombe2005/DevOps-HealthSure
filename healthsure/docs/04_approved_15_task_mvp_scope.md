# 04. Approved 15-Task MVP Scope Mapping

This document maps all 15 DevOps and software engineering assignment tasks to concrete repository deliverables, implementation files, verification commands, and execution status.

| Task # | Task Description | Concrete Deliverables in Repository | Verification Command | Implementation Status |
|:---:|---|---|---|:---:|
| **Task 1** | **Problem Definition & Scope** | `docs/01_problem_definition_and_scope.md`, `docs/02_stakeholders_constraints_and_metrics.md` | Document review | **Implemented & Verified** |
| **Task 2** | **SRS & Requirements** | `docs/03_srs_requirements.md`, `ValidationResult.java`, Entity constraints | Maven build | **Implemented & Verified** |
| **Task 3** | **Agile Planning & Scrum** | `docs/05_user_stories_and_acceptance_criteria.md`, `docs/06_product_backlog_and_scrum_plan.md`, `docs/07_definition_of_done.md` | Agile backlog inspection | **Implemented & Verified** |
| **Task 4** | **System Architecture & ERD** | `docs/08_use_case_diagram.md`, `docs/09_architecture_diagram.md`, `docs/10_er_diagram.md`, Flyway `V1__init_schema.sql` | Schema check | **Implemented & Verified** |
| **Task 5** | **Git & GitHub Strategy** | `.gitignore`, `.env.example`, `.gitattributes`, `.github/` templates, `docs/13_git_branching_and_collaboration_guide.md` | `git status` | **Implemented & Verified** |
| **Task 6** | **Feature Branching & Implementation** | Layered Spring Boot backend (43 classes), Thymeleaf responsive UI (16 templates), Flyway V1 & V2 | `mvn clean compile` | **Implemented & Verified** |
| **Task 7** | **REST API & Swagger Documentation** | `ClaimApiController.java`, OpenAPI 3 / Swagger (`/swagger-ui.html`, `/api-docs`), DTOs | `curl http://localhost:8081/swagger-ui.html` | **Implemented & Verified** |
| **Task 8** | **Automated Unit & Service Testing** | 26 JUnit 5 & Mockito test cases in `src/test/java/com/healthsure/service/` and `security/` | `mvn test` (26 tests pass) | **Implemented & Verified** |
| **Task 9** | **Selenium WebDriver Automation** | 5 browser automation journeys in `HealthSureSeleniumTest.java` | `mvn test -Pselenium` | **Implemented & Verified** |
| **Task 10** | **Dockerization & Multi-Stage Image** | `Dockerfile` (multi-stage build with non-root user `appuser`, JRE 21 runtime, actuator probe) | `docker build -t healthsure .` | **Implemented & Verified** |
| **Task 11** | **Docker Compose Orchestration** | `docker-compose.yml` (HealthSure app + MySQL 8 + persistent volumes + bridge network) | `docker compose up -d` | **Implemented & Verified** |
| **Task 12** | **Jenkins Declarative CI/CD Pipeline** | 14-stage `Jenkinsfile` with testing, package archiving, docker build, registry push, deploy, rollback | Jenkins job build | **Implemented & Documented** |
| **Task 13** | **Container Registry Publishing** | Jenkinsfile Stage 11 with Docker Hub credentials integration | Jenkins Pipeline step | **Implemented & Documented** |
| **Task 14** | **Ansible Provisioning & CM** | `ansible/playbook.yml`, `ansible/inventory.ini.example`, `ansible/group_vars/all.yml` | `ansible-playbook --syntax-check` | **Implemented & Verified** |
| **Task 15** | **Reliability, Health Probes & Rollback** | Spring Boot Actuator (`/actuator/health`), Jenkins Stage 13 automated rollback, Ansible rescue block | `curl -f http://localhost:8081/actuator/health` | **Implemented & Verified** |
