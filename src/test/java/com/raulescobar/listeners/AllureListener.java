package com.raulescobar.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.raulescobar.tests.base.BaseTest;

import java.io.ByteArrayInputStream;

public class AllureListener implements ITestListener {

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Allure.step("Test Started: " + testName);
        saveTextLog("Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        saveTextLog("✅ Test PASSED: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        saveTextLog("❌ Test FAILED: " + testName);
        
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testClass).driver;
            
            if (driver != null) {
                saveScreenshotPNG(driver);
                Allure.addAttachment("Failure Screenshot", "image/png",
                    new ByteArrayInputStream(((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES)), "png");
            }
        }
        
        if (result.getThrowable() != null) {
            saveTextLog("Exception: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        saveTextLog("⏭️ Test SKIPPED: " + testName);
    }

    @Override
    public void onStart(ITestContext context) {
        saveTextLog("🚀 Starting Test Suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        String summary = String.format(
            "Test Suite Finished\n✅ Passed: %d\n❌ Failed: %d\n⏭️ Skipped: %d",
            passed, failed, skipped
        );
        saveTextLog(summary);
    }
}