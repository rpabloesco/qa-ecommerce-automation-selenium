package com.raulescobar.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.raulescobar.core.BasePage;
import io.qameta.allure.Step;
import java.util.List;

public class HomePage extends BasePage {

    @FindBy(linkText = "Home")
    private WebElement homeLink;

    @FindBy(linkText = "Contact")
    private WebElement contactLink;

    @FindBy(linkText = "About us")
    private WebElement aboutUsLink;

    @FindBy(linkText = "Cart")
    private WebElement cartLink;

    @FindBy(id = "login2")
    private WebElement loginLink;

    @FindBy(id = "signin2")
    private WebElement signUpLink;

    @FindBy(id = "carouselExampleIndicators")
    private WebElement carousel;

    @FindBy(className = "carousel-control-next")
    private WebElement nextButton;

    @FindBy(className = "carousel-control-prev")
    private WebElement prevButton;

    @FindBy(linkText = "Phones")
    private WebElement phonesCategory;

    @FindBy(linkText = "Laptops")
    private WebElement laptopsCategory;

    @FindBy(linkText = "Monitors")
    private WebElement monitorsCategory;

    @FindBy(className = "card")
    private List<WebElement> productCards;

    @FindBy(className = "hrefch")
    private List<WebElement> productLinks;

    @FindBy(css = "div.card-block h4 a")
    private List<WebElement> productTitles;

    @FindBy(id = "prev2")
    private WebElement footerPrevButton;

    @FindBy(id = "next2")
    private WebElement footerNextButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Wait for home page to be interactive")
    public void waitForHomePageToLoad() {
        wait.until(ExpectedConditions.visibilityOf(carousel));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("card")));
    }

    @Step("Click Next on Carousel")
    public HomePage clickNextOnCarousel() {
        click(nextButton);
        waitForCarouselTransition();
        return this;
    }

    @Step("Click Previous on Carousel")
    public HomePage clickPreviousOnCarousel() {
        click(prevButton);
        waitForCarouselTransition();
        return this;
    }

    @Step("Navigate to Phones Category")
    public HomePage clickPhonesCategory() {
        clickCategoryAndWait(phonesCategory);
        return this;
    }

    @Step("Navigate to Laptops Category")
    public HomePage clickLaptopsCategory() {
        clickCategoryAndWait(laptopsCategory);
        return this;
    }

    @Step("Navigate to Monitors Category")
    public HomePage clickMonitorsCategory() {
        clickCategoryAndWait(monitorsCategory);
        return this;
    }

    @Step("Click on Category: {categoryName}")
    public HomePage clickCategory(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "phones":   return clickPhonesCategory();
            case "laptops":  return clickLaptopsCategory();
            case "monitors": return clickMonitorsCategory();
            default:
                throw new IllegalArgumentException(
                    "Invalid category: '" + categoryName + "'. Valid options: Phones, Laptops, Monitors");
        }
    }

    @Step("Click on Product: {productName}")
    public void clickProduct(String productName) {
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//h4[@class='card-title']/a[contains(text(), '" + productName + "')]")));
        scrollToElement(product);
        product.click();
        waitForPageLoad();
    }

    @Step("Get product count")
    public int getProductCount() {
        return productCards.size();
    }

    @Step("Verify product is displayed: {productName}")
    public boolean isProductDisplayed(String productName) {
        try {
            WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[@class='card-title']/a[contains(text(), '" + productName + "')]")));
            return product.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify home page is loaded")
    public boolean isHomePageLoaded() {
        try {
            return isDisplayed(carousel) && productCards.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get page title")
    public String getPageTitle() {
        return driver.getTitle();
    }

    @Step("Verify category {categoryName} is active")
    public boolean isCategoryActive(String categoryName) {
        return isDisplayed(getCategoryElement(categoryName));
    }

    /**
     * Captures a reference to an existing card before clicking the category so we can
     * detect when the DOM replaces it (staleness = category products finished loading).
     */
    private void clickCategoryAndWait(WebElement category) {
        WebElement firstCardBeforeClick = null;
        try {
            firstCardBeforeClick = driver.findElement(By.className("card"));
        } catch (NoSuchElementException ignored) { }

        click(category);

        if (firstCardBeforeClick != null) {
            wait.until(ExpectedConditions.stalenessOf(firstCardBeforeClick));
        }

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("card")));
    }

    /**
     * Bootstrap carousel adds carousel-item-next / carousel-item-prev during the slide
     * transition and removes them when the animation finishes.
     */
    private void waitForCarouselTransition() {
        wait.until(d -> d.findElements(
            By.cssSelector(".carousel-item.carousel-item-next, .carousel-item.carousel-item-prev")).isEmpty());
    }

    private WebElement getCategoryElement(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "phones":   return phonesCategory;
            case "laptops":  return laptopsCategory;
            case "monitors": return monitorsCategory;
            default:
                throw new IllegalArgumentException("Invalid category: " + categoryName);
        }
    }
}
