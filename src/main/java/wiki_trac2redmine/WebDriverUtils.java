package wiki_trac2redmine;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * utils
 */
public class WebDriverUtils {

    public static boolean isElementExist(WebDriver aDriver, String aId) {
        List<WebElement> list = aDriver.findElements(By.id(aId));
        return list!=null && !list.isEmpty();
    }

    public static void waitForId(WebDriver aDriver, String aId) {
        for(int i=0; i<20; i++) {

            if(isElementExist(aDriver, aId)) {
                return;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Unable to sleep about 500 ms", e);
            }

        }
    }


}
