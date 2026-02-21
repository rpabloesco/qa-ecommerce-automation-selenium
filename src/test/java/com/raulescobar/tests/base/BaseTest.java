package com.raulescobar.tests.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import com.raulescobar.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    public WebDriver driver;
    protected ConfigReader config;

    @BeforeMethod
    public void setUp() {
        System.out.println("=== SETUP STARTED ===");
        
        // Initialize config
        config = new ConfigReader();
        
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        driver = new ChromeDriver(options);
        
        System.out.println("=== DRIVER CREATED ===");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("=== TEARDOWN STARTED ===");
        if (driver != null) {
            driver.quit();
            System.out.println("=== DRIVER CLOSED ===");
        }
    }
}