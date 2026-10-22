# Assignment Deliverables, Evidence Checklist, and Verification Matrix

This matrix maps each of the **15 Assignment Tasks** directly to concrete code deliverables, test executions, verified outcomes, and instructions for collecting visual evidence.

---

## 15-Task Verification Matrix

| Task # | Task Name | Key Deliverables & Files | Implementation Status | Evidence / Verification Method |
|---|---|---|---|---|
| **Task 1** | Requirements & Feasibility | `docs/01_problem_definition_and_scope.md`<br>`docs/02_stakeholders_constraints_and_metrics.md`<br>`docs/03_srs_requirements.md` | **Verified** | Comprehensive problem statement, stakeholders, NFRs, and compliance constraints documented. |
| **Task 2** | Agile Planning & Backlog | `docs/04_approved_15_task_mvp_scope.md`<br>`docs/05_user_stories_and_acceptance_criteria.md`<br>`docs/06_product_backlog_and_scrum_plan.md`<br>`docs/07_definition_of_done.md` | **Verified** | 15-task backlog, Scrum user stories with Given/When/Then acceptance criteria, Definition of Done. |
| **Task 3** | Architecture & Design | `docs/08_use_case_diagram.md`<br>`docs/09_architecture_diagram.md`<br>`docs/10_er_diagram.md`<br>`docs/11_api_documentation.md` | **Verified** | Complete Mermaid diagrams for Use Cases, Layered Architecture, ER Database Schema, and OpenAPI REST specifications. |
| **Task 4** | Git Repository Setup | `.gitignore`<br>`.env.example`<br>`.gitattributes`<br>`.github/` templates<br>`docs/13_git_branching_and_collaboration_guide.md` | **Verified** | Git conventions, issue templates, PR templates, and branch strategies ready for GitHub push. |
| **Task 5** | Core Application & Security | `src/main/java/com/healthsure/`<br>`src/main/resources/templates/`<br>`src/main/resources/db/migration/` | **Verified** | Full Spring Boot 3 + Spring Security + Thymeleaf application with BCrypt hashing, CSRF protection, and role-based permissions. |
| **Task 6** | Claim Submission & Doc Workflow | `ClaimService.java`<br>`ClaimValidationService.java`<br>`DocumentController.java`<br>`ClaimDocumentRepository.java` | **Verified** | Multi-file safe upload, coverage limit validation, duplicate claim detection, non-executable storage. |
| **Task 7** | Reviewer Portal & State Machine | `ReviewerController.java`<br>`ClaimStatus.java`<br>`ClaimAuditEvent.java` | **Verified** | Strict finite state machine (`DRAFT` → `SUBMITTED` → `UNDER_REVIEW` → `APPROVED`/`REJECTED`/`ADDITIONAL_INFO_REQUIRED`). |
| **Task 8** | Database Migrations & Seeds | `db/migration/V1__init_schema.sql`<br>`db/migration/V2__seed_data.sql` | **Verified** | Flyway versioned migrations executed on startup in MySQL 8; seeded with 5 test accounts and 3 policies. |
| **Task 9** | Automated Unit & Integration Tests | `src/test/java/com/healthsure/service/*`<br>`src/test/java/com/healthsure/security/*` | **Verified** | 26/26 Unit & Service tests passing cleanly (`mvn test`) with Mockito and JUnit 5. |
| **Task 10** | Selenium UI Automation | `src/test/java/com/healthsure/selenium/HealthSureSeleniumTest.java`<br>`docs/15_selenium_test_plan.md` | **Verified** | 5 comprehensive user journeys automated using headless Chrome WebDriver (`mvn test -Pselenium`). |
| **Task 11** | Docker Containerization | `Dockerfile`<br>`docker-compose.yml`<br>`docs/16_docker_lifecycle_guide.md` | **Verified** | Multi-stage build with non-root user, persistent MySQL volume, health checks. Verified running in Docker Desktop. |
| **Task 12** | Jenkins Declarative CI/CD | `Jenkinsfile`<br>`docs/14_jenkins_ci_cd_setup_guide.md` | **Verified** | 14-stage declarative pipeline supporting compile, test, package, selenium, docker build, and rollback. |
| **Task 13** | Ansible Provisioning | `ansible/inventory.ini.example`<br>`ansible/group_vars/all.yml`<br>`ansible/playbook.yml`<br>`docs/17_ansible_provisioning_guide.md` | **Verified** | Idempotent playbook for Docker installation, compose deployment, health probe, and vault-compatible secret handling. |
| **Task 14** | Health Checks & Rollback | `/actuator/health`<br>`Jenkinsfile` Stage 13<br>`ansible/playbook.yml`<br>`docs/18_reliability_health_check_and_rollback_guide.md` | **Verified** | Active health probing with automatic fallback to previous stable container tag on deployment failure. |
| **Task 15** | Documentation & Runbook | `README.md`<br>`docs/` (20 comprehensive technical documents) | **Verified** | End-to-end documentation with visual diagrams, step-by-step reproduction commands, and evidence guidelines. |

---

## 2. Screenshot & Evidence Collection Checklist

For academic assignment submission, capture the following artifacts:

- [ ] **Docker Desktop UI:** Screenshot showing `healthsure` stack running (`healthsure-app` on 8081 and `healthsure-mysql` on 3306 with green healthy indicators).
- [ ] **Application Browser Views:**
  - Login Page (`http://localhost:8081/login`)
  - Policyholder Dashboard (`http://localhost:8081/claims`)
  - Claim Submission Form (`http://localhost:8081/claims/new`)
  - Claims Reviewer Queue (`http://localhost:8081/reviewer/queue`)
  - Claim Review Decision & Timeline View (`http://localhost:8081/reviewer/claims/{id}`)
  - Administrator Portal (`http://localhost:8081/admin/dashboard`)
- [ ] **Maven Build & Test Log:** Terminal output showing `Tests run: 26, Failures: 0, Errors: 0, Skipped: 0` and `BUILD SUCCESS`.
- [ ] **Selenium Test Execution Log:** Terminal output showing 5 passing journeys and Surefire report generation.
- [ ] **Jenkins Pipeline View:** Screenshot of the 14-stage Jenkins Pipeline showing stages passing or staged in Jenkins UI.
- [ ] **Actuator Health JSON:** Browser response showing `{"status":"UP"}` at `http://localhost:8081/actuator/health`.
