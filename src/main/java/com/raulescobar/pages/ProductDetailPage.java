package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

public class ProductDetailPage extends BasePage {

    @FindBy(css = "h2.name")
    private WebElement productName;

    @FindBy(css = "h3.price-container")
    private WebElement productPrice;

    @FindBy(id = "more-information")
    private WebElement productDescription;

    @FindBy(css = "div.item.active img")
    private WebElement productImage;

    @FindBy(css = "a.btn.btn-success.btn-lg")
    private WebElement addToCartButton;

    @FindBy(linkText = "Home")
    private WebElement homeLink;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get product name")
    public String getProductName() {
        return getText(productName);
    }

    @Step("Get product price")
    public String getProductPrice() {
        return getText(productPrice).split("\\*")[0].trim();
    }

    @Step("Get product price as number")
    public int getProductPriceAsInt() {
        return Integer.parseInt(getProductPrice().replace("$", "").trim());
    }

    @Step("Get product description")
    public String getProductDescription() {
        return getText(productDescription);
    }

    @Step("Verify product detail page is loaded")
    public boolean isProductDetailPageLoaded() {
        try {
            return isDisplayed(productName) && isDisplayed(addToCartButton);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Click Add to Cart button")
    public ProductDetailPage clickAddToCart() {
        click(addToCartButton);
        return this;
    }

    @Step("Accept 'Product added' alert")
    public String acceptAddToCartAlert() {
        String alertText = getAlertText();
        acceptAlert();
        return alertText;
    }

    @Step("Add product to cart (complete flow)")
    public String addToCartAndAcceptAlert() {
        clickAddToCart();
        return acceptAddToCartAlert();
    }

    @Step("Verify add to cart alert message")
    public boolean verifyAddToCartAlertMessage() {
        String alertText = getAlertText();
        boolean isCorrect = alertText.contains("Product added");
        acceptAlert();
        return isCorrect;
    }

    @Step("Navigate back to home page")
    public void goToHomePage() {
        click(homeLink);
        waitForPageLoad();
    }
}
