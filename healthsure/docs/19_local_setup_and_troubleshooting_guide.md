# Local Setup, Development, and Troubleshooting Guide

This guide details how to run, develop, and troubleshoot the **HealthSure** application on Windows 11, macOS, or Linux.

---

## 1. Prerequisites

- **Java Development Kit (JDK 21 or higher)** (Java 21/25 installed)
- **Apache Maven 3.9+**
- **Docker Desktop** (version 24+ with Compose v2)
- **Google Chrome** (for automated Selenium browser testing)

---

## 2. Quick Start: Running with Docker Compose (Recommended)

1. Open PowerShell / Terminal in `c:\Users\arpit\Desktop\College\DevOps\healthsure`.
2. Start the application and database:
   ```bash
   docker compose up -d --build
   ```
3. Wait 30 seconds for MySQL initialization and Spring Boot startup.
4. Verify containers:
   ```bash
   docker compose ps
   ```
5. Open your browser:
   - Application URL: [http://localhost:8081](http://localhost:8081)
   - Actuator Health: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)

---

## 3. Alternative: Local Development Workflow (Without Docker App)

If you prefer to run the Spring Boot app directly on your host machine while keeping MySQL in Docker:

1. Start only the MySQL container:
   ```bash
   docker compose up -d healthsure-mysql
   ```
2. Run the Spring Boot application using Maven:
   ```powershell
   mvn spring-boot:run
   ```
3. Access the application on [http://localhost:8080](http://localhost:8080).

---

## 4. Pre-Seeded Demonstration Accounts

| Role | Username | Password | Purpose |
|---|---|---|---|
| **System Administrator** | `admin` | `Admin@1234` | Manage users, policies, audit events |
| **Claims Reviewer** | `reviewer1` | `Review@1234` | Review claims, approve, reject, request info |
| **Senior Reviewer** | `reviewer2` | `Review@1234` | Secondary reviewer access |
| **Policyholder** | `john.doe` | `User@1234` | Submit claims, view claim timeline, upload documents |
| **Policyholder** | `jane.smith` | `User@1234` | Submit claims, view claims |

---

## 5. Troubleshooting Common Issues

### Issue 1: Port 8080 Already in Use
- **Cause:** Jenkins or another service is listening on port 8080.
- **Solution:** In `docker-compose.yml`, the application port is pre-mapped to `8081:8080`. Always access the containerized application on `http://localhost:8081`.

### Issue 2: Mockito ByteBuddy / MockMaker Error on Java 25
- **Cause:** JDK 25 blocks dynamic agent bytecode instrumentation by default.
- **Solution:** We configured `src/test/resources/META-INF/mockito-extensions/org.mockito.plugins.MockMaker` (`mock-maker-subclass`) and passed `--add-opens java.base/java.lang=ALL-UNNAMED` in `pom.xml`.

### Issue 3: Jenkins Pipeline "Cannot run program sh" on Windows
- **Cause:** Jenkins Windows service runs on cmd/PowerShell where `sh` is absent.
- **Solution:** The pipeline uses `isUnix()` branching, executing `bat` commands seamlessly on Windows.

### Issue 4: Selenium WebDriver Chrome Mismatch
- **Cause:** Local Chrome version differs from driver binary.
- **Solution:** Selenium 4.20+ includes built-in Selenium Manager that auto-discovers and provisions the matching ChromeDriver. Ensure Chrome is installed.
