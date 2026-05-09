package com.raulescobar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;

/**
 * Page Object for DemoBlaze Checkout Modal
 * Represents the order placement form and success confirmation
 */
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

    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    /**
     * Constructor for CheckOutPom
     * @param driver WebDriver instance
     */
    public CheckOutPom(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // FORM FILLING METHODS
    // ============================================
    
    /**
     * Enter name in checkout form
     * @param name Customer name
     */
    @Step("Enter name: {name}")
    public CheckOutPom enterName(String name) {
        sendKeys(nameField, name);
        return this;
    }

    /**
     * Enter country in checkout form
     * @param country Customer country
     */
    @Step("Enter country: {country}")
    public CheckOutPom enterCountry(String country) {
        sendKeys(countryField, country);
        return this;
    }

    /**
     * Enter city in checkout form
     * @param city Customer city
     */
    @Step("Enter city: {city}")
    public CheckOutPom enterCity(String city) {
        sendKeys(cityField, city);
        return this;
    }

    /**
     * Enter credit card number
     * @param cardNumber Credit card number
     */
    @Step("Enter credit card: ****{cardNumber}")
    public CheckOutPom enterCreditCard(String cardNumber) {
        sendKeys(creditCardField, cardNumber);
        return this;
    }

    /**
     * Enter expiration month
     * @param month Month (e.g., "12")
     */
    @Step("Enter month: {month}")
    public CheckOutPom enterMonth(String month) {
        sendKeys(monthField, month);
        return this;
    }

    /**
     * Enter expiration year
     * @param year Year (e.g., "2026")
     */
    @Step("Enter year: {year}")
    public CheckOutPom enterYear(String year) {
        sendKeys(yearField, year);
        return this;
    }

    /**
     * Fill complete checkout form
     * @param name Customer name
     * @param country Customer country
     * @param city Customer city
     * @param card Credit card number
     * @param month Expiration month
     * @param year Expiration year
     */
    @Step("Fill checkout form for {name}")
    public CheckOutPom fillCheckoutForm(String name, String country, String city, String card, String month, String year) {
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
    
    /**
     * Get total amount displayed in checkout modal
     * @return Total as string (e.g., "Total: 360")
     */
    @Step("Get total from checkout modal")
    public String getTotalInModal() {
        return getText(totalInModal);
    }

    /**
     * Get total amount as integer
     * @return Total as integer
     */
    @Step("Get total as number")
    public int getTotalAsInt() {
        String totalText = getTotalInModal();
        // Extract number from "Total: 360"
        String numberOnly = totalText.replace("Total:", "").trim();
        return Integer.parseInt(numberOnly);
    }

    /**
     * Verify checkout modal is displayed
     * @return true if modal is visible
     */
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
    
    /**
     * Click Purchase button to complete order
     */
    @Step("Click Purchase button")
    public CheckOutPom clickPurchase() {
        click(purchaseButton);
        
        // Wait for success modal to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return this;
    }

    /**
     * Close checkout modal without purchasing
     */
    @Step("Close checkout modal")
    public CheckOutPom closeCheckoutModal() {
        click(closeButton);
        return this;
    }

    // ============================================
    // SUCCESS MODAL METHODS
    // ============================================
    
    /**
     * Get success message title after purchase
     * @return Success message (e.g., "Thank you for your purchase!")
     */
    @Step("Get success message")
    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(successMessageTitle));
        return getText(successMessageTitle);
    }

    /**
     * Get success message details (includes order info)
     * @return Full success message details
     */
    @Step("Get success message details")
    public String getSuccessMessageDetails() {
        return getText(successMessageDetails);
    }

    /**
     * Verify success message is displayed
     * @return true if success modal is visible
     */
    @Step("Verify success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessageTitle));
            return isDisplayed(successMessageTitle);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract order ID from success message
     * @return Order ID as string
     */
    @Step("Extract order ID from success message")
    public String getOrderId() {
        String details = getSuccessMessageDetails();
        // Example format: "Id: 1234567\nAmount: 360 USD\nCard Number: 4111..."
        String[] lines = details.split("\n");
        for (String line : lines) {
            if (line.startsWith("Id:")) {
                return line.replace("Id:", "").trim();
            }
        }
        return "";
    }

    /**
     * Extract amount from success message
     * @return Amount as string (e.g., "360 USD")
     */
    @Step("Extract amount from success message")
    public String getAmountFromSuccessMessage() {
        String details = getSuccessMessageDetails();
        String[] lines = details.split("\n");
        for (String line : lines) {
            if (line.startsWith("Amount:")) {
                return line.replace("Amount:", "").trim();
            }
        }
        return "";
    }

    /**
     * Click OK button on success modal
     * @return CheckOutPom instance
     */
    @Step("Click OK on success modal")
    public CheckOutPom clickOkOnSuccessModal() {
        wait.until(ExpectedConditions.elementToBeClickable(okButton));
        click(okButton);
        
        // Wait for modal to close
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    // ============================================
    // COMPLETE FLOW METHOD
    // ============================================
    
    /**
     * Complete entire checkout flow
     * @param name Customer name
     * @param country Customer country
     * @param city Customer city
     * @param card Credit card number
     * @param month Expiration month
     * @param year Expiration year
     * @return Success message
     */
    @Step("Complete checkout flow for {name}")
    public String completeCheckout(String name, String country, String city, String card, String month, String year) {
        fillCheckoutForm(name, country, city, card, month, year);
        clickPurchase();
        String successMsg = getSuccessMessage();
        clickOkOnSuccessModal();
        return successMsg;
    }
}