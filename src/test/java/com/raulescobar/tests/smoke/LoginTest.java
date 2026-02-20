package com.raulescobar.tests.smoke;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.LoginPom;
import io.qameta.allure.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.ByteArrayInputStream;

@Epic("Authentication Module")
@Feature("Login Functionality")
public class LoginTest {
    
    private WebDriver driver;
    
    @BeforeMethod
    public void setUp() {
        System.out.println("=== SETUP STARTED ===");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        System.out.println("=== DRIVER CREATED: " + driver + " ===");
    }
    
    @AfterMethod
    public void tearDown() {
        System.out.println("=== TEARDOWN STARTED ===");
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test(priority = 1, groups = {"smoke", "regression"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can successfully login with valid credentials")
    @Story("User Login - Happy Path")
    public void testValidLogin() {
        driver.get("https://www.saucedemo.com/");
        
        LoginPom loginPage = new LoginPom(driver);
        loginPage.login("standard_user", "secret_sauce");
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"), 
            "User should be on inventory page after login");
        
        Allure.addAttachment("Login Success", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 2, groups = {"smoke", "negative"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error message is displayed with invalid credentials")
    @Story("User Login - Negative Scenario")
    public void testInvalidLogin() {
        driver.get("https://www.saucedemo.com/");
        
        LoginPom loginPage = new LoginPom(driver);
        loginPage.login("invalid_user", "wrong_password");
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be visible");
        
        Allure.addAttachment("Login Error", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
    }
    
    @Test(priority = 3, groups = {"smoke"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login page loads correctly")
    @Story("Page Load Validation")
    public void testLoginPageLoad() {
        driver.get("https://www.saucedemo.com/");
        
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Swag Labs"), 
            "Page title should contain 'Swag Labs'");
        
        LoginPom loginPage = new LoginPom(driver);
        Assert.assertTrue(loginPage.isLoginButtonEnabled(),
            "Login button should be enabled on page load");
    }
}