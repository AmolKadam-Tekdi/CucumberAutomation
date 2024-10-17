package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.BaseUtilities.BaseUtils;
import utils.BrowserManagerUtilities.BrowserManager;

public class LoginSteps extends BrowserManager {





	@Given("the user is on the login page")
	public void the_user_is_on_the_login_page() {
		logStep("the user is on the login page");
	}

	@When("the user enters the username {string}")
	public void the_user_enters_the_username_string_(String inputString) {
		logStep("the user enters the username ");

	}

	@And("the user enters the password {string}")
	public void the_user_enters_the_password_string_(String inputString) {
		logStep("the user enters the password");

	}

	@And("the user clicks on the login button")
	public void the_user_clicks_on_the_login_button() {
		logStep("the user clicks on the login button");

	}


	@Then("the user should see {string} on the home page")
	public void the_user_should_see_string_on_the_home_page(String inputString) {


	}



	@When("the user clicks on the continue button")
	public void the_user_clicks_on_the_continue_button() {
		logStep("the user clicks on the continue button");

	}


	@When("A user enters the username {string}")
	public void a_user_enters_the_username_string_(String inputString) {
		logStep("A user enters the username " + inputString + "");
		// TODO: Implement step
		throw new io.cucumber.java.PendingException();
	}



}
