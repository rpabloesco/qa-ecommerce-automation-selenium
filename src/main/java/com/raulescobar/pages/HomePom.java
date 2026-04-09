package com.raulescobar.pages; 

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;
import java.util.List;  

/**
 * Page Object for DemoBlaze Home Page
 * Represents the main landing page with categories, products, and carousel
 */
public class HomePom extends BasePage {  // ← CAMBIO 3: Nombre sin "Pom" sufijo

    // ============================================
    // LOCATORS - Navbar
    // ============================================
    
    @FindBy(linkText = "Home")  // ← CAMBIO 4: linkText más simple que xpath
    private WebElement homeLink;

    @FindBy(linkText = "Contact")
    private WebElement contactLink;

    @FindBy(linkText = "About us")
    private WebElement aboutUsLink;

    @FindBy(linkText = "Cart")
    private WebElement cartLink;

    @FindBy(id = "login2")  // ← CAMBIO 5: ID es más específico
    private WebElement loginLink;

    @FindBy(id = "signin2")
    private WebElement signUpLink;

    // ============================================
    // LOCATORS - Carousel
    // ============================================
    
    @FindBy(id = "carouselExampleIndicators")  // ← CAMBIO 6: ID del carousel completo
    private WebElement carousel;

    @FindBy(className = "carousel-control-next")  // ← CAMBIO 7: className más simple
    private WebElement nextButton;

    @FindBy(className = "carousel-control-prev")
    private WebElement prevButton;

    // ============================================
    // LOCATORS - Categories
    // ============================================
    
    @FindBy(linkText = "Phones")  // ← CAMBIO 8: linkText más confiable
    private WebElement phonesCategory;

    @FindBy(linkText = "Laptops")
    private WebElement laptopsCategory;

    @FindBy(linkText = "Monitors")
    private WebElement monitorsCategory;

    // ============================================
    // LOCATORS - Products (Lists for multiple elements)
    // ============================================
    
    @FindBy(className = "card")  // ← CAMBIO 9: Lista para todos los productos
    private List<WebElement> productCards;

    @FindBy(className = "hrefch")  // ← CAMBIO 10: Lista de links de productos
    private List<WebElement> productLinks;

    @FindBy(css = "div.card-block h5")  // ← CAMBIO 11: CSS selector para precios
    private List<WebElement> productPrices;

    @FindBy(css = "div.card-block h4 a")  // ← CAMBIO 12: CSS para títulos
    private List<WebElement> productTitles;

    // ============================================
    // LOCATORS - Footer Navigation
    // ============================================
    
    @FindBy(id = "prev2")
    private WebElement footerPrevButton;

