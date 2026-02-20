package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.raulescobar.core.BasePage;

public class LoginPom extends BasePage{

    @FindBy(id = "username")
    WebElement username;

    @FindBy(id = "password")
    WebElement password;

    @FindBy(id = "login-button")
    WebElement loginButton;

    public LoginPom(WebDriver driver) {
        super(driver);
    }

    public void getUrl(String url) {
        driver.get(url);
    }

    public void setUsername(String username) {
        this.username.sendKeys(username);
    }

    public void setPassword(String password) {
        this.password.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickLoginButton();
    }
    
}
