# Selenium WebDriver Automated Test Plan

This document details the test strategy, scope, execution procedure, and verification criteria for the Selenium WebDriver automated browser tests in **HealthSure**.

---

## 1. Test Scope & Journey Matrix

The automated Selenium test suite (`HealthSureSeleniumTest.java`) covers 5 essential end-to-end user journeys using headless Chrome:

| Journey ID | Title | Persona | Steps Covered | Expected Outcome |
|---|---|---|---|---|
| **TC-SEL-01** | Valid Claim Submission | Policyholder (`john.doe`) | 1. Log in via `/login`<br>2. Navigate to `/claims/new`<br>3. Fill policy, type, hospital, admission date, discharge date, amount, description<br>4. Submit form | Claim successfully created, redirected to `/claims/{id}` with status `SUBMITTED`. |
| **TC-SEL-02** | View Claims & Status Badge | Policyholder (`john.doe`) | 1. Navigate to `/claims`<br>2. Locate submitted claim in table<br>3. Inspect status badge | Claim row exists, displays accurate status badge (e.g. `SUBMITTED`, `APPROVED`), links to details. |
| **TC-SEL-03** | Reviewer Claim Approval | Claims Reviewer (`reviewer1`) | 1. Log in as reviewer<br>2. Navigate to `/reviewer/queue`<br>3. Open pending claim<br>4. Submit approval decision with notes | Status transitions to `APPROVED`, approval timestamp and reviewer recorded. |
| **TC-SEL-04** | Request Additional Information | Claims Reviewer (`reviewer1`) | 1. Open second pending claim in queue<br>2. Select `ADDITIONAL_INFO_REQUIRED`<br>3. Enter required notes<br>4. Submit decision | Status transitions to `ADDITIONAL_INFO_REQUIRED`, policyholder notified on dashboard. |
| **TC-SEL-05** | Role-Based Access Control Gate | Policyholder (`john.doe`) | 1. Log in as policyholder<br>2. Attempt direct GET `/admin/dashboard`<br>3. Attempt direct GET `/reviewer/queue` | Direct access denied; redirected to `/access-denied` (HTTP 403 status). |

---

## 2. Test Architecture & Reliability Principles

- **Driver Management:** Automated Chrome driver lifecycle managed via `Selenium WebDriver` using modern headless options (`--headless=new`, `--no-sandbox`, `--disable-dev-shm-usage`, `--remote-allow-origins=*`).
- **Explicit Synchronization:** Replaced arbitrary sleeps with `WebDriverWait` and `ExpectedConditions` (`visibilityOfElementLocated`, `urlContains`).
- **Clean Session Teardown:** Tests cleanly log out before deleting cookies, preventing session pollution across tests.
- **Artifact Capture on Failure:** Any unexpected assertion failure triggers automated page screenshot capture saved to `target/screenshots/`.

---

## 3. Running Selenium Tests Locally

### Running against the running application:

Ensure the app is running (e.g. on `http://localhost:8081` via Docker Compose or `http://localhost:8080` locally).

```powershell
# In PowerShell (quotes around parameter recommended):
mvn test -Pselenium "-Dapp.url=http://localhost:8081"
```

### Running with a specific browser or debug mode:
Set headless to false if local visual debugging is required:
```powershell
mvn test -Pselenium "-Dapp.url=http://localhost:8081" -Dselenium.headless=false
```

---

## 4. Test Reporting

Results are output in standard Surefire XML format:
`target/surefire-reports/TEST-com.healthsure.selenium.HealthSureSeleniumTest.xml`

Jenkins archives these reports and displays graphical pass/fail trends over time.
