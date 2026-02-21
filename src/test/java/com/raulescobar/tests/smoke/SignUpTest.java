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
    
    @Test(priority = 1, groups = {"smoke", "signup"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can successfully sign up with unique credentials")
    @Story("Sign Up Exitoso")
    public void testSuccessfulSignUp() {
        String baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        SignUpPom signUpPage = new SignUpPom(driver);
        
        // AUTO-GENERATE unique username (no manual setup required)
        String userPrefix = config.getEnv("newUserPrefix");
        String password = config.getEnv("newUserPassword");
        long timestamp = System.currentTimeMillis();
        String username = userPrefix + "_" + timestamp;
        
        Allure.step("Creating unique test user: " + username);
        System.out.println("Auto-generated credentials:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        // Perform sign up
        signUpPage.signUp(username, password);
        
        // Wait and verify
        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) {}
        
        String alertText = signUpPage.getAlertText();
        
        Assert.assertTrue(alertText.contains("Sign up successful"),
            "Sign up should be successful. Alert: " + alertText);
        
        signUpPage.acceptAlert();
        
        Allure.addAttachment("Sign Up Successful", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        System.out.println("User created successfully - ready for login tests");
    }
    
}