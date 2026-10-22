# HealthSure – Dockerized Health Insurance Claim Workflow

![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=flat-square&logo=springboot)
![Java](https://img.shields.io/badge/Java-21%20%7C%2025-orange?style=flat-square&logo=openjdk)
![Docker](https://img.shields.io/badge/Docker-Multi--stage%20Compose-blue?style=flat-square&logo=docker)
![Jenkins](https://img.shields.io/badge/Jenkins-Declarative%2014--Stage-red?style=flat-square&logo=jenkins)
![Selenium](https://img.shields.io/badge/Selenium-WebDriver%20E2E-43B02A?style=flat-square&logo=selenium)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

**HealthSure** is a production-grade, end-to-end DevOps and Full-Stack Health Insurance Claim processing platform. It automates policyholder claim submission, multi-file document verification, duplicate detection, coverage checks, reviewer assessments, and immutable audit tracking.

Built to fulfill all **15 Academic DevOps/Software Engineering Tasks**, HealthSure is fully containerized with Docker Compose, tested with JUnit 5, Mockito, and Selenium WebDriver, orchestrated with a 14-stage declarative Jenkins pipeline, and provisioned with idempotent Ansible playbooks.

---

## 🌟 Key Features

- **Multi-Role Access Control (RBAC):**
  - **Policyholder:** Submit claims, upload medical invoices/reports, track real-time claim status, view claim timeline.
  - **Claims Reviewer:** Queue management, inspection of claim documents, approval, rejection with mandatory reason, or requesting additional information.
  - **Administrator:** User management, policy configuration, audit log inspection, system monitoring.
- **Strict Claim State Machine:**
  - `DRAFT` ➔ `SUBMITTED` ➔ `UNDER_REVIEW` ➔ `APPROVED` / `REJECTED` / `ADDITIONAL_INFO_REQUIRED`.
  - Revalidation and resubmission loop for claims requiring information.
- **Validation Engine:** Server-side checks for coverage limits, policy active validity dates, duplicate submission detection, and safe document extensions (PDF/PNG/JPEG).
- **Audit History & Observability:** Immutable event logging (`ClaimAuditEvent`) and Spring Boot Actuator health and metrics probes (`/actuator/health`).
- **DevOps Integration:** Multi-stage Docker containerization (non-root `appuser`), Docker Compose service orchestration with health dependencies, 14-stage Jenkins CI/CD pipeline, and Ansible server provisioning.

---

## 🏗️ Architecture & Technology Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 21 / Spring Boot 3.2.5 (MVC, Data JPA, Security, Actuator, Validation) |
| **Frontend** | Thymeleaf 3, Bootstrap 5.3, Bootstrap Icons, Responsive CSS |
| **Database** | MySQL 8.0.36 with Flyway versioned database migrations |
| **Testing** | JUnit 5, Mockito, Spring Boot Test, Selenium WebDriver 4.20 (Headless Chrome) |
| **Containers** | Docker Engine & Docker Compose (Multi-stage Temurin JRE runtime) |
| **CI/CD** | Jenkins Declarative Pipeline (14 stages, cross-platform Windows `bat` / Linux `sh`) |
| **Configuration** | Ansible Playbook, group variables, and inventory for idempotent deployment |

---

## 🚀 Quick Start Guide

### Option 1: Running with Docker Compose (Recommended)

1. Make sure **Docker Desktop** is running.
2. In the `healthsure` directory, run:
   ```bash
   docker compose up -d --build
   ```
3. Verify that both containers are running and healthy:
   ```bash
   docker compose ps
   ```
4. Access the web portal in your browser:
   - **Application Portal:** [http://localhost:8081](http://localhost:8081)
   - **Actuator Health Check:** [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)

*(Note: Port 8081 is used to avoid port conflicts with native Jenkins running on 8080).*

---

### Option 2: Local Development Workflow (Without Docker App)

If you have Java 21+ and Maven installed locally:
1. Start MySQL via Docker:
   ```bash
   docker compose up -d healthsure-mysql
   ```
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. Open [http://localhost:8080](http://localhost:8080).

---

## 🔐 Seeded Demonstration Accounts

The system is pre-populated via Flyway migrations (`V2__seed_data.sql`) with the following test credentials:

| Role | Username | Password | Intended Journey |
|---|---|---|---|
| **Administrator** | `admin` | `Admin@1234` | Manage users & policies, inspect audits |
| **Claims Reviewer** | `reviewer1` | `Review@1234` | Review pending claims, approve/reject |
| **Senior Reviewer** | `reviewer2` | `Review@1234` | Secondary reviewer access |
| **Policyholder** | `john.doe` | `User@1234` | Submit claims against policy `POL-2024-001` |
| **Policyholder** | `jane.smith` | `User@1234` | Submit claims against policy `POL-2024-002` |

---

## 🧪 Automated Testing

### 1. Run Unit & Service Tests (26 Tests)
Uses in-memory H2 database in MySQL compatibility mode:
```powershell
mvn test
```
*Output: `Tests run: 26, Failures: 0, Errors: 0, Skipped: 0`*

### 2. Run Selenium End-to-End Tests (5 Journeys)
Runs headless Chrome tests against the active application:
```powershell
mvn test -Pselenium "-Dapp.url=http://localhost:8081"
```

---

## 🔄 Jenkins CI/CD Pipeline

The `Jenkinsfile` defines a 14-stage declarative pipeline:
1. **Checkout:** Git source checkout.
2. **Environment Check:** Diagnostic verification of Java, Maven, Docker.
3. **Compile:** `mvn clean compile`.
4. **Unit Tests:** Service and security tests with Surefire XML generation.
5. **Integration Tests:** Repository and database integration tests.
6. **Package:** Production Spring Boot executable JAR generation.
7. **Static Checks:** Code and configuration verification.
8. **Selenium Tests:** Automated headless browser user journeys.
9. **Docker Build:** Builds minimal multi-stage image.
10. **Image Tag:** Tags image with build number and `latest`.
11. **Registry Publish:** Pushes to Docker Hub (when credentials configured).
12. **Deploy Staging:** Deploys container stack using Docker Compose.
13. **Health Check & Rollback:** Probes `/actuator/health`; automatically reverts to `healthsure:previous` if unhealthy.
14. **Cleanup:** Archives Surefire test reports and cleans workspace.

---

## 📁 Project Directory Structure

```text
healthsure/
├── .github/                     # GitHub Issue & PR Templates
├── ansible/                     # Ansible Playbook, Inventory & Group Vars
│   ├── inventory.ini.example
│   ├── group_vars/all.yml
│   └── playbook.yml
├── docs/                        # Complete 20-Deliverable Documentation Suite
│   ├── 01_problem_definition_and_scope.md
│   ├── 08_use_case_diagram.md
│   ├── 09_architecture_diagram.md
│   ├── 10_er_diagram.md
│   ├── 11_api_documentation.md
│   ├── 12_devops_lifecycle_diagram.md
│   ├── 13_git_branching_and_collaboration_guide.md
│   ├── 14_jenkins_ci_cd_setup_guide.md
│   ├── 15_selenium_test_plan.md
│   ├── 16_docker_lifecycle_guide.md
│   ├── 17_ansible_provisioning_guide.md
│   ├── 18_reliability_health_check_and_rollback_guide.md
│   ├── 19_local_setup_and_troubleshooting_guide.md
│   └── 20_evidence_checklist_and_matrix.md
├── src/
│   ├── main/
│   │   ├── java/com/healthsure/ # 43 Spring Boot Source Classes
│   │   └── resources/
│   │       ├── db/migration/    # Flyway V1 Schema & V2 Seed Data
│   │       ├── static/          # CSS, JS, and Brand Assets
│   │       ├── templates/       # 16 Thymeleaf UI Templates
│   │       └── application.yml  # Application Configuration Profiles
│   └── test/
│       ├── java/com/healthsure/ # 26 Unit Tests & 5 Selenium Journeys
│       └── resources/           # Test Application Config & Mockito Setup
├── docker-compose.yml           # App + MySQL Multi-Container Orchestration
├── Dockerfile                   # Multi-Stage Secure Docker Build
├── Jenkinsfile                  # 14-Stage Declarative CI/CD Pipeline
├── pom.xml                      # Maven Dependencies & Build Configuration
└── README.md                    # Project Documentation
```

---

## 📚 Complete Documentation Suite

All 20 required assignment deliverables with full technical descriptions and Mermaid diagrams are located in the [`docs/`](./docs) folder:
- [01 Problem Definition & Scope](./docs/01_problem_definition_and_scope.md)
- [08 Use Case Diagram](./docs/08_use_case_diagram.md)
- [09 Architecture Diagram](./docs/09_architecture_diagram.md)
- [10 ER Diagram](./docs/10_er_diagram.md)
- [11 REST API Documentation](./docs/11_api_documentation.md)
- [12 DevOps Lifecycle Diagram](./docs/12_devops_lifecycle_diagram.md)
- [14 Jenkins Setup Guide](./docs/14_jenkins_ci_cd_setup_guide.md)
- [15 Selenium Test Plan](./docs/15_selenium_test_plan.md)
- [16 Docker Lifecycle Guide](./docs/16_docker_lifecycle_guide.md)
- [18 Health Checks & Rollback Guide](./docs/18_reliability_health_check_and_rollback_guide.md)
- [20 Deliverables & Evidence Matrix](./docs/20_evidence_checklist_and_matrix.md)
