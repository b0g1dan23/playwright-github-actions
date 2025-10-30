package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties config;

    static {
        config = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("credentials.properties")) {
            config.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load config.properties: " + e.getMessage());
        }
    }
    
    public static String getValidEmail() {
        return config.getProperty("validUser1");
    }
    
    public static String getValidPassword() {
        return config.getProperty("validPass1");
    }

    public static String getBaseUrl() { return config.getProperty("baseUrl"); }

    //Agnostic approach, but needs to have well-defined property names, harder to read in code
    public static String getProperty(String key) {
        return config.getProperty(key);
    }
}