package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

import java.util.List;

import static utils.Allure.logStep;
import static utils.ConfigReader.getBaseUrl;

public class DashboardPage extends LoggedInPage {
    private final String pageURL = getBaseUrl() + "dashboard";

    private final Locator cardGrid = page.locator("div.card-grid");

    public DashboardPage(Page page) {
        super(page);
    }

    @Override
    public void goTo() {
        safeNavigate(pageURL);
        Allure.step("Opening Dashboard page", Status.PASSED);
    }

    public List<Locator> getCardsFromGrid(){
        if(cardGrid == null)
            return null;
        return cardGrid.locator("a.card").all();
    }
}
