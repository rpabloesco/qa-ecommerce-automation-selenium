package com.raulescobar.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitHelper {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public WaitHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public boolean waitForUrlContains(String urlFraction) {
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }
    
    /**
     * Wait for title to contain specific text
     */
    public boolean waitForTitleContains(String title) {
        return wait.until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        wait.until(driver -> 
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete")
        );
    }

    /**
     * Wait for element to be invisible
     */
    public boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Wait for text to be present in element
     */
    public boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Wait for alert to be present
     */
    public org.openqa.selenium.Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}