package com.raulescobar.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;
import java.util.List;

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

    @FindBy(id = "orderModal")
    private WebElement checkoutModal;

    public CartPom(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    @Step("Navigate to cart page")
    public CartPom goToCart() {
        click(cartLink);
        waitForPageLoad();
        // Wait for cart total element — present even when cart is empty
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        return this;
    }

    // ============================================
    // CART CONTENT METHODS
    // ============================================

    @Step("Get product count in cart")
    public int getProductCount() {
        try {
            return cartProductRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return getProductCount() == 0;
    }

    @Step("Get all product names in cart")
    public List<String> getProductNames() {
        return productTitles.stream()
            .map(this::getText)
            .toList();
    }

    @Step("Verify product is in cart: {productName}")
    public boolean isProductInCart(String productName) {
        return getProductNames().stream()
            .anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    @Step("Get cart total")
    public int getCartTotal() {
        try {
            return Integer.parseInt(getText(totalPrice).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Get price of product: {productName}")
    public int getProductPrice(String productName) {
        for (int i = 0; i < productTitles.size(); i++) {
            if (getText(productTitles.get(i)).equalsIgnoreCase(productName)) {
                return Integer.parseInt(getText(productPrices.get(i)).trim());
            }
        }
        return 0;
    }

    // ============================================
    // PRODUCT MANAGEMENT METHODS
    // ============================================

    @Step("Delete product from cart: {productName}")
    public CartPom deleteProduct(String productName) {
        for (int i = 0; i < productTitles.size(); i++) {
            if (getText(productTitles.get(i)).equalsIgnoreCase(productName)) {
                WebElement button = deleteButtons.get(i);
                click(button);
                wait.until(ExpectedConditions.stalenessOf(button));
                break;
            }
        }
        return this;
    }

    @Step("Delete product at index: {index}")
    public CartPom deleteProductByIndex(int index) {
        if (index >= 0 && index < deleteButtons.size()) {
            WebElement button = deleteButtons.get(index);
            click(button);
            wait.until(ExpectedConditions.stalenessOf(button));
        }
        return this;
    }

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

    @Step("Click Place Order button")
    public void clickPlaceOrder() {
        click(placeOrderButton);
        wait.until(ExpectedConditions.visibilityOf(checkoutModal));
    }

    @Step("Verify Place Order button is visible")
    public boolean isPlaceOrderButtonVisible() {
        return isDisplayed(placeOrderButton);
    }

    // ============================================
    // VALIDATION METHODS
    // ============================================

    @Step("Verify cart total calculation is correct")
    public boolean verifyCartTotalCalculation() {
        int expectedTotal = productPrices.stream()
            .mapToInt(el -> Integer.parseInt(getText(el).trim()))
            .sum();
        int actualTotal = getCartTotal();
        System.out.println("Expected total: " + expectedTotal + " | Actual: " + actualTotal);
        return expectedTotal == actualTotal;
    }

    @Step("Verify cart page is loaded")
    public boolean isCartPageLoaded() {
        return getCurrentUrl().contains("cart.html");
    }
}
