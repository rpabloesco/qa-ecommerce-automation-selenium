package com.raulescobar.tests.smoke;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.raulescobar.pages.HomePom;
import com.raulescobar.pages.ProductDetailPom;
import com.raulescobar.pages.CartPom;
import com.raulescobar.tests.base.BaseTest;
import com.raulescobar.utils.TestDataReader;
import io.qameta.allure.*;
import java.io.ByteArrayInputStream;
import com.fasterxml.jackson.databind.JsonNode;

@Epic("Cart Module")
@Feature("Shopping Cart Functionality")
public class CartTest extends BaseTest {

    private HomePom homePage;
    private ProductDetailPom productDetailPage;
    private CartPom cartPage;
    private String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void navigateToHome() {
        baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);

        homePage = new HomePom(driver);
        productDetailPage = new ProductDetailPom(driver);
        cartPage = new CartPom(driver);

        homePage.waitForHomePageToLoad();
    }

    // ============================================
    // ADD TO CART TESTS
    // ============================================

    @Test(priority = 1, groups = {"smoke", "cart", "add"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can add a product to cart and alert is displayed")
    @Story("Add Single Product to Cart")
    public void testAddProductToCart() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        String expectedAlert = testData.get("expectedMessages").get("productAdded").asText();

        System.out.println("Adding product to cart: " + productName);

        // clickProduct() waits for product detail page to load
        homePage.clickProduct(productName);

        Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
            "Product detail page should load");

        Allure.addAttachment("Product Detail Page",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        // acceptAddToCartAlert() waits for alertIsPresent via BasePage.getAlertText()
        productDetailPage.clickAddToCart();
        String alertMessage = productDetailPage.acceptAddToCartAlert();

        Assert.assertTrue(alertMessage.contains(expectedAlert),
            "Alert should contain '" + expectedAlert + "'. Actual: " + alertMessage);

        System.out.println("Product added successfully. Alert: " + alertMessage);
    }

    @Test(priority = 2, groups = {"smoke", "cart", "add"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify product appears in cart after adding")
    @Story("Product Appears in Cart")
    public void testProductAppearsInCart() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        int expectedPrice = testData.get("singleProduct").get("price").asInt();

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();

        // goToCart() waits for cart content to be visible
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isCartPageLoaded(), "Should navigate to cart page");

        Allure.addAttachment("Cart Page with Product",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assert.assertTrue(cartPage.isProductInCart(productName),
            productName + " should be in cart");
        Assert.assertEquals(cartPage.getProductCount(), 1,
            "Cart should contain 1 product");
        Assert.assertEquals(cartPage.getProductPrice(productName), expectedPrice,
            "Product price should be " + expectedPrice);

        System.out.println("Product " + productName + " is in cart with correct price: $" + expectedPrice);
    }

    @Test(priority = 3, groups = {"regression", "cart", "add"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify multiple products can be added to cart")
    @Story("Add Multiple Products to Cart")
    public void testAddMultipleProductsToCart() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        JsonNode products = testData.get("multipleProductsScenario");
        int productCount = products.size();

        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            String category = product.get("category").asText();

            System.out.println("Adding product: " + productName);

            driver.get(baseUrl);
            homePage.waitForHomePageToLoad();

            homePage.clickCategory(category);
            homePage.clickProduct(productName);
            productDetailPage.addToCartAndAcceptAlert();
        }

        cartPage.goToCart();

        Allure.addAttachment("Cart with Multiple Products",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assert.assertEquals(cartPage.getProductCount(), productCount,
            "Cart should contain " + productCount + " products");

        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            Assert.assertTrue(cartPage.isProductInCart(productName),
                productName + " should be in cart");
        }

        System.out.println("Successfully added " + productCount + " products to cart");
    }

    // ============================================
    // CART CALCULATION TESTS
    // ============================================

    @Test(priority = 4, groups = {"smoke", "cart", "calculation"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cart total is calculated correctly")
    @Story("Cart Total Calculation")
    public void testCartTotalCalculation() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        JsonNode products = testData.get("multipleProductsScenario");
        int expectedTotal = testData.get("expectedTotalForMultiple").asInt();

        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            String category = product.get("category").asText();

            driver.get(baseUrl);
            homePage.waitForHomePageToLoad();
            homePage.clickCategory(category);
            homePage.clickProduct(productName);
            productDetailPage.addToCartAndAcceptAlert();
        }

        cartPage.goToCart();

        Assert.assertTrue(cartPage.verifyCartTotalCalculation(),
            "Cart total should equal sum of product prices");
        Assert.assertEquals(cartPage.getCartTotal(), expectedTotal,
            "Cart total should be " + expectedTotal);

        Allure.addAttachment("Cart Total Calculation",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        System.out.println("Cart total calculation is correct: $" + expectedTotal);
    }

    // ============================================
    // DELETE FROM CART TESTS
    // ============================================

    @Test(priority = 5, groups = {"smoke", "cart", "delete"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can delete a product from cart")
    @Story("Delete Product from Cart")
    public void testDeleteProductFromCart() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isProductInCart(productName),
            productName + " should be in cart before deletion");

        int initialCount = cartPage.getProductCount();

        Allure.addAttachment("Before Deleting Product",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        // deleteProduct() waits for stalenessOf the delete button (DOM update confirmed)
        cartPage.deleteProduct(productName);

        Allure.addAttachment("After Deleting Product",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assert.assertFalse(cartPage.isProductInCart(productName),
            productName + " should be removed from cart");
        Assert.assertEquals(cartPage.getProductCount(), initialCount - 1,
            "Product count should decrease by 1");

        System.out.println("Product deleted successfully from cart");
    }

    @Test(priority = 6, groups = {"regression", "cart", "delete"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can delete all products from cart")
    @Story("Empty Cart Completely")
    public void testDeleteAllProductsFromCart() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        JsonNode products = testData.get("products");

        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            String category = product.get("category").asText();

            driver.get(baseUrl);
            homePage.waitForHomePageToLoad();
            homePage.clickCategory(category);
            homePage.clickProduct(productName);
            productDetailPage.addToCartAndAcceptAlert();
        }

        cartPage.goToCart();

        int initialCount = cartPage.getProductCount();
        System.out.println("Initial products in cart: " + initialCount);
        Assert.assertTrue(initialCount > 0, "Cart should have products before deletion");

        // deleteAllProducts() loops calling deleteProductByIndex() which waits for staleness each time
        cartPage.deleteAllProducts();

        Allure.addAttachment("Empty Cart",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assert.assertTrue(cartPage.isCartEmpty(),
            "Cart should be empty after deleting all products");

        System.out.println("All products deleted. Cart is now empty.");
    }

    // ============================================
    // CART PERSISTENCE TEST
    // ============================================

    @Test(priority = 7, groups = {"regression", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cart persists after navigating away and back")
    @Story("Cart Persistence")
    public void testCartPersistsAfterNavigation() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isProductInCart(productName),
            "Product should be in cart initially");

        driver.get(baseUrl);
        homePage.waitForHomePageToLoad();

        cartPage.goToCart();

        Allure.addAttachment("Cart After Navigation",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assert.assertTrue(cartPage.isProductInCart(productName),
            "Product should still be in cart after navigation");

        System.out.println("Cart persists after navigation");
    }

    // ============================================
    // PLACE ORDER BUTTON TEST
    // ============================================

    @Test(priority = 8, groups = {"smoke", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Place Order button is visible when cart has products")
    @Story("Place Order Button Visibility")
    public void testPlaceOrderButtonVisibility() throws Exception {
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();

        homePage.clickProduct(productName);
        productDetailPage.addToCartAndAcceptAlert();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isPlaceOrderButtonVisible(),
            "Place Order button should be visible when cart has products");

        Allure.addAttachment("Place Order Button Visible",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        System.out.println("Place Order button is visible");
    }
}
