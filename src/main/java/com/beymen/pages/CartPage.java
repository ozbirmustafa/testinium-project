package com.beymen.pages;

import com.beymen.base.BasePage;
import com.beymen.utils.LoggerHelper;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CartPage extends BasePage {

    private static final Logger logger = LoggerHelper.getLogger(CartPage.class);

    private final By toastMessageLocator = By.id("notifyTitle");
    private final By totalDiscountPriceBy = By.xpath("//span[text()='Toplam İndirim Tutarı']/following-sibling::span");

    @FindBy(xpath = "//span[text()='Ödenecek Tutar']/following-sibling::span")
    private WebElement amaountToPay;

    @FindBy(xpath = "//span[text()='Toplam İndirim Tutarı']/following-sibling::span")
    private WebElement totalDiscountPrice;

    @FindBy(css = ".m-basket__productInfoCategory")
    private WebElement brandName;

    @FindBy(css = ".m-basket__productInfoName")
    private WebElement productName;

    @FindBy(id = "quantitySelect0-key-0")
    private WebElement quantitySelector;

    @FindBy(css = ".m-basket__remove")
    private WebElement removeButton;

    @FindBy(xpath = "//*[text()='Sepetinizde Ürün Bulunmamaktadır']")
    private WebElement emptyCartMessage;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void waitForCartPageToLoad() {
        waitForVisibility(amaountToPay);
        logger.info("Cart page loaded successfully.");
    }

    public String getAmountToPayPrice() {
        waitForVisibility(amaountToPay);
        return amaountToPay.getText();
    }

    public String getBrandName() {
        waitForVisibility(brandName);
        return brandName.getText();
    }

    public String getProductName() {
        waitForVisibility(productName);
        return productName.getText();
    }

    public void increaseQuantity(int quantity) {
        Select quantitySelect = new Select(waitForVisibility(quantitySelector));
        try {
            quantitySelect.selectByValue(String.valueOf(quantity));
            waitForInvisibility(toastMessageLocator);
        } catch (Exception e) {
            logger.error("Product has only 1 quantity {}", e.getMessage());
            throw e;
        }
    }

    public String getProductQuantity() {
        Select quantitySelect = new Select(waitForVisibility(quantitySelector));
        return quantitySelect.getFirstSelectedOption().getText();
    }

    public void removeProductFromCart() {
        waitUntilElementClickable(removeButton).click();
        logger.info("Product removed from cart.");
    }

    /**
     * Sepetteki ürünün markasının doğruluğunu kontrol eder.
     * Sepete eklenmeden önceki ve sepetteki marka isimlerinde büyük/küçük harf farklılıkları olduğu için,
     * karşılaştırma işlemi her iki marka ismi de küçük harfe çevrilerek yapılır.
     * Türkçe karakterlerdeki uyumsuzlukları önlemek için Locale.ENGLISH kullanılır.
     *
     * @param productBrand Kontrol edilecek ürün markası (örneğin: "Nike").
     */
    public void assertBrandNames(String productBrand) {
        String cartBrand = getBrandName().toLowerCase(Locale.ENGLISH);
        assertEquals(productBrand.toLowerCase(Locale.ENGLISH), cartBrand, "Product brand does not match with the cart!");
    }

    public void assertProductName(String productName) {
        String cartProductName = getProductName();
        assertEquals(productName, cartProductName, "Product name does not match with the cart!");
    }

    /**
     * Ürünün sepet öncesi fiyatı ile sepet fiyatını karşılaştırır.
     * Eğer indirim varsa, indirimli fiyatı hesaplayarak ve kontrol eder.
     *
     * @param productPrice Ürünün sepet öncesi fiyatı (örneğin "1.234,56 TL").
     *
     */
    public void assertProductPrice(String productPrice) {
        double amountToPayDouble = parsePriceToDouble(getAmountToPayPrice());
        double productPriceDouble = parsePriceToDouble(productPrice);

        if (driver.findElements(totalDiscountPriceBy).isEmpty()) {
            assertEquals(productPriceDouble, amountToPayDouble, "Product price does not match with the cart!");
        } else {
            String discountText = driver.findElement(totalDiscountPriceBy).getText();

            double discountPriceDouble = parsePriceToDouble(discountText);
            double expectedAmountToPay = productPriceDouble - discountPriceDouble;

            assertEquals(expectedAmountToPay, amountToPayDouble, 0.01, "Calculated amount to pay does not match!");
            logger.info("Discounted price verified. Product: {}, Discount: {}, Expected Payment: {}",
                    productPriceDouble, discountPriceDouble, expectedAmountToPay);
        }

    }

    public void assertToastMsgAfterRemoveItemFromCart() {
        String toastText = waitForVisibility(toastMessageLocator).getText();
        assertEquals("Ürün Silindi", toastText, "Toast message after removing product is incorrect.");
        logger.info("Toast message verified after removing product: {}", toastText);
    }

    public boolean isCartEmpty() {
        try {
            return emptyCartMessage.isDisplayed();
        } catch (Exception e) {
            logger.warn("Cart is not empty or empty message not found: {}", e.getMessage());
            return false;
        }
    }

    public String getNumberOfQuantity() {
        return getProductQuantity().replaceAll(" adet", "");
    }

    /**
     * String formatındaki fiyatı double'a çevirir.
     *
     * @param priceWithCurrency Fiyatı içeren string (örneğin "1.234,56 TL").
     * @return Fiyatın double formatı.
     */
    private double parsePriceToDouble(String priceWithCurrency) {
        String cleaned = priceWithCurrency
                .replace("TL", "")
                .replace(".", "")
                .replace(",", ".")
                .replace("-", "")
                .trim();
        return Double.parseDouble(cleaned);
    }

}