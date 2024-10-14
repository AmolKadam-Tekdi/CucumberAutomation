package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.BaseUtils;
import utils.BrowserManager;

public class LoginSteps extends BrowserManager {

	By usernameField = By.xpath("//*[@placeholder='Enter Username']");
	By passwordField = By.xpath("//*[@placeholder='Enter Password']");
	By loginButton = By.xpath("(//*[text()='Login'])[4]");
	By AcademicYear = By.xpath("//*[text()='Academic Year']");
	By Continue = By.xpath("//*[text()='Continue']");
	By SelectAcademicYear = By.xpath("(//*[@aria-label='Select'])[1]");
	By SelectState = By.xpath("(//*[@aria-label='Select'])[2]");




	@Given("the user is on the login page")
	public void the_user_is_on_the_login_page() {
		logStep("the user is on the login page");
	}

	@When("the user enters the username {string}")
	public void the_user_enters_the_username_string_(String inputString) {
		logStep("the user enters the username ");
		WebElement usernamefield = BaseUtils.driver.findElement(usernameField);
		sendTextOnUI( usernamefield,inputString);
	}

	@And("the user enters the password {string}")
	public void the_user_enters_the_password_string_(String inputString) {
		logStep("the user enters the password");
		WebElement passwordfield = BaseUtils.driver.findElement(passwordField);
		sendTextOnUI( passwordfield,inputString);
	}

	@And("the user clicks on the login button")
	public void the_user_clicks_on_the_login_button() {
		logStep("the user clicks on the login button");
		WebElement loginbutton = BaseUtils.driver.findElement(loginButton);
		click(loginbutton);
	}

/*	@Then("the user should see {string} on the home page")
	public void user_sees_academic_year(String expectedText) throws Exception {
		WebElement academicYear = BaseUtils.driver.findElement(AcademicYear);

		String AcademicYeartext = academicYear.getText();
		assertEquals("User is successfully logged in the system",AcademicYeartext," Academic Year","User log in failed");

	}*/

	@Then("the user should see {string} on the home page")
	public void the_user_should_see_string_on_the_home_page(String inputString) {

		WebElement academicYear = BaseUtils.driver.findElement(AcademicYear);

		String AcademicYeartext = academicYear.getText();
		assertEquals("User is successfully logged in the system",AcademicYeartext," Academic Year","User log in failed");

	}


	@When("the user clicks on the continue button")
	public void the_user_clicks_on_the_continue_button() {
		logStep("the user clicks on the continue button");
		WebElement continu = BaseUtils.driver.findElement(Continue);
		click(continu);
	}


}
