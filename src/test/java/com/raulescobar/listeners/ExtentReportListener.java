package com.raulescobar.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.raulescobar.tests.base.BaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom ExtentReports TestNG Listener
 * Generates HTML reports with screenshots and detailed test information
 */
public class ExtentReportListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String reportPath;
    
    @Override
    public void onStart(ITestContext context) {
        // Create timestamp for unique report name
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        reportPath = "test-output/ExtentReport_" + timestamp + ".html";
        
        // Configure ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Report configuration
        sparkReporter.config().setDocumentTitle("E-Commerce Test Automation Report");
        sparkReporter.config().setReportName("Test Execution Results");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setTimelineEnabled(true);
        
        // Initialize ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System Information
        extent.setSystemInfo("Application", "DemoBlaze E-Commerce");
        extent.setSystemInfo("Test Environment", "QA");
        extent.setSystemInfo("Tester", "Pablo Escobar");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Test Suite", context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        // Create test in report
        ExtentTest extentTest = extent.createTest(
            result.getMethod().getMethodName(),
            result.getMethod().getDescription()
        );
        
        // Add categories/groups
        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            extentTest.assignCategory(group);
        }
        
        test.set(extentTest);
        
        // Log test start
        test.get().log(Status.INFO, 
            MarkupHelper.createLabel("Test Started: " + result.getMethod().getMethodName(), 
            ExtentColor.BLUE));
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, 
            MarkupHelper.createLabel("Test PASSED", ExtentColor.GREEN));
        
        test.get().pass("Test completed successfully");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, 
            MarkupHelper.createLabel("Test FAILED", ExtentColor.RED));
        
        // Log exception
        test.get().fail(result.getThrowable());
        
        // Capture screenshot
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testClass).driver;
            
            if (driver != null) {
                try {
                    // Take screenshot
                    String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
                    
                    // Attach to report
                    test.get().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                    test.get().fail("Screenshot captured at: " + screenshotPath);
                    
                } catch (IOException e) {
                    test.get().fail("Failed to capture screenshot: " + e.getMessage());
                }
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, 
            MarkupHelper.createLabel("Test SKIPPED", ExtentColor.ORANGE));
        
        if (result.getThrowable() != null) {
            test.get().skip(result.getThrowable());
        }
    }
    
    @Override
    public void onFinish(ITestContext context) {
        // Flush reports
        extent.flush();
        
        // Log report location
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 ExtentReport generated at: " + reportPath);
        System.out.println("=".repeat(80) + "\n");
    }
    
    /**
     * Capture screenshot and save to file
     */
    private String captureScreenshot(WebDriver driver, String testName) throws IOException {
        // Create screenshots directory
        String screenshotDir = "test-output/screenshots/";
        Files.createDirectories(Paths.get(screenshotDir));
        
        // Generate filename with timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = screenshotDir + fileName;
        
        // Capture and save screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(screenshot.toPath(), Paths.get(filePath));
        
        return filePath;
    }
}