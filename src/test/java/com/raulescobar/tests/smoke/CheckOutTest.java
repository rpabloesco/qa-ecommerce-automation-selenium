package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.CartPage;
import com.raulescobar.pages.CheckoutPage;
import com.raulescobar.pages.HomePage;
import com.raulescobar.pages.ProductDetailPage;
import com.raulescobar.tests.base.BaseTest;
import com.raulescobar.utils.TestDataReader;
import io.qameta.allure.*;
import java.io.ByteArrayInputStream;
import com.fasterxml.jackson.databind.JsonNode;

@Epic("Checkout Module")
@Feature("Order Placement and Payment")
public class CheckOutTest extends BaseTest {

    private HomePage homePage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void navigateToHome() {
        baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        clearCart();

        homePage = new HomePage(driver);
        productDetailPage = new ProductDetailPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        homePage.waitForHomePageToLoad();
    }

    @Test(priority = 1, groups = {"smoke", "checkout"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can complete checkout with valid data")
    @Story("Complete Checkout Flow")
    public void testCompleteCheckoutWithValidData() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("checkout-testdata.json");
        JsonNode customer = testData.get("validCustomer");
        JsonNode product = testData.get("productForCheckout");
        String expectedSuccess = testData.get("expectedMessages").get("successTitle").asText();
        String productName = product.get("name").asText();

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();

        Allure.addAttachment("Cart Before Checkout",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        cartPage.clickPlaceOrder();

        Assert.assertTrue(checkoutPage.isCheckoutModalDisplayed(),
            "Checkout modal should be displayed");

        Allure.addAttachment("Checkout Modal",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        checkoutPage.fillCheckoutForm(
            customer.get("name").asText(),
            customer.get("country").asText(),
            customer.get("city").asText(),
            customer.get("creditCard").asText(),
            customer.get("month").asText(),
            customer.get("year").asText()
        );

        Allure.addAttachment("Checkout Form Filled",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        checkoutPage.clickPurchase();

        Assert.assertTrue(checkoutPage.isSuccessMessageDisplayed(),
            "Success message should be displayed");

        String successMessage = checkoutPage.getSuccessMessage();
        Assert.assertTrue(successMessage.contains(expectedSuccess),
            "Success message should contain '" + expectedSuccess + "'. Actual: " + successMessage);

        Allure.addAttachment("Purchase Success",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        System.out.println("Order completed! ID: " + checkoutPage.getOrderId()
            + " | Amount: " + checkoutPage.getAmountFromSuccessMessage()
            + " | Customer: " + customer.get("name").asText());

        checkoutPage.clickOkOnSuccessModal();
    }

    @Test(priority = 2, groups = {"smoke", "checkout"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify checkout modal displays total correctly")
    @Story("Checkout Total Display")
    public void testCheckoutModalDisplaysTotalCorrectly() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("checkout-testdata.json");
        JsonNode product = testData.get("productForCheckout");
        String productName = product.get("name").asText();
        int expectedPrice = product.get("price").asInt();

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();

        int cartTotal = cartPage.getCartTotal();
        cartPage.clickPlaceOrder();

        int modalTotal = checkoutPage.getTotalAsInt();

        Allure.addAttachment("Checkout Modal Total",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assert.assertEquals(modalTotal, cartTotal, "Modal total should match cart total");
        Assert.assertEquals(modalTotal, expectedPrice, "Modal total should match expected price");

        System.out.println("Checkout modal displays correct total: $" + modalTotal);
    }

    @Test(priority = 3, groups = {"regression", "checkout"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify success message is displayed after purchase")
    @Story("Purchase Success Message")
    public void testSuccessMessageAfterPurchase() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("checkout-testdata.json");
        JsonNode customer = testData.get("validCustomer");
        JsonNode product = testData.get("productForCheckout");

        homePage.clickProduct(product.get("name").asText());
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();
        cartPage.clickPlaceOrder();

        String successMsg = checkoutPage.completeCheckout(
            customer.get("name").asText(),
            customer.get("country").asText(),
            customer.get("city").asText(),
            customer.get("creditCard").asText(),
            customer.get("month").asText(),
            customer.get("year").asText()
        );

        Assert.assertTrue(successMsg.contains("Thank you"),
            "Success message should contain 'Thank you'");

        System.out.println("Success message verified: " + successMsg);
    }

    @Test(priority = 4, groups = {"regression", "checkout"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify checkout with multiple products calculates total correctly")
    @Story("Multi-Product Checkout")
    public void testCheckoutWithMultipleProducts() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("checkout-testdata.json");
        JsonNode customer = testData.get("validCustomer");
        JsonNode products = testData.get("multipleProductsForCheckout");
        int expectedTotal = testData.get("expectedTotalMultiple").asInt();

        for (JsonNode product : products) {
            driver.get(baseUrl);
            homePage.waitForHomePageToLoad();
            homePage.clickProduct(product.get("name").asText());
            productDetailPage.addToCartAndAcceptAlert();
        }

        cartPage.goToCart();

        Assert.assertEquals(cartPage.getCartTotal(), expectedTotal,
            "Cart total should be " + expectedTotal);

        Allure.addAttachment("Cart with Multiple Products",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        cartPage.clickPlaceOrder();

        Assert.assertEquals(checkoutPage.getTotalAsInt(), expectedTotal,
            "Modal total should be " + expectedTotal);

        Allure.addAttachment("Checkout Modal Multiple Products",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        checkoutPage.fillCheckoutForm(
            customer.get("name").asText(),
            customer.get("country").asText(),
            customer.get("city").asText(),
            customer.get("creditCard").asText(),
            customer.get("month").asText(),
            customer.get("year").asText()
        );

        checkoutPage.clickPurchase();

        Assert.assertTrue(checkoutPage.isSuccessMessageDisplayed(),
            "Success message should appear");

        System.out.println("Multi-product checkout successful. Total: "
            + checkoutPage.getAmountFromSuccessMessage());

        checkoutPage.clickOkOnSuccessModal();
    }

    @Test(priority = 5, groups = {"regression", "checkout"}, dataProvider = "customerData")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify checkout works with different customer data (data-driven)")
    @Story("Data-Driven Checkout")
    public void testCheckoutWithDifferentCustomers(String name, String country, String city,
                                                    String card, String month, String year) throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("checkout-testdata.json");
        String productName = testData.get("productForCheckout").get("name").asText();

        System.out.println("Testing checkout for: " + name + " from " + city + ", " + country);

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();
        cartPage.clickPlaceOrder();

        checkoutPage.fillCheckoutForm(name, country, city, card, month, year);
        checkoutPage.clickPurchase();

        Assert.assertTrue(checkoutPage.isSuccessMessageDisplayed(),
            "Checkout should succeed for " + name);

        System.out.println("Order placed for " + name + ". Order ID: " + checkoutPage.getOrderId());

        checkoutPage.clickOkOnSuccessModal();
    }

    @DataProvider(name = "customerData")
    public Object[][] customerDataProvider() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("checkout-testdata.json");
        JsonNode customers = testData.get("customers");

        Object[][] data = new Object[customers.size()][6];
        int i = 0;
        for (JsonNode customer : customers) {
            data[i][0] = customer.get("name").asText();
            data[i][1] = customer.get("country").asText();
            data[i][2] = customer.get("city").asText();
            data[i][3] = customer.get("creditCard").asText();
            data[i][4] = customer.get("month").asText();
            data[i][5] = customer.get("year").asText();
            i++;
        }
        return data;
    }
}
