package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.BaseUtilities.BaseUtils;
import utils.BrowserManagerUtilities.BrowserManager;

import java.util.concurrent.TimeUnit;

public class LearnersTableSteps extends BrowserManager {




	@When("the user selects {string} as academic year")
	public void the_user_selects_string_as_academic_year(String inputString) {

	}


	@And("User select {string} as State")
	public void user_select_string_as_state(String inputString) {
		logStep("User selects State");

	}




	@Then("System should display the learner users table to the user")
	public void system_should_display_the_learner_users_table_to_the_user() {
		logStep("System should display the learner users table to the user");

	}


	@When("User clicks on the Learners button")
	public void user_clicks_on_the_learners_button() {
		logStep("User clicks on the Learners button");

	}


	@When("User clicks on the View Button for the first user")
	public void user_clicks_on_the_view_button_for_the_first_user() {
		logStep("User clicks on the View Button for the first user");

	}



	@Then("the system Display profile infornation")
	public void the_system_display_profile_infornation() {
		logStep("the system Display profile infornation");

	}


}
