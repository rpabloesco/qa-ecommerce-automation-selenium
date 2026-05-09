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

/**
 * Test Suite for Shopping Cart functionality in DemoBlaze
 * Validates adding, viewing, and removing products from cart
 */
@Epic("Cart Module")
@Feature("Shopping Cart Functionality")
public class CartTest extends BaseTest {
    
    private HomePom homePage;
    private ProductDetailPom productDetailPage;
    private CartPom cartPage;
    private String baseUrl;
    
    /**
     * Setup executed before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void navigateToHome() {
        baseUrl = config.getEnv("baseUrl");
        driver.get(baseUrl);
        
        homePage = new HomePom(driver);
        productDetailPage = new ProductDetailPom(driver);
        cartPage = new CartPom(driver);
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ============================================
    // ADD TO CART TESTS
    // ============================================
    
    @Test(priority = 1, groups = {"smoke", "cart", "add"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can add a product to cart and alert is displayed")
    @Story("Add Single Product to Cart")
    public void testAddProductToCart() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        String expectedAlert = testData.get("expectedMessages").get("productAdded").asText();
        
        System.out.println("Adding product to cart: " + productName);
        
        // Navigate to product
        homePage.clickProduct(productName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify on product detail page
        Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
            "Product detail page should load");
        
        Allure.addAttachment("Product Detail Page", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Add to cart and get alert message
        productDetailPage.clickAddToCart();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String alertMessage = productDetailPage.acceptAddToCartAlert();
        
        // Verify alert message
        Assert.assertTrue(alertMessage.contains(expectedAlert),
            "Alert should contain '" + expectedAlert + "'. Actual: " + alertMessage);
        
        System.out.println("✅ Product added successfully. Alert: " + alertMessage);
    }
    
    @Test(priority = 2, groups = {"smoke", "cart", "add"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify product appears in cart after adding")
    @Story("Product Appears in Cart")
    public void testProductAppearsInCart() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        int expectedPrice = testData.get("singleProduct").get("price").asInt();
        
        // Add product to cart
        homePage.clickProduct(productName);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        productDetailPage.addToCartAndAcceptAlert();
        
        // Navigate to cart
        cartPage.goToCart();
        
        // Verify cart page loaded
        Assert.assertTrue(cartPage.isCartPageLoaded(),
            "Should navigate to cart page");
        
        Allure.addAttachment("Cart Page with Product", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Verify product is in cart
        Assert.assertTrue(cartPage.isProductInCart(productName),
            productName + " should be in cart");
        
        // Verify product count
        Assert.assertEquals(cartPage.getProductCount(), 1,
            "Cart should contain 1 product");
        
        // Verify product price
        int actualPrice = cartPage.getProductPrice(productName);
        Assert.assertEquals(actualPrice, expectedPrice,
            "Product price should be " + expectedPrice);
        
        System.out.println("✅ Product " + productName + " is in cart with correct price: $" + actualPrice);
    }
    
    @Test(priority = 3, groups = {"regression", "cart", "add"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify multiple products can be added to cart")
    @Story("Add Multiple Products to Cart")
    public void testAddMultipleProductsToCart() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        JsonNode products = testData.get("multipleProductsScenario");
        
        int productCount = products.size();
        
        // Add each product to cart
        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            String category = product.get("category").asText();
            
            System.out.println("Adding product: " + productName);
            
            // Go to home
            driver.get(baseUrl);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Navigate to category and product
            homePage.clickCategory(category);
            
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            homePage.clickProduct(productName);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Add to cart
            productDetailPage.addToCartAndAcceptAlert();
        }
        
        // Navigate to cart
        cartPage.goToCart();
        
        Allure.addAttachment("Cart with Multiple Products", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Verify all products are in cart
        Assert.assertEquals(cartPage.getProductCount(), productCount,
            "Cart should contain " + productCount + " products");
        
        // Verify each product
        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            Assert.assertTrue(cartPage.isProductInCart(productName),
                productName + " should be in cart");
        }
        
        System.out.println("✅ Successfully added " + productCount + " products to cart");
    }
    
    // ============================================
    // CART CALCULATION TESTS
    // ============================================
    
    @Test(priority = 4, groups = {"smoke", "cart", "calculation"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cart total is calculated correctly")
    @Story("Cart Total Calculation")
    public void testCartTotalCalculation() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        JsonNode products = testData.get("multipleProductsScenario");
        int expectedTotal = testData.get("expectedTotalForMultiple").asInt();
        
        // Add products to cart
        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            String category = product.get("category").asText();
            
            driver.get(baseUrl);
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            homePage.clickCategory(category);
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            
            homePage.clickProduct(productName);
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            
            productDetailPage.addToCartAndAcceptAlert();
        }
        
        // Navigate to cart
        cartPage.goToCart();
        
        // Verify total calculation
        Assert.assertTrue(cartPage.verifyCartTotalCalculation(),
            "Cart total should equal sum of product prices");
        
        int actualTotal = cartPage.getCartTotal();
        Assert.assertEquals(actualTotal, expectedTotal,
            "Cart total should be " + expectedTotal);
        
        Allure.addAttachment("Cart Total Calculation", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        System.out.println("✅ Cart total calculation is correct: $" + actualTotal);
    }
    
    // ============================================
    // DELETE FROM CART TESTS
    // ============================================
    
    @Test(priority = 5, groups = {"smoke", "cart", "delete"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can delete a product from cart")
    @Story("Delete Product from Cart")
    public void testDeleteProductFromCart() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        
        // Add product to cart
        homePage.clickProduct(productName);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        productDetailPage.addToCartAndAcceptAlert();
        
        // Go to cart
        cartPage.goToCart();
        
        // Verify product is in cart
        Assert.assertTrue(cartPage.isProductInCart(productName),
            productName + " should be in cart before deletion");
        
        int initialCount = cartPage.getProductCount();
        
        Allure.addAttachment("Before Deleting Product", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Delete product
        cartPage.deleteProduct(productName);
        
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        Allure.addAttachment("After Deleting Product", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Verify product is removed
        Assert.assertFalse(cartPage.isProductInCart(productName),
            productName + " should be removed from cart");
        
        Assert.assertEquals(cartPage.getProductCount(), initialCount - 1,
            "Product count should decrease by 1");
        
        System.out.println("✅ Product deleted successfully from cart");
    }
    
    @Test(priority = 6, groups = {"regression", "cart", "delete"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can delete all products from cart")
    @Story("Empty Cart Completely")
    public void testDeleteAllProductsFromCart() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        JsonNode products = testData.get("products");
        
        // Add multiple products
        for (JsonNode product : products) {
            String productName = product.get("name").asText();
            String category = product.get("category").asText();
            
            driver.get(baseUrl);
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            homePage.clickCategory(category);
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            
            homePage.clickProduct(productName);
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            
            productDetailPage.addToCartAndAcceptAlert();
        }
        
        // Go to cart
        cartPage.goToCart();
        
        int initialCount = cartPage.getProductCount();
        System.out.println("Initial products in cart: " + initialCount);
        
        Assert.assertTrue(initialCount > 0, "Cart should have products before deletion");
        
        // Delete all products
        cartPage.deleteAllProducts();
        
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        Allure.addAttachment("Empty Cart", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Verify cart is empty
        Assert.assertTrue(cartPage.isCartEmpty(),
            "Cart should be empty after deleting all products");
        
        System.out.println("✅ All products deleted. Cart is now empty.");
    }
    
    // ============================================
    // CART PERSISTENCE TEST
    // ============================================
    
    @Test(priority = 7, groups = {"regression", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cart persists after navigating away and back")
    @Story("Cart Persistence")
    public void testCartPersistsAfterNavigation() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        
        // Add product to cart
        homePage.clickProduct(productName);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        productDetailPage.addToCartAndAcceptAlert();
        
        // Go to cart
        cartPage.goToCart();
        
        Assert.assertTrue(cartPage.isProductInCart(productName),
            "Product should be in cart initially");
        
        // Navigate away to home
        driver.get(baseUrl);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        // Navigate back to cart
        cartPage.goToCart();
        
        Allure.addAttachment("Cart After Navigation", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        // Verify product still in cart
        Assert.assertTrue(cartPage.isProductInCart(productName),
            "Product should still be in cart after navigation");
        
        System.out.println("✅ Cart persists after navigation");
    }
    
    // ============================================
    // PLACE ORDER BUTTON TEST
    // ============================================
    
    @Test(priority = 8, groups = {"smoke", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Place Order button is visible when cart has products")
    @Story("Place Order Button Visibility")
    public void testPlaceOrderButtonVisibility() throws Exception {
        // Get test data
        JsonNode testData = TestDataReader.readJsonFile("cart-testdata.json");
        String productName = testData.get("singleProduct").get("name").asText();
        
        // Add product to cart
        homePage.clickProduct(productName);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        productDetailPage.addToCartAndAcceptAlert();
        
        // Go to cart
        cartPage.goToCart();
        
        // Verify Place Order button is visible
        Assert.assertTrue(cartPage.isPlaceOrderButtonVisible(),
            "Place Order button should be visible when cart has products");
        
        Allure.addAttachment("Place Order Button Visible", 
            new ByteArrayInputStream(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES)));
        
        System.out.println("✅ Place Order button is visible");
    }
}