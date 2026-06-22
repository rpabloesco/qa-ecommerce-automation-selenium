package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.HomePom;
import com.raulescobar.tests.base.BaseTest;
import io.qameta.allure.*;
import com.raulescobar.utils.TestDataReader;
import java.io.ByteArrayInputStream;
import java.util.List;

@Epic("Navigation Module")
@Feature("Category and Product Navigation")
public class NavigationTest extends BaseTest {

    private HomePom homePage;
    private String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void navigateToHome() {
        baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        homePage = new HomePom(driver);
        homePage.waitForHomePageToLoad();
    }

    // ============================================
    // HOME PAGE VALIDATION TESTS
    // ============================================

    @Test(priority = 1, groups = {"smoke", "validation"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify home page loads successfully")
    @Story("Home Page Load")
    public void testHomePageLoads() {
        Assert.assertTrue(homePage.isHomePomLoaded(),
            "Home page should load with carousel and products");

        Allure.addAttachment("Home Page Loaded",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 2, groups = {"smoke", "validation"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify page title contains expected text")
    @Story("Page Title Validation")
    public void testPageTitleIsCorrect() {
        String pageTitle = homePage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("STORE") || pageTitle.contains("PRODUCT STORE"),
            "Page title should contain 'STORE'. Actual: " + pageTitle);
        System.out.println("Page title: " + pageTitle);
    }

    @Test(priority = 3, groups = {"smoke", "validation"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify products are displayed on home page")
    @Story("Products Display Validation")
    public void testHomePageDisplaysProducts() {
        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0,
            "Home page should display at least one product. Found: " + productCount);
        System.out.println("Products displayed: " + productCount);

        Allure.addAttachment("Products on Home Page",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 3, groups = {"smoke", "validation"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify minimum number of products on home page (using external config)")
    @Story("Products Display Validation - Data Driven")
    public void testHomePageDisplaysMinimumProducts() {
        int minimumExpected = TestDataReader.getHomePageMinProducts();
        int actualCount = homePage.getProductCount();
        Assert.assertTrue(actualCount >= minimumExpected,
            "Home page should display at least " + minimumExpected + " products. Found: " + actualCount);
        System.out.println("Home page products: Expected min " + minimumExpected + ", Found " + actualCount);

        Allure.addAttachment("Products on Home Page",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // ============================================
    // PHONES CATEGORY TESTS
    // ============================================

    @Test(priority = 10, groups = {"smoke", "categories", "phones"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Phones category button is clickable")
    @Story("Phones Category - Click Action")
    public void testPhonesCategoryIsClickable() {
        // clickPhonesCategory() waits for products to reload before returning
        homePage.clickPhonesCategory();
        System.out.println("Phones category clicked successfully");

        Allure.addAttachment("After Clicking Phones Category",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 11, groups = {"smoke", "categories", "phones"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify products are displayed after clicking Phones category")
    @Story("Phones Category - Products Display")
    public void testPhonesCategoryDisplaysProducts() {
        homePage.clickPhonesCategory();

        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0,
            "Phones category should display at least one product. Found: " + productCount);
        System.out.println("Phones displayed: " + productCount);

        Allure.addAttachment("Phones Category Products",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 12, groups = {"regression", "categories", "phones"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify specific phone product is displayed in Phones category")
    @Story("Phones Category - Product Visibility")
    public void testSpecificPhoneIsDisplayed() {
        homePage.clickPhonesCategory();

        String productName = "Samsung galaxy s6";
        Assert.assertTrue(homePage.isProductDisplayed(productName),
            productName + " should be displayed in Phones category");
        System.out.println(productName + " is visible");

        Allure.addAttachment("Samsung Galaxy S6 Visible",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // ============================================
    // LAPTOPS CATEGORY TESTS
    // ============================================

    @Test(priority = 20, groups = {"smoke", "categories", "laptops"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Laptops category button is clickable")
    @Story("Laptops Category - Click Action")
    public void testLaptopsCategoryIsClickable() {
        homePage.clickLaptopsCategory();
        System.out.println("Laptops category clicked successfully");

        Allure.addAttachment("After Clicking Laptops Category",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 21, groups = {"smoke", "categories", "laptops"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify products are displayed after clicking Laptops category")
    @Story("Laptops Category - Products Display")
    public void testLaptopsCategoryDisplaysProducts() {
        homePage.clickLaptopsCategory();

        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0,
            "Laptops category should display at least one product. Found: " + productCount);
        System.out.println("Laptops displayed: " + productCount);

        Allure.addAttachment("Laptops Category Products",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 22, groups = {"regression", "categories", "laptops"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify specific laptop product is displayed in Laptops category")
    @Story("Laptops Category - Product Visibility")
    public void testSpecificLaptopIsDisplayed() {
        homePage.clickLaptopsCategory();

        String productName = "Sony vaio i5";
        Assert.assertTrue(homePage.isProductDisplayed(productName),
            productName + " should be displayed in Laptops category");
        System.out.println(productName + " is visible");

        Allure.addAttachment("Sony Vaio i5 Visible",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // ============================================
    // MONITORS CATEGORY TESTS
    // ============================================

    @Test(priority = 30, groups = {"smoke", "categories", "monitors"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Monitors category button is clickable")
    @Story("Monitors Category - Click Action")
    public void testMonitorsCategoryIsClickable() {
        homePage.clickMonitorsCategory();
        System.out.println("Monitors category clicked successfully");

        Allure.addAttachment("After Clicking Monitors Category",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 31, groups = {"smoke", "categories", "monitors"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify products are displayed after clicking Monitors category")
    @Story("Monitors Category - Products Display")
    public void testMonitorsCategoryDisplaysProducts() {
        homePage.clickMonitorsCategory();

        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0,
            "Monitors category should display at least one product. Found: " + productCount);
        System.out.println("Monitors displayed: " + productCount);

        Allure.addAttachment("Monitors Category Products",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 32, groups = {"regression", "categories", "monitors"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify specific monitor product is displayed in Monitors category")
    @Story("Monitors Category - Product Visibility")
    public void testSpecificMonitorIsDisplayed() {
        homePage.clickMonitorsCategory();

        String productName = "Apple monitor 24";
        Assert.assertTrue(homePage.isProductDisplayed(productName),
            productName + " should be displayed in Monitors category");
        System.out.println(productName + " is visible");

        Allure.addAttachment("Apple Monitor 24 Visible",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // ============================================
    // PRODUCT DETAIL NAVIGATION TESTS
    // ============================================

    @Test(priority = 40, groups = {"smoke", "products"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify clicking on a product navigates to product detail page")
    @Story("Product Detail Navigation")
    public void testProductClickNavigatesToDetail() {
        String productName = "Samsung galaxy s6";

        Assert.assertTrue(homePage.isProductDisplayed(productName),
            productName + " should be visible before clicking");

        // clickProduct() calls waitForPageLoad() before returning
        homePage.clickProduct(productName);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("prod.html"),
            "URL should contain 'prod.html'. Actual: " + currentUrl);
        System.out.println("Navigated to product detail page");

        Allure.addAttachment("Product Detail Page",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 41, groups = {"regression", "products"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify product detail page contains product information")
    @Story("Product Detail Content Validation")
    public void testProductDetailPageHasContent() {
        String productName = "Samsung galaxy s6";

        homePage.clickProduct(productName);

        String pageSource = driver.getPageSource();
        Assert.assertTrue(
            pageSource.contains(productName) || pageSource.contains("Product description"),
            "Product detail page should display product information");
        System.out.println("Product detail page has content");
    }

    // ============================================
    // CAROUSEL TESTS
    // ============================================

    @Test(priority = 50, groups = {"regression", "carousel"})
    @Severity(SeverityLevel.MINOR)
    @Description("Verify carousel next button is clickable")
    @Story("Carousel - Next Button")
    public void testCarouselNextButtonWorks() {
        // clickNextOnCarousel() waits for Bootstrap transition to complete before returning
        homePage.clickNextOnCarousel();
        System.out.println("Carousel next button clicked");

        Allure.addAttachment("After Carousel Next",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 51, groups = {"regression", "carousel"})
    @Severity(SeverityLevel.MINOR)
    @Description("Verify carousel previous button is clickable")
    @Story("Carousel - Previous Button")
    public void testCarouselPreviousButtonWorks() {
        homePage.clickNextOnCarousel();
        homePage.clickPreviousOnCarousel();
        System.out.println("Carousel previous button clicked");

        Allure.addAttachment("After Carousel Previous",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // ============================================
    // DATA-DRIVEN TESTS
    // ============================================

    @Test(priority = 60, groups = {"regression", "categories"}, dataProvider = "categoryProvider")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify generic category navigation method works for all categories (data-driven)")
    @Story("Generic Category Method - Data Driven")
    public void testGenericCategoryMethodDataDriven(String category) {
        homePage.clickCategory(category);

        int minimumExpected = TestDataReader.getMinimumProductCount(category);
        int actualCount = homePage.getProductCount();

        Assert.assertTrue(actualCount >= minimumExpected,
            category + " should display at least " + minimumExpected + " products. Found: " + actualCount);
        System.out.println(category + ": Expected min " + minimumExpected + ", Found " + actualCount);

        Allure.addAttachment(category + " Category Validation",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(priority = 61, groups = {"regression", "categories"}, dataProvider = "categoryProvider")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify expected products exist in each category")
    @Story("Category Products Validation - Data Driven")
    public void testCategoryHasExpectedProducts(String category) {
        homePage.clickCategory(category);

        List<String> expectedProducts = TestDataReader.getExpectedProductsForCategory(category);
        System.out.println("Validating products in " + category + ": " + expectedProducts);

        boolean foundExpectedProduct = expectedProducts.stream()
            .anyMatch(productName -> homePage.isProductDisplayed(productName));

        Assert.assertTrue(foundExpectedProduct,
            "At least one expected product should be visible in " + category);

        Allure.addAttachment(category + " Expected Products Check",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // ============================================
    // DATA PROVIDERS
    // ============================================

    @DataProvider(name = "categoryProvider")
    public Object[][] categoryProvider() {
        return TestDataReader.getCategoriesForDataProvider();
    }

    @DataProvider(name = "phonesProductProvider")
    public Object[][] phonesProductProvider() {
        return TestDataReader.getProductsByCategoryForDataProvider("Phones");
    }

    @DataProvider(name = "laptopsProductProvider")
    public Object[][] laptopsProductProvider() {
        return TestDataReader.getProductsByCategoryForDataProvider("Laptops");
    }

    @DataProvider(name = "monitorsProductProvider")
    public Object[][] monitorsProductProvider() {
        return TestDataReader.getProductsByCategoryForDataProvider("Monitors");
    }
}
