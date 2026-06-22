package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.SignUpPage;
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
        driver.get(config.getEnv("baseUrl"));
        SignUpPage signUpPage = new SignUpPage(driver);

        String username = config.getEnv("newUserPrefix") + "_" + System.currentTimeMillis();
        String password = config.getEnv("newUserPassword");

        System.out.println("Creating new user: " + username);

        signUpPage.signUp(username, password);

        Allure.addAttachment("Sign Up Form Submitted",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String alertText = signUpPage.getAlertText();
        System.out.println("Alert message: " + alertText);

        Assert.assertTrue(alertText.contains("Sign up successful"),
            "Alert should indicate successful sign up. Actual: " + alertText);

        signUpPage.acceptAlert();

        Allure.addAttachment("Sign Up Successful",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        System.out.println("User created: " + username);
    }

    @Test(priority = 2, groups = {"signup", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error message when trying to sign up with existing username")
    @Story("Sign Up con Usuario Duplicado")
    public void testSignUpWithExistingUser() {
        driver.get(config.getEnv("baseUrl"));
        SignUpPage signUpPage = new SignUpPage(driver);

        String existingUsername = config.getEnv("username");
        System.out.println("Attempting to sign up with existing user: " + existingUsername);

        signUpPage.signUp(existingUsername, config.getEnv("password"));

        Allure.addAttachment("Sign Up with Duplicate User",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String alertText = signUpPage.getAlertText();
        System.out.println("Alert message: " + alertText);

        Assert.assertTrue(alertText.contains("This user already exist"),
            "Alert should indicate user already exists. Actual: " + alertText);

        signUpPage.acceptAlert();
    }

    @Test(priority = 3, groups = {"signup", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error when submitting empty sign up form")
    @Story("Sign Up con Campos Vacíos")
    public void testSignUpWithEmptyFields() {
        driver.get(config.getEnv("baseUrl"));
        SignUpPage signUpPage = new SignUpPage(driver);

        System.out.println("Attempting sign up with empty fields");

        signUpPage.signUp("", "");

        Allure.addAttachment("Sign Up with Empty Fields",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String alertText = signUpPage.getAlertText();
        System.out.println("Alert message: " + alertText);

        Assert.assertTrue(alertText.contains("Please fill out Username and Password"),
            "Alert should indicate required fields. Actual: " + alertText);

        signUpPage.acceptAlert();
    }
}
