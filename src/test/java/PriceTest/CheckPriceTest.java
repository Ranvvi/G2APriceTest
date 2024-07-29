package PriceTest;

import commons.elements.Selectors;
import commons.steps.SupportSteps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class CheckPriceTest {
    @Test
    public void CheckPrice() throws IOException, InterruptedException {

        //NOTE: Due to lack of proper cookies the test will fail on the cart
        //NOTE: The cookies are cleared due to workaround. G2A site have a protection from automated behavior or some
        // settings are cosing the driver to break if the cookies are not cleared.
        //If you want to Check the behavior comment out: driver.manage().deleteAllCookies();

        ExtentReports report = new ExtentReports("./target/report/ShoppingCartReport.html");
        ExtentTest test = report.startTest("CheckPriceTest");

        String productName = SupportSteps.getProductName();

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Selectors selector = new Selectors();

        //G2A Connection
        driver.get("https://www.g2a.com");
        if (driver.findElement(By.cssSelector(selector.searchField)).isDisplayed()) {
            test.log(LogStatus.PASS, test.addScreenCapture(
                    SupportSteps.getScreenShoot(driver)
            ), "connected to G2A");
        } else {
            test.log(LogStatus.FAIL, "not connected to G2A");
            driver.quit();
            report.endTest(test);
            report.flush();
        }

        driver.manage().deleteAllCookies();

        //Searching
        driver.findElement(By.cssSelector(selector.searchField))
                .sendKeys(productName);
        if (driver.findElement(By.cssSelector(selector.searchField)).getAttribute("value").equals(productName)) {
            test.log(LogStatus.PASS, test.addScreenCapture(SupportSteps.getScreenShoot(driver)), "search passed");
        } else {
            test.log(LogStatus.FAIL, "search failed");
            driver.quit();
            report.endTest(test);
            report.flush();
        }

        driver.manage().deleteAllCookies();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector.productContainer)));

        driver.findElement(By.linkText(productName)).click();
        if (driver.findElement(By.className("productCard")).isDisplayed()) {
            test.log(LogStatus.PASS, test.addScreenCapture(SupportSteps.getScreenShoot(driver)), "product found");
        } else {
            test.log(LogStatus.FAIL, "product not found");
            driver.quit();
            report.endTest(test);
            report.flush();
        }

        driver.manage().deleteAllCookies();

        //Open Game Site
        driver.findElement(By.className("productCard")).click();
        driver.manage().deleteAllCookies();
        if (driver.findElement(By.cssSelector(selector.upperPrice)).isDisplayed()) {
            test.log(LogStatus.PASS, test.addScreenCapture(
                            SupportSteps.getScreenShoot(driver))
                    , "Game Page Opened");
        } else {
            test.log(LogStatus.FAIL, "Game Page Not Opened");
            driver.quit();
            report.endTest(test);
            report.flush();
        }


        //ADD to cart
        driver.findElement(By.cssSelector(selector.upperPrice)
        ).click();
        String price_shop = driver.findElement(By.cssSelector(selector.priceOnGamePage))
                .getText();

        driver.findElement(By.cssSelector(selector.addToCart)).click();
        driver.findElement(By.cssSelector(selector.acceptAddingToCart)).click();
        driver.findElement(By.cssSelector(selector.goToCart));

        TimeUnit.SECONDS.sleep(10);

        if (driver.findElement(By.cssSelector(selector.pageNotFound)).isDisplayed()) {
            test.log(LogStatus.FAIL, test.addScreenCapture(SupportSteps.getScreenShoot(driver)), "Game not found in cart");
            driver.quit();
            report.endTest(test);
            report.flush();
        } else {
            test.log(LogStatus.PASS, "Game found in cart");
        }

        String price_cart = driver.findElement(By.cssSelector(selector.priceOnCart))
                .getText();

        //Check if the Prices are equal
        Assertions.assertEquals(price_cart, price_shop);

        report.endTest(test);
        report.flush();
        driver.quit();
    }
}