package assertion;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;

public class LoginPageAsserts {
    private final SoftAssert softAssert;

    public LoginPageAsserts() {
        this.softAssert = new SoftAssert();
    }

    public void validateLogin(LoginPage loginPage) {

        softAssert.assertTrue(loginPage.isDashboardVisible(), "Dashboard should be visible after login");
        softAssert.assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "URL should contain dashboard");
        softAssert.assertAll();
    }

    public void validateLoginToken(LoginPage loginPage){
        DecodedJWT token = loginPage.getDecodedAuthToken();
        softAssert.assertTrue(loginPage.isTokenNotExpired(token), "Token has expired or the 'exp' claim is missing");
        softAssert.assertTrue(loginPage.isEmailValid(token), "The 'email' claim is invalid or does not match the expected user");
        softAssert.assertTrue(loginPage.isActive(token), "The 'active' claim is either missing or not set to true");

        softAssert.assertAll();
    }
}
