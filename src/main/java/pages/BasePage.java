package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;

import java.nio.file.Paths;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        page.click(selector);
    }
    protected void fill(String selector, String text) {
        page.fill(selector, text);
    }

    protected void safeFill(String selector, String text) {
        try {
            page.waitForSelector(selector);
            page.fill(selector, text);
        } catch (PlaywrightException e) {
            takeScreenshot("fill_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to fill: " + selector, e);
        }
    }

    protected void safeLocatorClick(Locator locator) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
            locator.click();
        } catch (PlaywrightException e) {
            takeScreenshot("locator_click_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to click on locator", e);
        }
    }

    protected List<String> getTextListFromLocator(Locator locator) {
        return locator.all().stream().map(Locator::textContent).toList();
    }

    protected void waitForVisible(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeoutMs));
    }

    protected String getFirstVisibleText(Locator locator) {
        locator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
        return locator.first().textContent();
    }

    protected void safeScrollAndClick(Locator locator) {
        try {
            locator.scrollIntoViewIfNeeded();
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
            locator.click();
        } catch (PlaywrightException e) {
            takeScreenshot("scroll_click_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to scroll and click", e);
        }
    }

    protected List<String> safeGetAllTextFromLocator(Locator locator) {
        try {
            locator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
            return locator.allTextContents();
        } catch (PlaywrightException e) {
            takeScreenshot("locator_all_text_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to get all texts from locator", e);
        }
    }

    protected void safeClick(String selector) {
        try {
            page.waitForSelector(selector);
            page.click(selector);
        } catch (PlaywrightException e) {
            takeScreenshot("click_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to click: " + selector, e);
        }
    }

    protected void safeNavigate(String url) {
        try {
            page.navigate(url);
            page.waitForURL(actualUrl -> actualUrl.contains("sandbox"));
        } catch (PlaywrightException e) {
            takeScreenshot("click_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to navigate to: " + url, e);
        }
    }

    protected void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get("screenshots/" + name + ".png")));
    }

    public String getCurrentUrl() {
        return page.url();
    }
    public abstract void goTo();
}