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

    /**
     * Setup executed before EACH test method
     * Initializes ConfigReader and WebDriver
     */
    @BeforeMethod(alwaysRun = true)  // ← IMPORTANTE: alwaysRun = true
    public void setUp() {
        System.out.println("=== TEST SETUP STARTED ===");
        
        // CRITICAL: Initialize configuration FIRST
        config = new ConfigReader();
        System.out.println("✅ ConfigReader initialized");
        
        // Initialize WebDriver using DriverFactory
        DriverFactory.initializeDriver(config);
        driver = DriverFactory.getDriver();
        
        System.out.println("✅ WebDriver initialized");
        System.out.println("=== TEST SETUP COMPLETED ===");
    }

    /**
     * Teardown executed after EACH test method
     * Closes WebDriver
     */
    @AfterMethod(alwaysRun = true)  // ← IMPORTANTE: alwaysRun = true
    public void tearDown() {
        System.out.println("=== TEST TEARDOWN STARTED ===");
        
        // Quit driver using DriverFactory
        DriverFactory.quitDriver();
        
        System.out.println("✅ WebDriver closed");
        System.out.println("=== TEST TEARDOWN COMPLETED ===");
    }
}