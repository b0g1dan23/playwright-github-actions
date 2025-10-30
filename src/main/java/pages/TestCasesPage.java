package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static utils.Allure.logStep;
import static utils.ConfigReader.getBaseUrl;

public class TestCasesPage extends LoggedInPage{
    private final String pageName = "testcases";
    private final String pageURL = getBaseUrl() + pageName;
    private final Locator portraitGrid = page.locator("div.portrait-grid");
    private final Locator deleteBtn = page.locator("button.btn.btn-danger");
    private final Locator createNewBtn = page.locator("a.btn.btn-primary[href='/new-testcase']");
    private final Locator noContent_locator = page.locator("div.no-content");
    private final Locator title_input = page.getByPlaceholder("Title");
    private final Locator description_input = page.getByPlaceholder("Description");
    private final Locator expectedResult_input = page.getByPlaceholder("Expected Result");
    private final Locator addNewStep_btn = page.locator("div.full-width-btn");
    private final Locator submit_btn = page.locator("div.submit-button").locator("button.btn.btn-primary.float-right");

    public TestCasesPage(Page page) {
        super(page);
    }

    @Override
    public void goTo() {
        Allure.step("Opening Test cases page");
        safeNavigate(pageURL);
    }

    public List<Locator> getAllTestCases() {
        Allure.step("Getting all test cases");

        try {
            noContent_locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(2000));
            return new ArrayList<>();
        } catch (PlaywrightException e) {
            waitForVisible(portraitGrid, 1000);
            return portraitGrid.locator("a.preview-card").all();
        }
    }

    public Locator verifyIndividualPage(Page page) throws Exception {
        String currentURL = page.url();
        String editURL = getBaseUrl() + "edit-testcase/";

        if (currentURL.contains(editURL)) {
            if (deleteBtn == null) throw new Exception("Delete button could not be located");
            return deleteBtn;
        }

        return null;
    }

    public String getCaseIDFromURL(){
        String currentURL = page.url();
        String editURL = getBaseUrl() + "edit-testcase/";
        if(currentURL.contains(editURL)) {
            return currentURL.split(editURL)[1];
        }
        return null;
    }

    public Locator getRemoveTestCaseButtonPopup(){return page.locator("div.confirmation-dialog--buttons--confirm");}

    public void fillCreationForm(String title, String description, String expectedResult, List<Number> list_steps){
        Allure.step("Filling out creation form");

        safeFill("input[name='title']", title);
        safeFill("textarea[name='description']", description);
        safeFill("input[name='expected_result']", expectedResult);

        for (int i = 0; i < list_steps.size(); i++) {
            safeFill("input[name='step-" + i + "']", list_steps.get(i).toString());

            if (i != list_steps.size() - 1) {
                safeLocatorClick(addNewStep_btn);
            }
        }
    }

    public void createNewTestCase(String title,String description, String expectedResult){
        Allure.step("Creating new test with title:" + title + "\tand description: " + description);
        safeLocatorClick(createNewBtn);
        ArrayList<Number> steps = new ArrayList<>();
        for(int i = 0;i<10;i++){
            steps.add(ThreadLocalRandom.current().nextInt(1,1_000_000));
        }
        fillCreationForm(title, description, expectedResult, steps);
        safeLocatorClick(submit_btn);
    }

    public String deleteCase() throws Exception {
        String caseID = getCaseIDFromURL();
        Allure.step("Deleting test case with ID: "+caseID);

        Locator deleteBtn = verifyIndividualPage(page);
        safeLocatorClick(deleteBtn);

        Locator confirmDeleteBtn = getRemoveTestCaseButtonPopup();
        safeLocatorClick(confirmDeleteBtn);

        return caseID;
    }

    public Locator getCreationError(){ return page.locator("#validation-msg");}

    public Locator getSuccessToast(){
        return page.locator("div.Toastify__toast.Toastify__toast--default.SUCCESS_TOAST").first();
    }
}
