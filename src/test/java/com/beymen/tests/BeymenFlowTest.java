package com.beymen.tests;

import com.beymen.pages.ResultsPage;
import com.beymen.utils.*;
import com.beymen.base.DriverFactory;
import com.beymen.pages.CartPage;
import com.beymen.pages.HomePage;
import com.beymen.pages.ProductDetailPage;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class BeymenFlowTest {

    private static final Logger logger = LoggerHelper.getLogger(BeymenFlowTest.class);

    private WebDriver driver;
    private HomePage homePage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;
    private ResultsPage resultsPage;
    private ExcelReader excelReader;

    @Before
    public void setUp() {

        FileUtils.deleteFile(ConfigReader.getProperty("text.file.path"));
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("beymen.url"));

        homePage = new HomePage(driver);
        productDetailPage = new ProductDetailPage(driver);
        cartPage = new CartPage(driver);
        resultsPage = new ResultsPage(driver);

        excelReader = new ExcelReader(ConfigReader.getProperty("excel.file.path"));
        logger.info("Browser launched and Beymen.com opened.");

        homePage.rejectAllCookies();
        homePage.closeGenderModal();
    }

    @Test
    public void beymenShoppingFlowTest() {
        // 1. Ana sayfanın açıldığı kontrol edilir.
        homePage.verifyHomePageOpened();
        logger.info("Home page verified.");

        // 2. Arama kutucuğuna “şort” kelimesi girilir.
        String searchTerm1 = excelReader.getCellData("Sheet 1", 1, 1);
        homePage.assertNotEmpty(searchTerm1, "şort");
        homePage.enterSearchTerm(searchTerm1);
        logger.info("Entered '{}' from Excel.", searchTerm1);

        // 3. Arama kutucuğuna girilen “şort” kelimesi silinir.
        homePage.clearSearchBox();
        logger.info("Search box cleared.");

        // 4. Arama kutucuğuna “gömlek” kelimesi girilir.
        String searchTerm2 = excelReader.getCellData("Sheet 1", 1, 2);
        homePage.assertNotEmpty(searchTerm1, "gömlek");
        homePage.enterPostSearchTerm(searchTerm2);
        logger.info("Entered '{}' from Excel.", searchTerm2);

        // 5. Klavye üzerinden “enter” tuşuna bastırılır
        homePage.pressEnter();

        // 6. Sonuca göre sergilenen ürünlerden rastgele bir ürün seçilir.
        resultsPage.assertProductsLoadedSuccessfully();
        resultsPage.selectRandomProduct();


        // 7. Seçilen ürünün ürün bilgisi ve tutar bilgisi txt dosyasına yazılır.
        String productBrand = productDetailPage.getProductBrand();
        String productName = productDetailPage.getProductInfo();
        String productPrice = productDetailPage.getProductPrice();

        String productDetails = "Product Brand: " + productBrand + "\n" +
                                "Product Name: "     + productName + "\n" +
                                "Product Price: "  + productPrice;

        FileUtils.writeToFile("product_details.txt", productDetails);
        logger.info("Product details written to product_details.txt.");

        productDetailPage.selectRandomActiveSize();

        // 8. Seçilen ürün sepete eklenir.
        productDetailPage.addToCart();
        logger.info("Product added to cart.");

        // 9. Sepete git butonuna tıklanır.
        productDetailPage.clickCartButton();

        // 10. Sepet sayfasının açıldığı kontrol edilir.
        cartPage.waitForCartPageToLoad();

        // 11. Sepetteki ürün bilgileri ile ürün sayfasındaki bilgiler karşılaştırılır.
        cartPage.assertBrandNames(productBrand);
        cartPage.assertProductName(productName);

        // 12. Sepetteki ürün fiyatı ile ürün sayfasındaki fiyat karşılaştırılır.
        cartPage.assertProductPrice(productPrice);

        // 13. Adet arttırılarak ürün adedinin 2 olduğu doğrulanır.
        FlowHelper.tryToAddProductWithIncreasedQuantity(driver, resultsPage, productDetailPage, cartPage);

        String afterQuantity = cartPage.getNumberOfQuantity();
        Assert.assertEquals("Product quantity should be 2, but was: " + afterQuantity, "2", afterQuantity);
        logger.info("Product quantity verified as 2.");

        // 14. Ürün sepetten silinince çıkan toast mesajı kontrol edilir.
        cartPage.removeProductFromCart();
        cartPage.assertToastMsgAfterRemoveItemFromCart();

        // 14. Sepetin boş olduğu kontrol edilir.
        Assert.assertTrue("Cart is not empty", cartPage.isCartEmpty());
        logger.info("Product removed from cart and cart is empty.");
    }

//    @After
//    public void tearDown() {
//        if (driver != null) {
//            DriverFactory.closeDriver();
//            logger.info("Browser closed.");
//        }
//        if (excelReader != null) {
//            excelReader.close();
//            logger.info("Excel reader closed.");
//        }
//    }

}