package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

public class CheckOutPom extends BasePage {

    // ============================================
    // LOCATORS - Checkout Modal Form Fields
    // ============================================

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "country")
    private WebElement countryField;

    @FindBy(id = "city")
    private WebElement cityField;

    @FindBy(id = "card")
    private WebElement creditCardField;

    @FindBy(id = "month")
    private WebElement monthField;

    @FindBy(id = "year")
    private WebElement yearField;

    // ============================================
    // LOCATORS - Modal Information
    // ============================================

    @FindBy(id = "totalm")
    private WebElement totalInModal;

    @FindBy(id = "orderModal")
    private WebElement checkoutModal;

    // ============================================
    // LOCATORS - Modal Actions
    // ============================================

    @FindBy(css = "button[onclick='purchaseOrder()']")
    private WebElement purchaseButton;

    @FindBy(css = "#orderModal button.btn-secondary")
    private WebElement closeButton;

    // ============================================
    // LOCATORS - Success Modal
    // ============================================

    @FindBy(css = ".sweet-alert h2")
    private WebElement successMessageTitle;

    @FindBy(css = ".sweet-alert .lead")
    private WebElement successMessageDetails;

    @FindBy(css = ".sweet-alert button.confirm")
    private WebElement okButton;

    public CheckOutPom(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // FORM FILLING METHODS
    // ============================================

    @Step("Enter name: {name}")
    public CheckOutPom enterName(String name) {
        sendKeys(nameField, name);
        return this;
    }

    @Step("Enter country: {country}")
    public CheckOutPom enterCountry(String country) {
        sendKeys(countryField, country);
        return this;
    }

    @Step("Enter city: {city}")
    public CheckOutPom enterCity(String city) {
        sendKeys(cityField, city);
        return this;
    }

    @Step("Enter credit card: ****{cardNumber}")
    public CheckOutPom enterCreditCard(String cardNumber) {
        sendKeys(creditCardField, cardNumber);
        return this;
    }

    @Step("Enter month: {month}")
    public CheckOutPom enterMonth(String month) {
        sendKeys(monthField, month);
        return this;
    }

    @Step("Enter year: {year}")
    public CheckOutPom enterYear(String year) {
        sendKeys(yearField, year);
        return this;
    }

    @Step("Fill checkout form for {name}")
    public CheckOutPom fillCheckoutForm(String name, String country, String city,
                                        String card, String month, String year) {
        enterName(name);
        enterCountry(country);
        enterCity(city);
        enterCreditCard(card);
        enterMonth(month);
        enterYear(year);
        return this;
    }

    // ============================================
    // MODAL INFORMATION METHODS
    // ============================================

    @Step("Get total from checkout modal")
    public String getTotalInModal() {
        return getText(totalInModal);
    }

    @Step("Get total as number")
    public int getTotalAsInt() {
        return Integer.parseInt(getTotalInModal().replace("Total:", "").trim());
    }

    @Step("Verify checkout modal is displayed")
    public boolean isCheckoutModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(nameField));
            return isDisplayed(nameField);
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================
    // PURCHASE ACTIONS
    // ============================================

    @Step("Click Purchase button")
    public CheckOutPom clickPurchase() {
        click(purchaseButton);
        wait.until(ExpectedConditions.visibilityOf(successMessageTitle));
        return this;
    }

    @Step("Close checkout modal")
    public CheckOutPom closeCheckoutModal() {
        click(closeButton);
        return this;
    }

    // ============================================
    // SUCCESS MODAL METHODS
    // ============================================

    @Step("Get success message")
    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(successMessageTitle));
        return getText(successMessageTitle);
    }

    @Step("Get success message details")
    public String getSuccessMessageDetails() {
        return getText(successMessageDetails);
    }

    @Step("Verify success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessageTitle));
            return isDisplayed(successMessageTitle);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Extract order ID from success message")
    public String getOrderId() {
        for (String line : getSuccessMessageDetails().split("\n")) {
            if (line.startsWith("Id:")) {
                return line.replace("Id:", "").trim();
            }
        }
        return "";
    }

    @Step("Extract amount from success message")
    public String getAmountFromSuccessMessage() {
        for (String line : getSuccessMessageDetails().split("\n")) {
            if (line.startsWith("Amount:")) {
                return line.replace("Amount:", "").trim();
            }
        }
        return "";
    }

    @Step("Click OK on success modal")
    public CheckOutPom clickOkOnSuccessModal() {
        wait.until(ExpectedConditions.elementToBeClickable(okButton));
        click(okButton);
        wait.until(ExpectedConditions.invisibilityOf(successMessageTitle));
        return this;
    }

    // ============================================
    // COMPLETE FLOW METHOD
    // ============================================

    @Step("Complete checkout flow for {name}")
    public String completeCheckout(String name, String country, String city,
                                   String card, String month, String year) {
        fillCheckoutForm(name, country, city, card, month, year);
        clickPurchase();
        String successMsg = getSuccessMessage();
        clickOkOnSuccessModal();
        return successMsg;
    }
}
