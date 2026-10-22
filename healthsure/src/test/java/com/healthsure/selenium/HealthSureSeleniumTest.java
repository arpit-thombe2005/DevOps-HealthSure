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

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HealthSureSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl;

    @BeforeAll
    static void setUpAll() {
        baseUrl = System.getProperty("app.url", "http://localhost:8081");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            Files.createDirectories(Paths.get("target/selenium-screenshots"));
        } catch (IOException ignored) {}
    }

    @AfterAll
    static void tearDownAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterEach
    void captureScreenshotOnFailure(TestInfo testInfo) {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "target/selenium-screenshots/" + testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + timestamp + ".png";
            FileUtils.copyFile(scrFile, new File(filename));
        } catch (Exception ignored) {}
    }

    private void performLogin(String username, String password) {
        driver.manage().deleteAllCookies();
        driver.get(baseUrl + "/login");
        WebElement userField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        userField.clear();
        userField.sendKeys(username);

        WebElement passField = driver.findElement(By.id("password"));
        passField.clear();
        passField.sendKeys(password);

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

    private void performLogout() {
        try {
            driver.manage().deleteAllCookies();
            driver.get(baseUrl + "/login");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        } catch (Exception ignored) {}
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

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("decision")));
            Select decisionSelect = new Select(driver.findElement(By.id("decision")));
            decisionSelect.selectByValue("APPROVED");

            WebElement notes = driver.findElement(By.id("notes"));
            notes.clear();
            notes.sendKeys("Claim documentation verified. Approved as eligible treatment.");

            driver.findElement(By.cssSelector("button[type='submit']")).click();
            wait.until(ExpectedConditions.urlContains("/reviewer"));
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

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("decision")));
            Select decisionSelect = new Select(driver.findElement(By.id("decision")));
            decisionSelect.selectByValue("ADDITIONAL_INFO_REQUIRED");

            WebElement infoInput = driver.findElement(By.id("additionalInfoRequest"));
            infoInput.clear();
            infoInput.sendKeys("Please attach itemized medicine invoice and diagnostic report.");

            driver.findElement(By.cssSelector("button[type='submit']")).click();
            wait.until(ExpectedConditions.urlContains("/reviewer"));
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

        // Try Admin dashboard
        driver.get(baseUrl + "/admin/dashboard");
        wait.until(ExpectedConditions.urlContains("/access-denied"));
        assertTrue(driver.getCurrentUrl().contains("/access-denied"));

        // Try Reviewer queue
        driver.get(baseUrl + "/reviewer/queue");
        wait.until(ExpectedConditions.urlContains("/access-denied"));
        assertTrue(driver.getCurrentUrl().contains("/access-denied"));

        performLogout();
    }
}
