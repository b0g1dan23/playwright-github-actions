package assertion;

import com.microsoft.playwright.Locator;
import org.testng.asserts.SoftAssert;
import pages.TestCasesPage;

import java.util.List;

public class TestCasesAsserts {
    private final SoftAssert softAssert;

    public TestCasesAsserts() {
        this.softAssert = new SoftAssert();
    }

    public void assertFailedDeletion(Exception e){
        softAssert.fail(e.getMessage());
        softAssert.assertAll();
    }

    public void assertToastMessage(Locator toastLocator, String message){
        softAssert.assertNotNull(toastLocator, message);
    }

    public void assertAllTestCasesDeleted(TestCasesPage testCasesPage) {
        List<Locator> remainingCases = testCasesPage.getAllTestCases();
        softAssert.assertTrue(remainingCases.isEmpty(), "There are still test cases present after deletion.");
    }

    public void assertVisibleLocator(Locator locator){
        softAssert.assertNotNull(locator,"Locator should not be null");
        softAssert.assertTrue(locator.isVisible(),"Locator should be visible");
        softAssert.assertAll();
    }
}
