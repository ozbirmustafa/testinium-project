package com.beymen.base;

import com.beymen.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = ConfigReader.getProperty("browser");
            setDriver(browser);
        }
        return driver.get();
    }

    public static void setDriver(String browserType) {
        switch (browserType.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-gpu");
                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--disable-blink-features=AutomationControlled");
                edgeOptions.addArguments("--disable-gpu");
                driver.set(new EdgeDriver(edgeOptions));
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
        driver.get().manage().window().maximize();
    }

    public static void closeDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

}
