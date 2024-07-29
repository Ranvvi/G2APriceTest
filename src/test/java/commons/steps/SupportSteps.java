package commons.steps;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public final class SupportSteps {


    public static String getProductName() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String productName;
        while (true) {
            System.out.println("Find Product: ");
            productName = br.readLine();
            if (!productName.isEmpty()) {
                break;
            }
        }
    return productName;
    }

    public static String getScreenShoot(WebDriver driver) throws IOException {
        File Dest = new File("./target/report/" + System.currentTimeMillis()+ ".png");
        File screenShoot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShoot,Dest);
        return Dest.getAbsolutePath();

    }

}
