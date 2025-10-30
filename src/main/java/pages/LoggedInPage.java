package pages;

import com.microsoft.playwright.Page;
import constants.TokenConstants;
import org.testng.Assert;
import utils.TokenHandler;

public abstract class LoggedInPage extends BasePage{
    protected final String refreshToken = TokenHandler.getSavedRefreshToken();
    protected final String authToken = TokenHandler.getSavedAuthToken();

    public LoggedInPage(Page page) {
        super(page);
        if(refreshToken == null || authToken == null){
            Assert.fail("Couldn't get the refresh and auth token, please login first, and make sure you have them in credentials.properties file");
        }
        page.context().addInitScript("window.localStorage.setItem('"+ TokenConstants.SANDBOX_TOKEN +"', '" + authToken + "');");
        page.context().addInitScript("window.localStorage.setItem('"+ TokenConstants.SANDBOX_REFRESH_TOKEN +"', '" + refreshToken + "');");
    }
}
