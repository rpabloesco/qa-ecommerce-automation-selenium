package com.raulescobar.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.raulescobar.core.BasePage;

import io.qameta.allure.Step;

public class HomePom extends BasePage {

    //NavBar elements
    @FindBy(xpath = ".//a[contains(text(),'Home ')]")
    private WebElement homeLink;

    @FindBy(xpath = ".//a[contains(text(),'Contact')]")
    private WebElement contactLink;

    @FindBy(xpath = ".//a[contains(text(),'About us')]")
    private WebElement aboutUsLink;

    @FindBy(xpath = ".//a[contains(text(),'Cart')]")
    private WebElement cartLink;

    @FindBy(id = "login2")
    private WebElement loginLink;

    @FindBy(id = "signin2")
    private WebElement signUpLink;

    //Carousel elements
    @FindBy(id = "carouselExampleIndicators")
    private WebElement carrosuel;

    @FindBy(className = "carousel-control-next")
    private WebElement nextButton;

    @FindBy(className = "carousel-control-prev")
    private WebElement prevButton;

    //Categories elements
    @FindBy(xpath = ".//a[contains(text(),'Phones')]")
    private WebElement phonesLink;

    @FindBy(xpath = ".//a[contains(text(),'Laptops')]")
    private WebElement laptopsLink;

    @FindBy(xpath = ".//a[contains(text(),'Monitors')]")
    private WebElement monitorsLink;

    //Products elements
   @FindBy(className = "card")  
    private List<WebElement> productCards;

    @FindBy(className = "hrefch")  
    private List<WebElement> productLinks;

    @FindBy(css = "div.card-block h5")  
    private List<WebElement> productPrices;

    @FindBy(css = "div.card-block h4 a")  
    private List<WebElement> productTitles;

    //Footer elements
    @FindBy(id = "prev2")
    private WebElement footerPrevButton;

    @FindBy(id = "next2")
    private WebElement footerNextButton;

    /*
    * Constructor
    * @param driver WebDriver instance 
    */
    public HomePom(WebDriver driver) {
        super(driver);
    }

    // Simple Carousel methods
    @Step("Click Next on Carousel") 
    public HomePom clickNextOnCarousel(){
        click(nextButton);
       try {
         Thread.sleep(500);
       } catch (Exception e) {
        // TODO: handle exception
        Thread.currentThread().interrupt();
       }
        return this;
    }

    @Step("Click Previous on Carousel")
    public HomePom clickPreviousOnCarousel(){
        click(prevButton);
        try {
         Thread.sleep(500);
       } catch (Exception e) {
        // TODO: handle exception
        Thread.currentThread().interrupt();
       }
        return this;
    }

    // Simple Navigation methods
    @Step("Navigate to Phone Category")
    public HomePom clickPhonesCategory(){
        click(phonesLink);
        waitForPageLoad();
        return this;
    }

    @Step("Navigate to Laptops Category")
    public HomePom clickLaptopsCategory(){
        click(laptopsLink);
        waitForPageLoad();
        return this;
    }

    @Step("Navigate to Monitors Category")
    public HomePom clickMonitorsCategory(){
        click(monitorsLink);
        waitForPageLoad();
        return this;
    }

    /**
     * Generic method to click on a category based on its name
     * @param categoryName
     * @return
     */
    @Step("Click on Category: {categoryName}")
    public HomePom clickCategory(String categoryName){
        switch(categoryName.toLowerCase()){
            case "phones":
                return clickPhonesCategory();
            case "laptops":
                return clickLaptopsCategory();
            case "monitors":
                return clickMonitorsCategory();
            default:
                throw new IllegalArgumentException("Invalid category name: " + categoryName);
        }
    }

    @Step("Click on Product: {productName}")
    public void clickProductByName(String productName){
        WebElement product = driver.findElement(By.xpath(".//h4/a[contains(text(),'"+productName+"')]"));
        scrollToElement(product);
        click(product);
        waitForPageLoad();
    }

    @Step("Get product count")
    public int getProductCount() {  
        return productCards.size();
    }

     @Step("Verify product is displayed: {productName}")
    public boolean isProductDisplayed(String productName) { 
        try {
            WebElement product = driver.findElement(
                By.xpath(".//h4[@class='card-title']/a[contains(text(), '" + productName + "')]")
            );
            return isDisplayed(product);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get page title")
    public String getPageTitle() { 
        return driver.getTitle();
    }

     @Step("Verify home page is loaded")
    public boolean isHomePageLoaded() {  // ← CAMBIO 22: NUEVO método importante
        try {
            return isDisplayed(carrosuel) && productCards.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
}
