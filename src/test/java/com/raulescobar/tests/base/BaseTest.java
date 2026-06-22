package com.raulescobar.tests.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.raulescobar.config.ConfigReader;
import com.raulescobar.driver.DriverFactory;

public class BaseTest {

    public WebDriver driver;
    protected ConfigReader config;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        config = new ConfigReader();
        DriverFactory.initializeDriver(config);
        driver = DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /**
     * Clears the DemoBlaze cart via localStorage so each test starts from a clean state.
     * Must be called after driver.get(baseUrl) so the page's origin is set.
     */
    protected void clearCart() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
    }
}
