package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.SignUpPom;
import com.raulescobar.tests.base.BaseTest;
import io.qameta.allure.*;
import java.io.ByteArrayInputStream;

@Epic("Authentication Module")
@Feature("Sign Up Functionality")
public class SignUpTest extends BaseTest {
    
    @Test(priority = 1, groups = {"signup", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can successfully sign up with unique credentials")
    @Story("Sign Up Exitoso")
    public void testSuccessfulSignUp() {
        // Navigate to DemoBlaze
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        SignUpPom signUpPage = new SignUpPom(driver);
        
        // Get password from config
        String password = config.getEnv("newUserPassword");
        
        // Generate unique username using timestamp
        String userPrefix = config.getEnv("newUserPrefix");
        long timestamp = System.currentTimeMillis();
        String username = userPrefix + "_" + timestamp;
        
        System.out.println("Creating new user:");
        System.out.println("   Username: " + username);
        System.out.println("   Password: " + password);
        
        // Perform sign up
        signUpPage.signUp(username, password);
        
        // Wait for alert
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Capture screenshot before handling alert
        Allure.addAttachment("Sign Up Form Submitted", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Get alert text
        String alertText = signUpPage.getAlertText();
        System.out.println("Alert message: " + alertText);
        
        // Verify success message
        Assert.assertTrue(alertText.contains("Sign up successful"),
            "Alert should indicate successful sign up. Actual: " + alertText);
        
        // Accept alert
        signUpPage.acceptAlert();
        
        // Capture success screenshot
        Allure.addAttachment("Sign Up Successful", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        System.out.println("User created successfully!");
        System.out.println("To use this user in other tests, update qa.properties:");
        System.out.println("   username=" + username);
    }
    
    @Test(priority = 2, groups = {"signup", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error message when trying to sign up with existing username")
    @Story("Sign Up con Usuario Duplicado")
    public void testSignUpWithExistingUser() {
        // Navigate to DemoBlaze
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        SignUpPom signUpPage = new SignUpPom(driver);
        
        // Use EXISTING user credentials from config
        String existingUsername = config.getEnv("username");
        String password = config.getEnv("password");
        
        System.out.println("Attempting to sign up with existing user:");
        System.out.println("   Username: " + existingUsername);
        
        // Try to sign up with existing credentials
        signUpPage.signUp(existingUsername, password);
        
        // Wait for alert
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Capture screenshot
        Allure.addAttachment("Sign Up with Duplicate User", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Get alert text
        String alertText = signUpPage.getAlertText();
        System.out.println("Alert message: " + alertText);
        
        // Verify error message
        Assert.assertTrue(alertText.contains("This user already exist"),
            "Alert should indicate user already exists. Actual: " + alertText);
        
        // Accept alert
        signUpPage.acceptAlert();
        
        System.out.println("Duplicate user validation working correctly");
    }
    
    @Test(priority = 3, groups = {"signup", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error when submitting empty sign up form")
    @Story("Sign Up con Campos Vacíos")
    public void testSignUpWithEmptyFields() {
        // Navigate to DemoBlaze
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        SignUpPom signUpPage = new SignUpPom(driver);
        
        System.out.println("Attempting sign up with empty fields");
        
        // Try to sign up with empty fields
        signUpPage.signUp("", "");
        
        // Wait for alert
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Capture screenshot
        Allure.addAttachment("Sign Up with Empty Fields", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Get alert text
        String alertText = signUpPage.getAlertText();
        System.out.println("Alert message: " + alertText);
        
        // Verify error message
        Assert.assertTrue(alertText.contains("Please fill out Username and Password"),
            "Alert should indicate required fields. Actual: " + alertText);
        
        // Accept alert
        signUpPage.acceptAlert();
        
        System.out.println("Empty fields validation working correctly");
    }
}