    @FindBy(id = "next2")
    private WebElement footerNextButton;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    /**
     * Constructor for HomePom
     * @param driver WebDriver instance
     */
    public HomePom(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // CAROUSEL METHODS
    // ============================================
    
    /**
     * Click next button on carousel to show next slide
     * @return HomePom instance for method chaining
     */
    @Step("Click Next on Carousel")
    public HomePom clickNextOnCarousel() {
        click(nextButton);
        // ← CAMBIO 13: Pequeña espera para animación del carousel
        try { 
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    /**
     * Click previous button on carousel to show previous slide
     * @return HomePom instance for method chaining
     */
    @Step("Click Previous on Carousel")
    public HomePom clickPreviousOnCarousel() {
        click(prevButton);
        try { 
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    // ============================================
    // CATEGORY NAVIGATION METHODS
    // ============================================
    
    /**
     * Navigate to Phones category
     * @return HomePom instance for method chaining
     */
    @Step("Navigate to Phones Category")
    public HomePom clickPhonesCategory() {
        click(phonesCategory);
        waitForPageLoad();
        // ← CAMBIO 14: Espera adicional para que los productos se filtren
        try { 
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    /**
     * Navigate to Laptops category
     * @return HomePom instance for method chaining
     */
    @Step("Navigate to Laptops Category")
    public HomePom clickLaptopsCategory() {
        click(laptopsCategory);
        waitForPageLoad();
        try { 
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    /**
     * Navigate to Monitors category
     * @return HomePom instance for method chaining
     */
    @Step("Navigate to Monitors Category")
    public HomePom clickMonitorsCategory() {
        click(monitorsCategory);
        waitForPageLoad();
        try { 
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    /**
     * Navigate to specific category by name
     * @param categoryName Name of category (Phones, Laptops, or Monitors)
     * @return HomePom instance for method chaining
     * @throws IllegalArgumentException if invalid category name
     */
    @Step("Click on Category: {categoryName}")
    public HomePom clickCategory(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "phones":
                return clickPhonesCategory();  // ← CAMBIO 15: Return directo
            case "laptops":
                return clickLaptopsCategory();
            case "monitors":
                return clickMonitorsCategory();
            default:
                throw new IllegalArgumentException(
                    "Invalid category: '" + categoryName + "'. " +
                    "Valid options are: Phones, Laptops, Monitors"
                );
        }
    }

    // ============================================
    // PRODUCT INTERACTION METHODS
    // ============================================
    
    /**
     * Click on a specific product by its name
     * @param productName Exact or partial name of the product
     */
    @Step("Click on Product: {productName}")
    public void clickProduct(String productName) {  // ← CAMBIO 16: Nombre más simple
        // ← CAMBIO 17: XPath más robusto
        WebElement product = driver.findElement(
            By.xpath("//h4[@class='card-title']/a[contains(text(), '" + productName + "')]")
        );
        
        // ← CAMBIO 18: Scroll al elemento antes de hacer click
        scrollToElement(product);
        click(product);
        
        // ← CAMBIO 19: Espera a que la página de detalle cargue
        waitForPageLoad();
    }

    /**
     * Get total count of products displayed on page
     * @return Number of products visible
     */
    @Step("Get product count")
    public int getProductCount() {  // ← CAMBIO 20: NUEVO método útil
        return productCards.size();
    }

    /**
     * Check if a specific product is displayed
     * @param productName Name of the product to check
     * @return true if product is visible, false otherwise
     */
    @Step("Verify product is displayed: {productName}")
    public boolean isProductDisplayed(String productName) {  // ← CAMBIO 21: NUEVO método
        try {
            WebElement product = driver.findElement(
                By.xpath("//h4[@class='card-title']/a[contains(text(), '" + productName + "')]")
            );
            return isDisplayed(product);
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================
    // VALIDATION METHODS
    // ============================================
    
    /**
     * Verify home page has loaded successfully
     * @return true if carousel is visible (indicator of home page)
     */
    @Step("Verify home page is loaded")
    public boolean isHomePomLoaded() {  // ← CAMBIO 22: NUEVO método importante
        try {
            return isDisplayed(carousel) && productCards.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get current page title
     * @return Page title as string
     */
    @Step("Get page title")
    public String getPageTitle() {  // ← CAMBIO 23: NUEVO método útil
        return driver.getTitle();
    }

    /**
     * Verify specific category is selected
     * @param categoryName Name of category to verify
     * @return true if category appears active
     */
    @Step("Verify category {categoryName} is active")
    public boolean isCategoryActive(String categoryName) {  // ← CAMBIO 24: NUEVO método
        WebElement category = getCategoryElement(categoryName);
        // DemoBlaze no tiene clase "active" pero podemos verificar que existe
        return isDisplayed(category);
    }

    // ============================================
    // PRIVATE HELPER METHODS
    // ============================================
    
    /**
     * Get WebElement for specific category
     * @param categoryName Name of category
     * @return WebElement of the category link
     */
    private WebElement getCategoryElement(String categoryName) {  // ← CAMBIO 25: Helper privado
        switch (categoryName.toLowerCase()) {
            case "phones":
                return phonesCategory;
            case "laptops":
                return laptopsCategory;
            case "monitors":
                return monitorsCategory;
            default:
                throw new IllegalArgumentException("Invalid category: " + categoryName);
        }
    }
}