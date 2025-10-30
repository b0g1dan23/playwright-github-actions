package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static utils.Allure.logStep;
import static utils.ConfigReader.getBaseUrl;

public class ProjectsPage extends LoggedInPage{
    private final String pageURL = getBaseUrl() + "projects";
    private final Locator addNew_btn = page.locator("a[href='/new-project']");
    private final Locator title_input = page.locator("input[name='title']");
    private final Locator title_submit_btn = page.locator("div.submit-button>button");
    private final Locator person_name_input = page.getByPlaceholder("Person");
    private final Locator person_submit_btn = page.locator("div.project-bottom-technologies>div.submit-button>button").last();
    private final Locator person_error_message = page.locator("#validation-msg");
    private final Locator portraitGrid = page.locator("div.portrait-grid");
    private final Locator people_selector = page.locator("button[data-testid='picky-input']").first();
    private final Locator people_submit_btn = page.locator("div.project-top-people>div.submit-button>button");
    private final Locator back_btn = page.locator("a.navigate-left[href='/projects']");

    public ProjectsPage(Page page) {
        super(page);
    }

    @Override
    public void goTo() {
        page.navigate(pageURL);
        Allure.step("Opened projects page",Status.PASSED);
    }

    private void openCreateModal(){
        safeLocatorClick(addNew_btn);
        Allure.step("Opening create project modal", Status.PASSED);
    }

    private String createProjectTitle() {
        openCreateModal();
        String projectsTitle = "Project " + ThreadLocalRandom.current().nextInt(1, 1_000);
        safeFill("input[name='title']", projectsTitle);
        safeLocatorClick(title_submit_btn);
        Allure.step("Submitting project title: " + projectsTitle, Status.PASSED);
        return projectsTitle;
    }

    public Locator createProjectTitleOnly(){
        String projectTitle = createProjectTitle();
        back_btn.click();
        return findProjectByTitle(projectTitle);
    }

    public String getExistingPersonName() {
        Allure.step("INFO: Getting a random existing person's name");

        Locator personLocator = page.locator("div.person-container-bottom--teams-people--person-name");

        waitForVisible(personLocator.first(), 1000);

        List<String> personNames = getTextListFromLocator(personLocator);

        if (personNames.isEmpty()) {
            throw new RuntimeException("No person names found on the page.");
        }

        Allure.step("Got a random existing person's name", Status.PASSED);
        int randIdx = ThreadLocalRandom.current().nextInt(personNames.size());
        return personNames.get(randIdx);
    }

    public Locator getPersonErrorMessage(){ return person_error_message; }

    public void createNewExistingPerson(){
        String name = getExistingPersonName();
        Allure.step("Filling out the form with existing persons' name");
        person_name_input.fill(name);
        person_submit_btn.click();
        Allure.step("Filled out the form with existing persons' name", Status.PASSED);
    }

    public void createProjectExistingPerson(){
        createProjectTitle();
        createNewExistingPerson();
    }

    public List<String> addPeopleOnProject(Locator project) {
        Allure.step("Adding people on project");
        safeScrollAndClick(project);
        safeLocatorClick(people_selector);

        Locator selectAll = page.locator("div#picky-list div#picky-option-selectall[data-selectall='true']").first();
        waitForVisible(selectAll, 1000);

        List<String> selectedNames = getTextListFromLocator(page.locator("div[data-testid='dropdown'] div[data-testid='option']"));
        safeLocatorClick(selectAll);
        safeScrollAndClick(people_submit_btn);
        page.goBack();
        Allure.step("Added people on project", Status.PASSED);
        return selectedNames;
    }

    public Locator findProjectByTitle(String title) {
        Allure.step("Searching for project with title: " + title);
        page.waitForSelector("a.preview-card");
        Locator projectCards = portraitGrid.locator("a.preview-card");
        int count = projectCards.count();

        for (int i = 0; i < count; i++) {
            Locator card = projectCards.nth(i);
            String actualTitle = card.locator("div.preview-card-title-value").textContent();
            if (title.equals(actualTitle)) {
                return card;
            }
        }

        Allure.step("Found project with title: " + title, Status.PASSED);
        return null;
    }

    public Locator getPreviewButton(Locator locator){
        return locator.locator("div.preview-card-body--items-single-preview-title");
    }

    public List<String> getPersonNamesInPreview() {
        Allure.step("Getting all person names from modal");

        Locator peopleLocator = page.locator("div.modal-body div.project-container-bottom--teams-people--person-name");

        waitForVisible(peopleLocator.first(), 1000);
        Allure.step("Got all person names from modal", Status.PASSED);
        return safeGetAllTextFromLocator(peopleLocator);
    }
}
