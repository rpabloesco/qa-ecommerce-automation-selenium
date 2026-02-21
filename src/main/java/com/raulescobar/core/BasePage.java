package com.raulescobar.core;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;
import java.time.Duration;

/**
 * Base class for all Page Objects
 * Contains common methods and utilities used across all pages
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    
    /**
     * Constructor that initializes PageFactory and common utilities
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for element to be visible
     */
    @Step("Wait for element to be visible")
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be clickable
     */
    @Step("Wait for element to be clickable")
    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Click element with wait
     */
    @Step("Click element")
    protected void click(WebElement element) {
        waitForClickable(element).click();
    }
    
    /**
     * Send keys to element with wait
     */
    @Step("Enter text into field")
    protected void sendKeys(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Get text from element
     */
    @Step("Get element text")
    protected String getText(WebElement element) {
        return waitForVisibility(element).getText();
    }
    
    /**
     * Check if element is displayed
     */
    @Step("Verify element is displayed")
    protected boolean isDisplayed(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for URL to contain specific text
     */
    @Step("Wait for URL to contain: {urlFraction}")
    protected boolean waitForUrlContains(String urlFraction) {
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }
    
    /**
     * Wait for page title to contain text
     */
    @Step("Wait for title to contain: {title}")
    protected boolean waitForTitleContains(String title) {
        return wait.until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Handle JavaScript Alert - Accept
     */
    @Step("Accept JavaScript alert")
    public void acceptAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }
    
    /**
     * Handle JavaScript Alert - Dismiss
     */
    @Step("Dismiss JavaScript alert")
    protected void dismissAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.dismiss();
    }
    
    /**
     * Get JavaScript Alert text
     */
    @Step("Get alert text")
    public String getAlertText() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        return alert.getText();
    }
    
    /**
     * Scroll to element
     */
    @Step("Scroll to element")
    protected void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Click using JavaScript (for stubborn elements)
     */
    @Step("Click element using JavaScript")
    protected void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }
    
    /**
     * Wait for page to load completely
     */
    @Step("Wait for page load")
    protected void waitForPageLoad() {
        wait.until(driver -> 
            js.executeScript("return document.readyState").equals("complete")
        );
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
}