package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

/**
 * Page Object for DemoBlaze Product Detail Page
 * Represents the individual product page with add to cart functionality
 */
public class ProductDetailPom extends BasePage {

    // ============================================
    // LOCATORS - Product Information
    // ============================================
    
    @FindBy(css = "h2.name")
    private WebElement productName;

    @FindBy(css = "h3.price-container")
    private WebElement productPrice;

    @FindBy(id = "more-information")
    private WebElement productDescription;

    @FindBy(css = "div.item.active img")
    private WebElement productImage;

    // ============================================
    // LOCATORS - Actions
    // ============================================
    
    @FindBy(css = "a.btn.btn-success.btn-lg")
    private WebElement addToCartButton;

    @FindBy(linkText = "Home")
    private WebElement homeLink;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    /**
     * Constructor for ProductDetailPage
     * @param driver WebDriver instance
     */
    public ProductDetailPom(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // PRODUCT INFORMATION METHODS
    // ============================================
    
    /**
     * Get product name
     * @return Product name as string
     */
    @Step("Get product name")
    public String getProductName() {
        return getText(productName);
    }

    /**
     * Get product price
     * @return Product price as string (includes $ symbol)
     */
    @Step("Get product price")
    public String getProductPrice() {
        String priceText = getText(productPrice);
        // Remove any extra text, keep only price with $
        return priceText.split("\\*")[0].trim();
    }

    /**
     * Get product price as integer (without $ symbol)
     * @return Product price as integer
     */
    @Step("Get product price as number")
    public int getProductPriceAsInt() {
        String priceText = getProductPrice();
        // Remove $ and any spaces, convert to int
        return Integer.parseInt(priceText.replace("$", "").trim());
    }

    /**
     * Get product description
     * @return Product description as string
     */
    @Step("Get product description")
    public String getProductDescription() {
        return getText(productDescription);
    }

    /**
     * Verify product detail page has loaded
     * @return true if product name and add to cart button are visible
     */
    @Step("Verify product detail page is loaded")
    public boolean isProductDetailPageLoaded() {
        try {
            return isDisplayed(productName) && isDisplayed(addToCartButton);
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================
    // CART INTERACTION METHODS
    // ============================================
    
    /**
     * Click Add to Cart button
     * Note: This will trigger a JavaScript alert
     */
    @Step("Click Add to Cart button")
    public ProductDetailPom clickAddToCart() {
        click(addToCartButton);
        return this;
    }

    /**
     * Accept the alert that appears after adding to cart
     * @return Alert text before accepting
     */
    @Step("Accept 'Product added' alert")
    public String acceptAddToCartAlert() {
        // Wait for alert to appear
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String alertText = getAlertText();
        acceptAlert();
        
        return alertText;
    }

    /**
     * Complete flow: Add to cart and accept alert
     * @return Alert message that was displayed
     */
    @Step("Add product to cart (complete flow)")
    public String addToCartAndAcceptAlert() {
        clickAddToCart();
        return acceptAddToCartAlert();
    }

    /**
     * Verify the alert message is correct
     * @return true if alert contains "Product added"
     */
    @Step("Verify add to cart alert message")
    public boolean verifyAddToCartAlertMessage() {
        String alertText = getAlertText();
        boolean isCorrect = alertText.contains("Product added");
        acceptAlert();
        return isCorrect;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================
    
    /**
     * Navigate back to home page
     */
    @Step("Navigate back to home page")
    public void goToHomePage() {
        click(homeLink);
        waitForPageLoad();
    }
}