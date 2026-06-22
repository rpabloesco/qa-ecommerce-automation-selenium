package com.raulescobar.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitHelper {
    
    private final WebDriverWait wait;
    
    public WaitHelper(WebDriver driver, int timeoutSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    
    public WaitHelper(WebDriver driver) {
        this(driver, 10);
    }
    
    public void waitForPageLoad(WebDriver driver) {
        wait.until(d -> ((org.openqa.selenium.JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }
    
    public void waitForAjax(WebDriver driver) {
        wait.until(d -> ((org.openqa.selenium.JavascriptExecutor) d).executeScript("return jQuery.active == 0"));
    }
    
    public void customWait(ExpectedCondition<?> condition) {
        wait.until(condition);
    }
}