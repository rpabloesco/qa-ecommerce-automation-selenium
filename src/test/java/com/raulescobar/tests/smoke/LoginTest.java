package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.raulescobar.pages.LoginPom;
import com.raulescobar.tests.base.BaseTest;
import io.qameta.allure.*;
import java.io.ByteArrayInputStream;

@Epic("Authentication Module")
@Feature("Login Functionality - DemoBlaze")
public class LoginTest extends BaseTest {
    
    @Test(priority = 1, groups = {"smoke", "regression", "login"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can successfully login with valid credentials")
    @Story("Login Exitoso")
    public void testSuccessfulLogin() {
        // Navigate to DemoBlaze
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        LoginPom loginPage = new LoginPom(driver);
        
        // Verify we're on the home page
        Assert.assertTrue(loginPage.isLoginButtonVisible(),
            "Login button should be visible on home page");
        
        // Perform login
        String username = config.getEnv("username");
        String password = config.getEnv("password");
        
        loginPage.login(username, password);
        
        // Wait for login to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify successful login
        Assert.assertTrue(loginPage.isLoggedIn(),
            "User should be logged in - welcome message and logout button should be visible");
        
        // Verify welcome message contains username
        Assert.assertTrue(loginPage.verifyWelcomeMessage(username),
            "Welcome message should contain username: " + username);
        
        String welcomeText = loginPage.getWelcomeMessage();
        System.out.println("Welcome message: " + welcomeText);
        
        // Capture screenshot
        Allure.addAttachment("Successful Login - Home Page", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 2, groups = {"smoke", "negative", "login"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error alert is displayed with invalid credentials")
    @Story("Login Fallido")
    public void testFailedLogin() {
        // Navigate to DemoBlaze
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        LoginPom loginPage = new LoginPom(driver);
        
        // Try to login with invalid credentials
        loginPage.loginWithAlertHandling("invalid_user_xyz", "wrong_password_123");
        
        // Wait for alert
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Capture screenshot before handling alert
        Allure.addAttachment("Failed Login - Before Alert", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Verify alert is present and get text
        String alertText = loginPage.getAlertText();
        System.out.println("Alert message: " + alertText);
        
        Assert.assertTrue(alertText.contains("User does not exist") || 
                         alertText.contains("Wrong password"),
            "Alert should indicate invalid credentials. Actual: " + alertText);
        
        // Accept alert
        loginPage.acceptAlert();
        
        // Wait after accepting alert
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify user is NOT logged in
        Assert.assertFalse(loginPage.isLoggedIn(),
            "User should NOT be logged in after failed login attempt");
        
        // Close the modal
        loginPage.closeModal();
        
        // Capture final screenshot
        Allure.addAttachment("Failed Login - After Alert Handled", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 3, groups = {"smoke", "regression", "login"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can successfully logout from the application")
    @Story("Logout")
    public void testLogout() {
        // Navigate and login first
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        LoginPom loginPage = new LoginPom(driver);
        
        // Login
        String username = config.getEnv("username");
        String password = config.getEnv("password");
        
        loginPage.login(username, password);
        
        // Wait for login
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify logged in
        Assert.assertTrue(loginPage.isLoggedIn(),
            "User should be logged in before testing logout");
        
        // Capture before logout
        Allure.addAttachment("Before Logout - Logged In State", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Perform logout
        loginPage.logout();
        
        // Wait for logout to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify logout successful
        Assert.assertTrue(loginPage.isLoggedOut(),
            "User should be logged out - login button should be visible, logout button should be hidden");
        
        Assert.assertTrue(loginPage.isLoginButtonVisible(),
            "Login button should be visible after logout");
        
        // Capture after logout
        Allure.addAttachment("After Logout - Logged Out State", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 4, groups = {"smoke"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify home page loads correctly and login button is accessible")
    @Story("Page Load Validation")
    public void testHomePageLoad() {
        // Navigate to DemoBlaze
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        LoginPom loginPage = new LoginPom(driver);
        
        // Verify page title
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("STORE") || title.contains("PRODUCT STORE"), 
            "Page title should contain 'STORE'. Actual: " + title);
        
        // Verify login button is visible
        Assert.assertTrue(loginPage.isLoginButtonVisible(),
            "Login button should be visible on page load");
        
        // Capture screenshot
        Allure.addAttachment("Home Page Loaded", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
}