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
        driver.get(config.getEnv("baseUrl"));
        LoginPom loginPage = new LoginPom(driver);

        Assert.assertTrue(loginPage.isLoginButtonVisible(),
            "Login button should be visible on home page");

        String username = config.getEnv("username");
        String password = config.getEnv("password");

        // login() already waits for modal to close and welcome message to appear
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isLoggedIn(),
            "User should be logged in — welcome message and logout button should be visible");

        Assert.assertTrue(loginPage.verifyWelcomeMessage(username),
            "Welcome message should contain username: " + username);

        System.out.println("Welcome message: " + loginPage.getWelcomeMessage());

        Allure.addAttachment("Successful Login - Home Page",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 2, groups = {"smoke", "negative", "login"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error alert is displayed with invalid credentials")
    @Story("Login Fallido")
    public void testFailedLogin() {
        driver.get(config.getEnv("baseUrl"));
        LoginPom loginPage = new LoginPom(driver);

        // loginWithAlertHandling uses isAlertPresent() which already waits for alert
        loginPage.loginWithAlertHandling("invalid_user_xyz", "wrong_password_123");

        Allure.addAttachment("Failed Login - Before Alert",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String alertText = loginPage.getAlertText();
        System.out.println("Alert message: " + alertText);

        Assert.assertTrue(alertText.contains("User does not exist") ||
                         alertText.contains("Wrong password"),
            "Alert should indicate invalid credentials. Actual: " + alertText);

        loginPage.acceptAlert();

        Assert.assertFalse(loginPage.isLoggedIn(),
            "User should NOT be logged in after failed login attempt");

        loginPage.closeModal();

        Allure.addAttachment("Failed Login - After Alert Handled",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 3, groups = {"smoke", "regression", "login"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can successfully logout from the application")
    @Story("Logout")
    public void testLogout() {
        driver.get(config.getEnv("baseUrl"));
        LoginPom loginPage = new LoginPom(driver);

        String username = config.getEnv("username");
        String password = config.getEnv("password");

        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isLoggedIn(),
            "User should be logged in before testing logout");

        Allure.addAttachment("Before Logout - Logged In State",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        // logout() already waits for welcomeMessage to disappear
        loginPage.logout();

        Assert.assertTrue(loginPage.isLoggedOut(),
            "User should be logged out — login button visible, logout button hidden");

        Assert.assertTrue(loginPage.isLoginButtonVisible(),
            "Login button should be visible after logout");

        Allure.addAttachment("After Logout - Logged Out State",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 4, groups = {"smoke"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify home page loads correctly and login button is accessible")
    @Story("Page Load Validation")
    public void testHomePageLoad() {
        driver.get(config.getEnv("baseUrl"));
        LoginPom loginPage = new LoginPom(driver);

        String title = driver.getTitle();
        Assert.assertTrue(title.contains("STORE") || title.contains("PRODUCT STORE"),
            "Page title should contain 'STORE'. Actual: " + title);

        // isLoginButtonVisible() uses waitForVisibility internally — no sleep needed
        Assert.assertTrue(loginPage.isLoginButtonVisible(),
            "Login button should be visible on page load");

        Allure.addAttachment("Home Page Loaded",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }
}
