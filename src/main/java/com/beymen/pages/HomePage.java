package com.beymen.pages;

import com.beymen.base.BasePage;
import com.beymen.utils.LoggerHelper;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;

public class HomePage extends BasePage {

    private static final Logger logger = LoggerHelper.getLogger(HomePage.class);

    @FindBy(css = "input[placeholder='Ürün, Marka Arayın']")
    private WebElement intialSearchBox;

    @FindBy(css = "input[id='o-searchSuggestion__input']")
    private WebElement postSearchBox;

    @FindBy(xpath = "//button[@class='o-modal__closeButton bwi-close']")
    private WebElement closeGenderModalButton;

    @FindBy(id = "onetrust-reject-all-handler")
    private WebElement rejectAllCookiesButton;

    @FindBy(css = ".o-header__search--close")
    private WebElement searchClearButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void rejectAllCookies() {
        try {
            waitForVisibility(rejectAllCookiesButton).click();
            logger.info("Cookies rejected.");
        } catch (TimeoutException | NoSuchElementException e) {
            logger.info("Cookie reject button not found or already handled.");
        }
    }

    public void closeGenderModal() {
        try {
            click(closeGenderModalButton);
            logger.info("Gender modal closed.");
        } catch (TimeoutException | NoSuchElementException e) {
            logger.info("Gender modal close button not found or already handled.");
        } catch (Exception e) {
            logger.error("Unexpected error while closing gender modal.", e);
            throw e;
        }
    }

    public void verifyHomePageOpened() {
        waitForPageToLoad();
        assertTrue(intialSearchBox.isEnabled(), "Initial search box should be enabled");
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains("Beymen.com"), "Title should contain 'Beymen.com'");
        logger.info("Home page opened successfully.");
    }

    /**
     * Excel’den okunan değerin null veya boş olmadığını doğrular.
     * @param value     Okunan metin
     * @param searchTerm Hata mesajlarında kullanılacak alan adı (ör. “şort”, “gömlek”)
     */
    public void assertNotEmpty(String value, String searchTerm) {
        assertNotNull(value, "Search term '" + searchTerm + "' cannot be null.");
        assertFalse(value.trim().isEmpty(), "Search term '" + searchTerm + "' cannot be empty.");
    }


    public void enterSearchTerm(String term) {
        waitForVisibility(intialSearchBox);
        sendKeys(intialSearchBox, term);
    }

    /**
     * Arama kutucuğuna (temizlendikten veya arama yapıldıktan sonra ortaya çıkan) metin girer.
     * Özellikle 1. indeksteki elemente (2. element) yazmak için kullanılır.
     * @param term Aranacak kelime.
     */
    public void enterPostSearchTerm(String term) {
        waitForVisibility(postSearchBox).sendKeys(term);
    }

    public void clearSearchBox() {
        waitUntilElementClickable(searchClearButton).click();
        logger.info("Search box cleared.");
    }

    public void pressEnter() {
        postSearchBox.sendKeys(Keys.ENTER);
        logger.info("Pressed ENTER key.");
    }
}
