package stepdefinitions;

import io.cucumber.java.en.*;

import utils.BrowserManagerUtilities.BrowserManager;;

public class prerakSteps extends BrowserManager{




	@When("The user clicks on the Preraks button")
	public void the_user_clicks_on_the_preraks_button() {
		logStep("The user clicks on the Preraks button");

	}

	@Then("The system should display the Preraks page")
	public void the_system_should_display_the_preraks_page() {
		logStep("The system should display the Preraks page");

	}

}
