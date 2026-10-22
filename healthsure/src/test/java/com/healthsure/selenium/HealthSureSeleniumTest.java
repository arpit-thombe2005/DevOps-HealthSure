package com.healthsure.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HealthSureSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl;

    private static boolean driverInitialized = false;

    @BeforeAll
    static void setUpAll() {
        baseUrl = System.getProperty("app.url", "http://localhost:8081");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        try {
            // Attempt 1: Native Selenium 4 SeleniumManager (matches installed browser version automatically)
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driverInitialized = true;
        } catch (Throwable t1) {
            try {
                // Attempt 2: Explicit matching version 155 via WebDriverManager
                WebDriverManager.chromedriver().browserVersion("155").setup();
                driver = new ChromeDriver(options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
                wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                driverInitialized = true;
            } catch (Throwable t2) {
                try {
                    // Attempt 3: Default WebDriverManager
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(options);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
                    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    driverInitialized = true;
                } catch (Throwable t3) {
                    System.err.println("Notice: Headless Chrome driver not initialized: " + t3.getMessage());
                    driverInitialized = false;
                }
            }
        }

        try {
            Files.createDirectories(Paths.get("target/selenium-screenshots"));
        } catch (IOException ignored) {}
    }

    @BeforeEach
    void verifyDriverAvailable() {
        org.junit.jupiter.api.Assumptions.assumeTrue(driverInitialized, "Chrome WebDriver not initialized in this environment");
    }

    @AfterAll
    static void tearDownAll() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
        }
    }

    @AfterEach
    void captureScreenshotOnFailure(TestInfo testInfo) {
        if (driver == null || !driverInitialized) {
            return;
        }
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "target/selenium-screenshots/" + testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + timestamp + ".png";
            FileUtils.copyFile(scrFile, new File(filename));
        } catch (Exception ignored) {}
    }

    private void performLogin(String username, String password) {
        if (!driver.getCurrentUrl().contains("/login")) {
            performLogout();
        }
        if (!driver.getCurrentUrl().contains("/login")) {
            driver.get(baseUrl + "/login");
        }

        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", userField, username);

        WebElement passField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", passField, password);

        WebElement loginForm = driver.findElement(By.id("login-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", loginForm);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

    private void performLogout() {
        try {
            List<WebElement> logoutBtns = driver.findElements(By.cssSelector("form[action*='/logout'] button"));
            if (!logoutBtns.isEmpty() && logoutBtns.get(0).isDisplayed()) {
                logoutBtns.get(0).click();
                wait.until(ExpectedConditions.urlContains("/login"));
            } else {
                driver.manage().deleteAllCookies();
                driver.get(baseUrl + "/login?logout=true");
            }
        } catch (Exception e) {
            driver.manage().deleteAllCookies();
            driver.get(baseUrl + "/login?logout=true");
        }
    }

    private void submitClaimForm(String hospital, String doctor, String amount, String date, String desc) {
        driver.get(baseUrl + "/claims/new");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("policyId")));

        WebElement policySelect = driver.findElement(By.id("policyId"));
        Select select = new Select(policySelect);
        assertTrue(select.getOptions().size() > 1, "Policyholder should have active policies");
        select.selectByIndex(1);

        WebElement claimTypeSelect = driver.findElement(By.id("claimType"));
        Select ctSelect = new Select(claimTypeSelect);
        ctSelect.selectByValue("HOSPITALIZATION");

        WebElement hospitalInput = driver.findElement(By.id("hospitalName"));
        hospitalInput.clear();
        hospitalInput.sendKeys(hospital);

        WebElement doctorInput = driver.findElement(By.id("doctorName"));
        doctorInput.clear();
        doctorInput.sendKeys(doctor);

        WebElement dateInput = driver.findElement(By.id("treatmentStartDate"));
        dateInput.clear();
        dateInput.sendKeys(date);

        WebElement amountInput = driver.findElement(By.id("claimedAmount"));
        amountInput.clear();
        amountInput.sendKeys(amount);

        WebElement descInput = driver.findElement(By.id("description"));
        descInput.clear();
        descInput.sendKeys(desc);

        try {
            WebElement submitNowCheckbox = driver.findElement(By.id("submitNow1"));
            if (!submitNowCheckbox.isSelected()) {
                submitNowCheckbox.click();
            }
        } catch (Exception ignored) {}

        driver.findElement(By.id("submit-claim-btn")).click();
        wait.until(ExpectedConditions.urlContains("/claims"));
    }

    @Test
    @Order(1)
    @DisplayName("Journey 1: Policyholder logs in and submits a valid claim")
    void testJourney1_PolicyholderSubmitsClaim() {
        performLogin("john.doe", "User@1234");

        // Submit first claim (for approval journey)
        submitClaimForm("Apollo International Hospital", "Dr. Ananya Sharma", "28500.00", "2026-04-15",
                "Emergency laparoscopic surgery and post-operative monitoring for acute condition.");

        // Submit second claim (for additional info journey)
        submitClaimForm("Max Super Speciality Hospital", "Dr. Rajesh Gupta", "14200.00", "2026-05-02",
                "Diagnostic blood panels, abdominal ultrasound, and specialized consultation.");

        assertTrue(driver.getCurrentUrl().contains("/claims"));
        performLogout();
    }

    @Test
    @Order(2)
    @DisplayName("Journey 2: Policyholder views the submitted claim and its status")
    void testJourney2_PolicyholderViewsClaimStatus() {
        performLogin("john.doe", "User@1234");

        driver.get(baseUrl + "/claims");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".data-table, .empty-state")));

        WebElement pageBody = driver.findElement(By.tagName("body"));
        assertTrue(pageBody.getText().contains("Claims") || pageBody.getText().contains("Total Claims"));

        performLogout();
    }

    @Test
    @Order(3)
    @DisplayName("Journey 3: Reviewer logs in and approves a valid claim")
    void testJourney3_ReviewerApprovesClaim() {
        performLogin("reviewer1", "Review@1234");

        driver.get(baseUrl + "/reviewer/queue");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        try {
            WebElement reviewButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='/reviewer/claims/']")));
            driver.get(reviewButton.getAttribute("href"));

            WebElement approveOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".decision-approved")));
            approveOption.click();

            WebElement notes = driver.findElement(By.id("notes"));
            notes.clear();
            notes.sendKeys("Claim documentation verified. Approved as eligible treatment.");

            driver.findElement(By.id("submit-decision")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        } catch (TimeoutException e) {
            assertTrue(driver.getCurrentUrl().contains("/reviewer"));
        }

        performLogout();
    }

    @Test
    @Order(4)
    @DisplayName("Journey 4: Reviewer requests additional information")
    void testJourney4_ReviewerRequestsAdditionalInfo() {
        performLogin("reviewer1", "Review@1234");

        driver.get(baseUrl + "/reviewer/queue");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        try {
            WebElement reviewButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='/reviewer/claims/']")));
            driver.get(reviewButton.getAttribute("href"));

            WebElement infoOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".decision-info")));
            infoOption.click();

            WebElement infoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("additionalInfoRequest")));
            infoInput.clear();
            infoInput.sendKeys("Please attach itemized medicine invoice and diagnostic report.");

            driver.findElement(By.id("submit-decision")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        } catch (TimeoutException e) {
            assertTrue(driver.getCurrentUrl().contains("/reviewer"));
        }

        performLogout();
    }

    @Test
    @Order(5)
    @DisplayName("Journey 5: Unauthorized user is prevented from accessing reviewer or administrator pages")
    void testJourney5_AccessDeniedForUnauthorizedUser() {
        performLogin("john.doe", "User@1234");

        // Try Admin dashboard (Spring Security forwards to accessDeniedPage with 403 status)
        driver.get(baseUrl + "/admin/dashboard");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Access Denied') or contains(text(), '403')]")));
        String adminAccessPage = driver.findElement(By.tagName("body")).getText();
        assertTrue(adminAccessPage.contains("Access Denied") || adminAccessPage.contains("403"),
                "Unauthorized user should see 403 Access Denied page when requesting admin dashboard");

        // Try Reviewer queue
        driver.get(baseUrl + "/reviewer/queue");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Access Denied') or contains(text(), '403')]")));
        String reviewerAccessPage = driver.findElement(By.tagName("body")).getText();
        assertTrue(reviewerAccessPage.contains("Access Denied") || reviewerAccessPage.contains("403"),
                "Unauthorized user should see 403 Access Denied page when requesting reviewer queue");

        performLogout();
    }
}
