package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.LoginPom;
import com.raulescobar.tests.base.BaseTest;
import com.raulescobar.utils.WaitHelper;
import io.qameta.allure.*;
import java.io.ByteArrayInputStream;

@Epic("Authentication Module")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {
    
    @Test(priority = 1, groups = {"smoke", "regression"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can successfully login with valid credentials")
    @Story("User Login - Happy Path")
    public void testValidLogin() {
        // Navigate to application
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        // Initialize helpers
        WaitHelper waitHelper = new WaitHelper(driver);
        LoginPom loginPage = new LoginPom(driver);
        
        // Login with valid credentials
        String username = config.getEnv("username");
        String password = config.getEnv("password");
        loginPage.login(username, password);
        
        // Wait for successful navigation (PROFESIONAL)
        waitHelper.waitForUrlContains("inventory.html");
        
        // Verify successful login
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"), 
            "User should be on inventory page after login. Current URL: " + currentUrl);
        
        // Capture screenshot
        Allure.addAttachment("Login Success Page", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 2, groups = {"smoke", "negative"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error message is displayed with invalid credentials")
    @Story("User Login - Negative Scenario")
    public void testInvalidLogin() {
        // Navigate to application
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        // Login with invalid credentials
        LoginPom loginPage = new LoginPom(driver);
        loginPage.login("invalid_user", "wrong_password");
        
        // Verify error message (el wait ya está en LoginPom.isErrorMessageDisplayed)
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be visible");
        
        String errorText = loginPage.getErrorMessage();
        Assert.assertTrue(errorText.contains("Epic sadface"), 
            "Error message should contain 'Epic sadface'. Actual: " + errorText);
        
        // Capture screenshot
        Allure.addAttachment("Login Error Message", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 3, groups = {"smoke"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login page loads correctly")
    @Story("Page Load Validation")
    public void testLoginPageLoad() {
        // Navigate to application
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        // Initialize helpers
        WaitHelper waitHelper = new WaitHelper(driver);
        
        // Wait for page to load completely 
        waitHelper.waitForPageLoad();
        waitHelper.waitForTitleContains("Swag Labs");
        
        // Verify page title
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Swag Labs"), 
            "Page title should contain 'Swag Labs'. Actual: " + title);
        
        // Verify login button state
        LoginPom loginPage = new LoginPom(driver);
        Assert.assertTrue(loginPage.isLoginButtonEnabled(),
            "Login button should be enabled on page load");
        
        // Capture screenshot
        Allure.addAttachment("Login Page Loaded", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
}