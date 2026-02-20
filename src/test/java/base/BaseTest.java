package base;

import org.openqa.selenium.WebDriver;
import com.raulescobar.config.ConfigReader;
import com.raulescobar.driver.DriverFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public abstract class BaseTest {
    protected WebDriver driver;
    protected ConfigReader config;

    @BeforeTest
    public void setUp() {
        config = new ConfigReader();
        DriverFactory.initializeDriver(config);
        driver = DriverFactory.getDriver();
    }

    @AfterTest
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
