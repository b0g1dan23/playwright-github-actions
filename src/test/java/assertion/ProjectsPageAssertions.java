package assertion;

import com.microsoft.playwright.Locator;
import org.testng.asserts.SoftAssert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectsPageAssertions {
    private final SoftAssert softAssert;

    public ProjectsPageAssertions() {
        softAssert = new SoftAssert();
    }

    public void assertFailedPersonCreation(Locator locator){
        softAssert.assertNotNull(locator, "Expected error message upon failed person creation is missing.");
        softAssert.assertEquals(locator.textContent(), "Person name already exist", "Expected error message for duplicate person name was not displayed");
        softAssert.assertAll();
    }

    public void assertCorrectPersons(List<String> actualNames, List<String> expectedNames){
        Set<String> actualSet = new HashSet<>(actualNames);
        Set<String> expectedSet = new HashSet<>(expectedNames);

        softAssert.assertEquals(actualSet, expectedSet, "The actual list of person names does not match the expected list.");
        softAssert.assertAll();

    }
}
