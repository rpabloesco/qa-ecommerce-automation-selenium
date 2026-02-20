package com.raulescobar.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.raulescobar.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void initializeDriver(ConfigReader config) {
        String browser = config.get("browser", "chrome").toLowerCase();
        WebDriver driver;

        switch(browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("El navegador '" + browser + "' no está soportado");
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getInt("timeoutSeconds", 10)));
        driver.manage().window().maximize();
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("Driver no inicializado. Llama initDriver() primero.");
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
