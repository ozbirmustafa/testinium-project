package com.beymen.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Utils {

    private static final Logger logger = LoggerHelper.getLogger(Utils.class);

    /**
     * Bu metot, verilen ürün listesinden rastgele bir ürünü seçer ve tıklar.
     *
     * @param productItems Ürün öğeleri listesi
     */
    public static void selectRandomProduct(List<WebElement> productItems) {
        if (!productItems.isEmpty()) {
            int randomIndex = (int) (Math.random() * productItems.size());
            WebElement randomProduct = productItems.get(randomIndex);
            randomProduct.click();
            logger.info("Random product selected.");
        } else {
            logger.error("No products found to select.");
        }
    }

}