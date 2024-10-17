package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.BrowserManagementUtilities.BrowserManager;

public class prerakSteps extends BrowserManager {

	By preraks = By.xpath("(//*[text()='Preraks'])[2]");
	By prerakLogo = By.xpath("//*[text()='All Preraks']");


	@When("The user clicks on the Preraks button")
	public void the_user_clicks_on_the_preraks_button() {
		logStep("the user clicks on the Preraks button");
		WebElement preraksbutton = driver.findElement(preraks);
		assertTrue("Prerak button is displayed to the user",isElementDisplayed(preraksbutton),"Prerak Button is not displayed to the user");
		click(preraksbutton);
	}

	@Then("The system should display the Preraks page")
	public void the_system_should_display_the_preraks_page() {
		logStep("The system should display the Preraks page");
		WebElement PrerakLogo = driver.findElement(prerakLogo);
		assertTrue("Preraks Page is displayed to the user",isElementDisplayed(PrerakLogo),"Preraks Page is not displayed to the user");
	}





}
