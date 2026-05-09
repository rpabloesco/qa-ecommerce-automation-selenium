package com.raulescobar.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;
import java.util.List;

/**
 * Page Object for DemoBlaze Cart Page
 * Represents the shopping cart with product management and checkout functionality
 */
public class CartPom extends BasePage {

    // ============================================
    // LOCATORS - Navigation
    // ============================================
    
    @FindBy(id = "cartur")
    private WebElement cartLink;

    // ============================================
    // LOCATORS - Cart Content
    // ============================================
    
    @FindBy(id = "tbodyid")
    private WebElement cartTableBody;

    @FindBy(css = "#tbodyid tr")
    private List<WebElement> cartProductRows;

    @FindBy(css = "#tbodyid tr td:nth-child(2)")
    private List<WebElement> productTitles;

    @FindBy(css = "#tbodyid tr td:nth-child(3)")
    private List<WebElement> productPrices;

    @FindBy(css = "#tbodyid tr td a")
    private List<WebElement> deleteButtons;

    @FindBy(id = "totalp")
    private WebElement totalPrice;

    // ============================================
    // LOCATORS - Actions
    // ============================================
    
    @FindBy(css = "button.btn.btn-success")
    private WebElement placeOrderButton;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    /**
     * Constructor for CartPage
     * @param driver WebDriver instance
     */
    public CartPom(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================
    
    /**
     * Navigate to cart page by clicking cart link in navbar
     */
    @Step("Navigate to cart page")
    public CartPom goToCart() {
        click(cartLink);
        waitForPageLoad();
        
        // Wait for cart to load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return this;
    }

    // ============================================
    // CART CONTENT METHODS
    // ============================================
    
    /**
     * Get number of products in cart
     * @return Count of products
     */
    @Step("Get product count in cart")
    public int getProductCount() {
        try {
            return cartProductRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Check if cart is empty
     * @return true if no products in cart
     */
    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return getProductCount() == 0;
    }

    /**
     * Get list of product names in cart
     * @return List of product names
     */
    @Step("Get all product names in cart")
    public List<String> getProductNames() {
        return productTitles.stream()
            .map(this::getText)
            .toList();
    }

    /**
     * Check if a specific product is in cart
     * @param productName Name of product to check
     * @return true if product is in cart
     */
    @Step("Verify product is in cart: {productName}")
    public boolean isProductInCart(String productName) {
        return getProductNames().stream()
            .anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    /**
     * Get cart total price
     * @return Total price as integer
     */
    @Step("Get cart total")
    public int getCartTotal() {
        try {
            String totalText = getText(totalPrice);
            return Integer.parseInt(totalText.trim());
        } catch (Exception e) {
            System.err.println("Error getting cart total: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Get price of specific product in cart
     * @param productName Name of the product
     * @return Price as integer, or 0 if not found
     */
    @Step("Get price of product: {productName}")
    public int getProductPrice(String productName) {
        for (int i = 0; i < productTitles.size(); i++) {
            if (getText(productTitles.get(i)).equalsIgnoreCase(productName)) {
                String priceText = getText(productPrices.get(i));
                return Integer.parseInt(priceText.trim());
            }
        }
        return 0;
    }

    // ============================================
    // PRODUCT MANAGEMENT METHODS
    // ============================================
    
    /**
     * Delete a specific product from cart by name
     * @param productName Name of product to delete
     */
    @Step("Delete product from cart: {productName}")
    public CartPom deleteProduct(String productName) {
        for (int i = 0; i < productTitles.size(); i++) {
            if (getText(productTitles.get(i)).equalsIgnoreCase(productName)) {
                click(deleteButtons.get(i));
                
                // Wait for product to be removed
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                break;
            }
        }
        return this;
    }

    /**
     * Delete product by index (position in cart)
     * @param index 0-based index
     */
    @Step("Delete product at index: {index}")
    public CartPom deleteProductByIndex(int index) {
        if (index >= 0 && index < deleteButtons.size()) {
            click(deleteButtons.get(index));
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return this;
    }

    /**
     * Delete all products from cart
     */
    @Step("Delete all products from cart")
    public CartPom deleteAllProducts() {
        while (!isCartEmpty()) {
            deleteProductByIndex(0);
        }
        return this;
    }

    // ============================================
    // CHECKOUT METHODS
    // ============================================
    
    /**
     * Click Place Order button to proceed to checkout
     */
    @Step("Click Place Order button")
    public void clickPlaceOrder() {
        click(placeOrderButton);
        
        // Wait for checkout modal to appear
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verify Place Order button is visible
     * @return true if button is displayed
     */
    @Step("Verify Place Order button is visible")
    public boolean isPlaceOrderButtonVisible() {
        return isDisplayed(placeOrderButton);
    }

    // ============================================
    // VALIDATION METHODS
    // ============================================
    
    /**
     * Verify cart total equals sum of product prices
     * @return true if total is correct
     */
    @Step("Verify cart total calculation is correct")
    public boolean verifyCartTotalCalculation() {
        int expectedTotal = 0;
        
        for (WebElement priceElement : productPrices) {
            String priceText = getText(priceElement);
            expectedTotal += Integer.parseInt(priceText.trim());
        }
        
        int actualTotal = getCartTotal();
        
        System.out.println("Expected total: " + expectedTotal);
        System.out.println("Actual total: " + actualTotal);
        
        return expectedTotal == actualTotal;
    }

    /**
     * Verify cart page has loaded
     * @return true if on cart page
     */
    @Step("Verify cart page is loaded")
    public boolean isCartPageLoaded() {
        return getCurrentUrl().contains("cart.html");
    }
}