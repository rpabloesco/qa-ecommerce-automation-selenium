package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

public class SignUpPom extends BasePage {

    @FindBy(id = "signin2")
    private WebElement signUpMenuLink;

    @FindBy(id = "sign-username")
    private WebElement usernameInput;

    @FindBy(id = "sign-password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@onclick='register()']")
    private WebElement signUpButton;

    @FindBy(xpath = "//div[@id='signInModal']//button[text()='Close']")
    private WebElement closeModalButton;

    public SignUpPom(WebDriver driver) {
        super(driver);
    }

    @Step("Open signup modal")
    public SignUpPom openSignUpModal() {
        click(signUpMenuLink);
        waitForVisibility(usernameInput);
        return this;
    }

    @Step("Enter username: {username}")
    public SignUpPom enterUsername(String username) {
        sendKeys(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public SignUpPom enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }

    @Step("Click signup button")
    public SignUpPom clickSignUpButton() {
        click(signUpButton);
        return this;
    }

    @Step("Sign up with username: {username}")
    public void signUp(String username, String password) {
        openSignUpModal();
        enterUsername(username);
        enterPassword(password);
        clickSignUpButton();
        // Alert is handled by BasePage.getAlertText() which already waits for alertIsPresent
    }

    @Step("Close signup modal")
    public void closeModal() {
        click(closeModalButton);
    }
}
