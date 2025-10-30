package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.*;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class TestBase {
    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;

    @BeforeSuite(alwaysRun = true)
    public static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterSuite(alwaysRun = true)
    public static void tearDownClass() {
        browser.close();
        playwright.close();
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(ITestResult result) {
        page = browser.newPage();
        Allure.step("Opening new page");
        page.context().tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));

        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            Allure.label("group", group);
        }
    }

    public byte[] takeScreenshot(String name) {
        try {
            Path path = Paths.get("screenshots/" + name + ".png");
            page.screenshot(new Page.ScreenshotOptions().setPath(path));

            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(org.testng.ITestResult result) {
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            String tracePath = "target/playwright-report/trace-" + System.currentTimeMillis() + ".zip";
            page.context().tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracePath)));
            Allure.addAttachment("Image attachment","image/png",new ByteArrayInputStream(takeScreenshot(result.getName())), ".png");
        } else {
            page.context().tracing().stop();
        }
        page.close();
    }
}
