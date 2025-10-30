package utils;

import io.qameta.allure.Step;

public class Allure {
    @Step("{0}")
    public static void logStep(final String message){
        System.out.println(message);
    }

    @Step("{0}")
    public static void beforeSuiteLog(final String message){
        System.out.println(message);
    }
}
