package com.raulescobar.tests.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import com.raulescobar.config.ConfigReader;
import com.raulescobar.driver.DriverFactory;

/**
 * Base test class for all test classes
 * Handles WebDriver setup and teardown
 */
public class BaseTest {
    
    public WebDriver driver;
    protected ConfigReader config;

    @BeforeMethod
    public void setUp() {
        System.out.println("=== TEST SETUP STARTED ===");
        
        // Initialize configuration
        config = new ConfigReader();
        
        // Initialize WebDriver using DriverFactory
        DriverFactory.initializeDriver(config);
        driver = DriverFactory.getDriver();
        
        System.out.println("=== DRIVER INITIALIZED SUCCESSFULLY ===");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("=== TEST TEARDOWN STARTED ===");
        
        // Quit driver using DriverFactory
        DriverFactory.quitDriver();
        
        System.out.println("=== DRIVER CLOSED SUCCESSFULLY ===");
    }
}