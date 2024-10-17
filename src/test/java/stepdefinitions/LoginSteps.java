package stepdefinitions;

import io.cucumber.java.en.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.BrowserManagerUtilities.BrowserManager;;

public class LoginSteps extends BrowserManager{



	By usernameField = By.xpath("//*[@placeholder='Enter Username']");
	By passwordField = By.xpath("//*[@placeholder='Enter Password']");
	By loginButton = By.xpath("(//*[text()='Login'])[4]");
	By academicYear = By.xpath("//*[text()='Academic Year']");
	By selectAcademicYear = By.xpath("(//*[@aria-label='Select'])[1]");
	By selectState = By.xpath("(//*[@aria-label='Select'])[2]");

	@Given("the user is on the login page")
	public void the_user_is_on_the_login_page() {
		logStep("the user is on the login page");
	}

	@When("the user enters the username {string}")
	public void the_user_enters_the_username_string_(String inputString) {
		logStep("the user enters the username " + inputString + "");
		WebElement usernamefield = driver.findElement(usernameField);

		assertTrue("Username field is displayed on the login page",isElementDisplayed(usernamefield),"Username field is not displayed on the login page");
		sendTextOnUI( usernamefield,inputString);
	}

	@And("the user enters the password {string}")
	public void the_user_enters_the_password_string_(String inputString) {
		logStep("the user enters the password " + inputString + "");
		WebElement passwordfield = driver.findElement(passwordField);
		assertTrue("Password field is displayed on the login page",isElementDisplayed(passwordfield),"Password field is not displayed on the login page");
		sendTextOnUI( passwordfield,inputString);
	}

	@And("the user clicks on the login button")
	public void the_user_clicks_on_the_login_button() {
		logStep("the user clicks on the login button");
		WebElement loginbutton = driver.findElement(loginButton);
		assertTrue("Login Button is displayed on the login page",isElementDisplayed(loginbutton),"Login Button is not displayed on the login page");

		click(loginbutton);

	}

	@Then("the user should see {string} on the home page")
	public void the_user_should_see_string_on_the_home_page(String inputString) {
		logStep("the user should see " + inputString + " on the home page");
		WebElement academicYear = driver.findElement(this.academicYear);

		String academicYearText = academicYear.getText();
		assertEquals("User is successfully logged in the system",academicYearText," Academic Year","User log in failed");

	}

}
