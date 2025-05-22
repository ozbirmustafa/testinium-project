package com.beymen.utils;

import com.beymen.base.BasePage;
import com.beymen.pages.CartPage;
import com.beymen.pages.ProductDetailPage;
import com.beymen.pages.ResultsPage;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.beymen.base.BasePage.waitForSeconds;

public class FlowHelper {

    private static final Logger logger = LoggerHelper.getLogger(FlowHelper.class);
    private static final int MAX_ATTEMPTS = 10;

    /**
     * Bu metot, ürün detay sayfasında rastgele bir ürün seçer ve sepete ekler.
     * Sepet sayfasında ürün miktarını artırmayı dener.
     * Eğer üründen 1 stok kalmışsa artırma işlemi başarısız olduğu için ürünü sepetten kaldırır.
     * Ardından, sonuç sayfasına geri döner ve yeni bir ürün seçer.
     * Bu işlem belirtilen maksimum deneme sayısına kadar devam eder.
     *
     * @param driver            WebDriver nesnesi
     * @param resultsPage       Sonuç sayfası nesnesi
     * @param productDetailPage Ürün detay sayfası nesnesi
     * @param cartPage          Sepet sayfası nesnesi
     */
    public static void tryToAddProductWithIncreasedQuantity(
            WebDriver driver,
            ResultsPage resultsPage,
            ProductDetailPage productDetailPage,
            CartPage cartPage
    ) {

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                cartPage.increaseQuantity(2);
                logger.info("Quantity successfully increased on attempt #{}", attempt);
                break;
            } catch (Exception e) {
                logger.warn("Attempt #{} failed: {}", attempt, e.getMessage());
                cartPage.removeProductFromCart();

                driver.navigate().back();
                waitForSeconds(2);
                driver.navigate().back();

                resultsPage.selectRandomProduct();
                productDetailPage.selectRandomActiveSize();
                productDetailPage.addToCart();
                productDetailPage.clickCartButton();
                cartPage.waitForCartPageToLoad();
            }
        }
    }

}