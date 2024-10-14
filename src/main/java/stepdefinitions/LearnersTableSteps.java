package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.BaseUtils;
import utils.BrowserManager;

import java.util.concurrent.TimeUnit;

public class LearnersTableSteps extends BrowserManager {
	By LearnerTab = By.xpath("(//*[text()='Learners'])[2]");
	By Identified = By.xpath("//*[text()='Identified']");
	By SelectAcademicYear = By.xpath("(//*[@aria-label='Select'])[1]");
	By SelectState = By.xpath("(//*[@aria-label='Select'])[2]");

	@When("User clicks on the Learners button")
	public void user_clicks_on_the_learners_button() {
		setImplicitWait(10, TimeUnit.SECONDS);

		WebElement learnerTab = BaseUtils.driver.findElement(LearnerTab);
		click(learnerTab);
	}
	@Then("System should display the learner users table to the user")
	public void system_should_display_the_learner_users_table_to_the_user() {
		setImplicitWait(10, TimeUnit.SECONDS);

		WebElement identified = BaseUtils.driver.findElement(Identified);

		assertTrue("Learners table is displayed to the user",isElementDisplayed(identified),"Learners table is not displayed to the user");

	}





	@When("user clicks on the Veiw button for the first user")
	public void user_clicks_on_the_veiw_button_for_the_first_user() {
		// TODO: Implement step
		throw new io.cucumber.java.PendingException();
	}


	@When("the user selects {string} as academic year")
	public void the_user_selects_string_as_academic_year(String inputString) {
		logStep("the user selects academic year");
		WebElement selectAcademicYeaR = BaseUtils.driver.findElement(SelectAcademicYear);

		Select sel = new Select(selectAcademicYeaR);
		sel.selectByVisibleText(inputString);
	}


	@And("User select {string} as State")
	public void user_select_string_as_state(String inputString) {
		logStep("User selects State");
		WebElement selectState = BaseUtils.driver.findElement(SelectState);

		Select sel = new Select(selectState);
		sel.selectByVisibleText(inputString);
	}


}
