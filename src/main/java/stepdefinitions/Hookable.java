package stepdefinitions;

import com.aventstack.extentreports.ExtentReports;
import io.cucumber.java.*;
import org.junit.AfterClass;
import utils.LoggerUtil;
import utils.Reporter;

import java.util.concurrent.TimeUnit;

import static utils.BaseUtils.*;
import static utils.BrowserManager.browserRun;


public class Hookable {


	private static boolean isSuiteInitialized = false;
	private static String featureName = "";



	@Before(order = 1)
	public void setUp(Scenario scenario) throws Exception {
		if (!isSuiteInitialized) {
			// Extract the feature name from the URI path
			featureName = scenario.getUri().getPath();
			featureName = featureName.substring(featureName.lastIndexOf("/") + 1);

			// Sanitize the feature name by removing or replacing illegal characters
			featureName = featureName.replaceAll("[^a-zA-Z0-9\\._]", "_");  // Replace illegal characters with '_'

			// Initialize the report with the sanitized feature name
			Reporter.extent = new ExtentReports();
			Reporter.setupReport(featureName);  // Use sanitized feature name
			isSuiteInitialized = true;
		}

		// Set up the browser and log the scenario name
		browserRun();
		LoggerUtil.setLogFileName(scenario.getName());
		Reporter.createTest(scenario.getName());  // Create a new test for each scenario
		logStep("Scenario: " + scenario.getName());
	}


/*	@Before(order = 0)
	public void setUpSuite(Scenario scenario) throws Exception {
		if (!isSuiteInitialized) {
			Reporter.extent = new ExtentReports();

			String featureName = scenario.getUri().getPath();  // Get feature file path
			featureName = featureName.substring(featureName.lastIndexOf("/") + 1);  // Extract feature file name
			Reporter.setupReport("Feature: " + featureName);  // Initialize the report with the feature name

			isSuiteInitialized = true;
		}
	}

	@Before(order = 1)
	public void setUp(Scenario scenario) throws Exception {
		browserRun();
		LoggerUtil.setLogFileName(scenario.getName());
		Reporter.createTest(scenario.getName());  // Create a new test for each scenario
		logStep("Scenario: " + scenario.getName());
	}*/



	@BeforeStep
	public void beforeStep(Scenario scenario) {
		long timeout = 10;
		TimeUnit unit = null;
		try {
			setImplicitWait(10, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			driver
					.manage()
					.timeouts()
					.implicitlyWait(timeout, unit == null ? TimeUnit.SECONDS : unit);
		}
	}


	@After
	public void tearDownScenario(Scenario scenario) throws Exception {
		logResultStatus(scenario);  // Log the result of the scenario
		driver.quit();

		Reporter.flushReport();     // Flush the report after each scenario
	}

	@AfterClass
	public static void tearDownSuite() {
		Reporter.flushReport();  // Final flush after all tests are executed
	}

	private void logResultStatus(Scenario scenario) throws Exception {
		if (scenario.isFailed()) {
			Reporter.logFail("**** " + scenario.getName() + " has FAILED ****", driver);
		} else if (scenario.getStatus() == Status.PASSED) {
			Reporter.logPass("**** " + scenario.getName() + " has PASSED ****");
		} else {
			Reporter.logStep("**** " + scenario.getName() + " has been SKIPPED ****");
		}
	}
}






/*
	@Before
	public void setUp(Scenario scenario) throws Exception {
		browserRun();
		LoggerUtil.setLogFileName(scenario.getName());
		Reporter.setupReport("Suite Run");
		Reporter.createTest(scenario.getName());
		logStep("Scenario: " + scenario.getName());
	}
*/


