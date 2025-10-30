package tests;

import assertion.LoginPageAsserts;
import base.TestBase;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.TokenHandler;

import static utils.ConfigReader.*;

public class LoginTests extends TestBase {

    @Test(groups = {"Smoke", "UI", "Credentials"}
    )
    @Description("Verify successful login with valid credentials")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateToLogin(getBaseUrl());
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        LoginPageAsserts loginPageAsserts = new LoginPageAsserts();
        loginPageAsserts.validateLogin(loginPage);
        loginPageAsserts.validateLoginToken(loginPage);

        TokenHandler.saveAuthToken(loginPage);
    }

    @Test(groups = {"Negative"})
    @Description("Failing test on purpose")
    public void failingTest(){
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateToLogin(getBaseUrl());
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword()+"123");

        LoginPageAsserts loginPageAsserts = new LoginPageAsserts();
        loginPageAsserts.validateLogin(loginPage);
        loginPageAsserts.validateLoginToken(loginPage);

        TokenHandler.saveAuthToken(loginPage);
    }
}