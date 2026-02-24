package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

public class LoginPom extends BasePage {

    // Navbar elements
    @FindBy(id = "login2")
    private WebElement loginMenuLink;

    @FindBy(id = "logout2")
    private WebElement logoutLink;

    @FindBy(id = "nameofuser")
    private WebElement welcomeMessage;

    // Login Modal elements
    @FindBy(id = "loginusername")
    private WebElement usernameInput;

    @FindBy(id = "loginpassword")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@onclick='logIn()']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@id='logInModal']//button[text()='Close']")
    private WebElement closeModalButton;

    public LoginPom(WebDriver driver) {
        super(driver);
    }

    @Step("Open login modal")
    public LoginPom openLoginModal() {
        click(loginMenuLink);
        waitForVisibility(usernameInput);
        return this;
    }

    @Step("Enter username: {username}")
    public LoginPom enterUsername(String username) {
        sendKeys(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public LoginPom enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }

    @Step("Click login button")
    public LoginPom clickLoginButton() {
        click(loginButton);
        return this;
    }

    @Step("Login with username: {username}")
    public void login(String username, String password) {
        openLoginModal();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        
        // Wait for modal to close (login successful)
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginButton));
        } catch (Exception e) {
            // If modal doesn't close, there might be an alert
        }
    }

    @Step("Login with credentials and handle alert")
    public void loginWithAlertHandling(String username, String password) {
        openLoginModal();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        
        //For alert
        try {
            Thread.sleep(500);
            // Check if there's an alert (wrong credentials)
            if (isAlertPresent()) {
                // Alert will be handled by test
                return;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for successful login (modal closes)
        wait.until(ExpectedConditions.invisibilityOf(loginButton));
    }

    @Step("Verify user is logged in")
    public boolean isLoggedIn() {
        try {
            return isDisplayed(welcomeMessage) && isDisplayed(logoutLink);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get welcome message")
    public String getWelcomeMessage() {
        return getText(welcomeMessage);
    }

    @Step("Verify username in welcome message: {expectedUsername}")
    public boolean verifyWelcomeMessage(String expectedUsername) {
        String welcomeText = getWelcomeMessage();
        return welcomeText.contains(expectedUsername);
    }

    @Step("Click logout")
    public void logout() {
        click(logoutLink);
        // Wait for logout to complete (welcome message disappears)
        wait.until(ExpectedConditions.invisibilityOf(welcomeMessage));
    }

    @Step("Verify user is logged out")
    public boolean isLoggedOut() {
        try {
            return isDisplayed(loginMenuLink) && !isDisplayed(logoutLink);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Close login modal")
    public void closeModal() {
        click(closeModalButton);
        wait.until(ExpectedConditions.invisibilityOf(usernameInput));
    }

    /**
     * Helper method to check if alert is present
     */
    private boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify login button is visible in navbar")
    public boolean isLoginButtonVisible() {
        return isDisplayed(loginMenuLink);
    }
}