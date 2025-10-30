package assertion;

import com.microsoft.playwright.Page;
import org.testng.asserts.SoftAssert;

public class DashboardPageAsserts {
    private final SoftAssert softAssert;

    public DashboardPageAsserts(Page page) {
        this.softAssert = new SoftAssert();
    }

    public void validateURL(String actualURL, String expectedURL){
        softAssert.assertTrue(actualURL.contains(expectedURL), "URL after click does not match href attribute");
        softAssert.assertAll();
    }
}
