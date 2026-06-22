package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

public class SignUpPage extends BasePage {

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

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open signup modal")
    public SignUpPage openSignUpModal() {
        click(signUpMenuLink);
        waitForVisibility(usernameInput);
        return this;
    }

    @Step("Enter username: {username}")
    public SignUpPage enterUsername(String username) {
        sendKeys(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public SignUpPage enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }

    @Step("Click signup button")
    public SignUpPage clickSignUpButton() {
        click(signUpButton);
        return this;
    }

    @Step("Sign up with username: {username}")
    public void signUp(String username, String password) {
        openSignUpModal();
        enterUsername(username);
        enterPassword(password);
        clickSignUpButton();
    }

    @Step("Close signup modal")
    public void closeModal() {
        click(closeModalButton);
    }
}
