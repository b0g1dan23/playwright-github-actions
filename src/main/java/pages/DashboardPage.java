package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

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
        Allure.step("Opening Dashboard page");
        safeNavigate(pageURL);
    }

    public List<Locator> getCardsFromGrid(){
        if(cardGrid == null)
            return null;
        return cardGrid.locator("a.card").all();
    }
}
