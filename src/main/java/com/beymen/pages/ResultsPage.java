package com.beymen.pages;

import com.beymen.base.BasePage;
import com.beymen.utils.LoggerHelper;
import com.beymen.utils.Utils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ResultsPage extends BasePage {

    private static final Logger logger = LoggerHelper.getLogger(ResultsPage.class);

    private final By productList = By.cssSelector(".o-productList__item");

    public ResultsPage(WebDriver driver) {
        super(driver);
    }

    public void assertProductsLoadedSuccessfully() {
        waitForPageToLoad();
        List<WebElement> products = productItems();
        assertFalse(products.isEmpty(), "Product list should not be empty.");
        logger.info("Results page loaded successfully with {} products.", products.size());
    }

    public void selectRandomProduct() {
        Utils.selectRandomProduct(driver.findElements(productList));
    }

    private List<WebElement> productItems() {
        return driver.findElements(productList);
    }

}