package utils;

import constants.TokenConstants;
import pages.LoginPage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenHandler {
    public static void saveAuthToken(LoginPage loginPage) {
        String authToken = loginPage.getAuthToken();
        String refreshToken = loginPage.getRefreshToken();
        Properties props = new Properties();

        try (InputStream input = TokenHandler.class.getClassLoader().getResourceAsStream("credentials.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Could not load existing properties: " + e.getMessage());
        }

        props.setProperty(TokenConstants.SANDBOX_TOKEN, authToken);
        props.setProperty(TokenConstants.SANDBOX_REFRESH_TOKEN,refreshToken);

        try (FileOutputStream output = new FileOutputStream("src/main/resources/credentials.properties")) {
            props.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSavedAuthToken(){ return ConfigReader.getProperty(TokenConstants.SANDBOX_TOKEN); }
    public static String getSavedRefreshToken(){ return ConfigReader.getProperty(TokenConstants.SANDBOX_REFRESH_TOKEN); }
}
