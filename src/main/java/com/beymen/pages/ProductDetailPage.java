package com.beymen.pages;

import com.beymen.base.BasePage;
import com.beymen.utils.LoggerHelper;
import com.beymen.utils.Utils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.Random;

public class ProductDetailPage extends BasePage {

    private static final Logger logger = LoggerHelper.getLogger(ProductDetailPage.class);
    Random random = new Random();

    private final By activeSizeOptionsLocator = By.cssSelector(".m-variation__item:not(.-disabled)");

    @FindBy(css = ".o-productDetail__brandLink")
    private WebElement productBrand;

    @FindBy(css = ".o-productDetail__description")
    private WebElement productDescription;

    @FindBy(css = ".m-price__new")
    private WebElement productPrice;

    @FindBy(id = "addBasket")
    private WebElement addToCartButton;

    @FindBy(css = "a.o-header__userInfo--item.bwi-cart-o.-cart")
    private WebElement cartButton;


    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getProductBrand() {
        waitForVisibility(productBrand);
        return productBrand.getText();
    }

    public String getProductInfo() {
        waitForVisibility(productDescription);
        return productDescription.getText();
    }

    public String getProductPrice() {
        waitForVisibility(productPrice);
        return productPrice.getText();
    }

    /**
     * Ürünün aktif beden seçeneklerinin olup olmadığını kontrol eder.
     * @return Aktif beden seçeneği varsa true, yoksa false döner.
     */
    public boolean hasActiveSizeOptions() {
        List<WebElement> activeSizes = driver.findElements(activeSizeOptionsLocator);
        boolean hasActive = !activeSizes.isEmpty();
        if (!hasActive) {
            logger.warn("No active size options found for the product.");
        }
        return hasActive;
    }

    /**
     * Ürünün aktif beden seçeneklerinden rastgele birini seçer.
     * Eğer aktif beden seçeneği yoksa, önceki sayfaya geri döner.
     * Bu durumda, ürün listesinden tekrar rastgele bir ürün seçer.
     */
    public void selectRandomActiveSize() {
            if (hasActiveSizeOptions()) {
                List<WebElement> activeSizes = driver.findElements(activeSizeOptionsLocator);
                int randomIndex = random.nextInt(activeSizes.size());
                WebElement randomSize = activeSizes.get(randomIndex);
                randomSize.click();
                logger.info("Random size selected: {}", randomSize.getText());

            } else {
                while (hasActiveSizeOptions()){
                driver.navigate().back();
                logger.warn("No active size options available. Navigating back.");
                List<WebElement> productItems = driver.findElements(By.cssSelector(".o-productList__item"));
                Utils.selectRandomProduct(productItems);
            }
        }
    }

    public void addToCart() {
        waitUntilElementClickable(addToCartButton).click();
        waitForSeconds(5);
    }

    public void clickCartButton() {
        waitUntilElementClickable(cartButton).click();
        logger.info("Clicked on the Cart button.");
    }
}