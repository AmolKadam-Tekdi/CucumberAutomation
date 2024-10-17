package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.BaseUtilities.BaseUtils;
import utils.BrowserManagementUtilities.BrowserManager;

import java.util.concurrent.TimeUnit;

public class LearnersTableSteps extends BrowserManager {
	By learnerTab = By.xpath("(//*[text()='Learners'])[2]");
	By identified = By.xpath("//*[text()='Identified']");
	By selectAcademicYear = By.xpath("(//*[@aria-label='Select'])[1]");
	By selectState = By.xpath("(//*[@aria-label='Select'])[2]");
	By viewButton = By.xpath ("(//*[text()='View' and contains(@class,'css-901oao r-1f4ndn9 r-1odpvz2 r-1b43r93 r-13uqrnb r-16dba41 r-oxtfae r-dhbnww')])[1]");
	By learnersNameElement = By.xpath("//*[@class='css-901oao r-op4f77 r-1odpvz2 r-1x35g6 r-13uqrnb r-b88u0q r-oxtfae r-dhbnww r-1udh08x r-1udbk01 r-3s2u2q']");
	By learnersIDElement = By.xpath("//*[@class='css-1dbjc4n r-t42m4q r-sdzlij r-fxxt2n r-15zivkp r-13hce6t r-1d4mawv r-14gqq1x r-iphfwy r-1m04atk r-1pyaxff r-1h8ys4a r-q4m81j']");
	By learnersStatusElement = By.xpath("//*[@class='css-1dbjc4n r-1x08z4s r-z2wwpe r-15zivkp r-13hce6t r-1d4mawv r-14gqq1x r-iphfwy r-1qhn6m8 r-i023vh r-1h8ys4a']");









	String learnersID, learnersName, learnersAge, learnersStatus;
	String learnersIDonBenificiaryPage, learnersNameonBenificiaryPage, learnersAgeonBenificiaryPage, learnersStatusonBenificiaryPage;







	@When("the user selects {string} as academic year")
	public void the_user_selects_string_as_academic_year(String inputString) {
		logStep("the user selects academic year");
		WebElement selectAcademicYeaR = BaseUtils.driver.findElement(selectAcademicYear);

		Select sel = new Select(selectAcademicYeaR);
		sel.selectByVisibleText(inputString);
	}


	@And("User select {string} as State")
	public void user_select_string_as_state(String inputString) {
		logStep("User selects State");
		WebElement selectState = BaseUtils.driver.findElement(this.selectState);

		Select sel = new Select(selectState);
		sel.selectByVisibleText(inputString);
	}




	@Then("System should display the learner users table to the user")
	public void system_should_display_the_learner_users_table_to_the_user() {
		logStep("System should display the learner users table to the user");
		setImplicitWait(10, TimeUnit.SECONDS);

		WebElement identified = BaseUtils.driver.findElement(this.identified);

		assertTrue("Learners table is displayed to the user",isElementDisplayed(identified),"Learners table is not displayed to the user");

	}


	@When("User clicks on the Learners button")
	public void user_clicks_on_the_learners_button() {
		logStep("User clicks on the Learners button");
		setImplicitWait(10, TimeUnit.SECONDS);

		WebElement learnerTab = BaseUtils.driver.findElement(this.learnerTab);
		click(learnerTab);
	}


	@When("User clicks on the View Button for the first user")
	public void user_clicks_on_the_view_button_for_the_first_user() {
		logStep("User clicks on the View Button for the first user");
		boolean result = searchElementByText(2, "Pritam"); // Searching learner's name
		learnersID = getLearnerUserInfoByIndex(0,1);
		learnersName = getLearnerUserInfoByIndex(0,2);
		learnersAge = getLearnerUserInfoByIndex(0,3);
		learnersStatus = getLearnerUserInfoByIndex(0,5);
//		logStep(learnersID + learnersName+ learnersAge+ learnersStatus);
		System.out.println("Search result: " + result);
		WebElement ViewButton = BaseUtils.driver.findElement(this.viewButton);
		assertTrue("View button is displayed on the table",isElementDisplayed(ViewButton),"View button is not displayed on the table");
		click(ViewButton);
	}



	@Then("the system Display profile infornation")
	public void the_system_display_profile_infornation() {
		logStep("the system Display profile infornation");
		WebElement LearnersNameElement = BaseUtils.driver.findElement(learnersNameElement);
		WebElement LearnersIDElement = BaseUtils.driver.findElement(learnersIDElement);
		WebElement LearnersStstusElement = BaseUtils.driver.findElement(learnersStatusElement);

		learnersNameonBenificiaryPage = LearnersNameElement.getText();
		learnersIDonBenificiaryPage = LearnersIDElement.getText();
		learnersStatusonBenificiaryPage = LearnersStstusElement.getText();

		logStep(learnersNameonBenificiaryPage + learnersIDonBenificiaryPage + learnersStatusonBenificiaryPage);

		assertEquals("Learners name on the benificiary page is same as on the learners table" , learnersName,learnersNameonBenificiaryPage ,"Learners name on the benificiary page is not same as on the learners table" );
		assertEquals("Learners name on the benificiary page is same as on the learners table" , learnersID,learnersIDonBenificiaryPage ,"Learners name on the benificiary page is not same as on the learners table" );
		assertEquals("Learners status on the benificiary page is same as on the learners table" , learnersStatus,learnersStatusonBenificiaryPage ,"Learners name on the benificiary page is not same as on the learners table" );

	}


}
