package stepdefinitions;

import com.aventstack.extentreports.ExtentReports;
import io.cucumber.java.*;
import org.junit.AfterClass;
import utils.LoggingUtilities.LoggerUtil;
import utils.ReportingUtilities.Reporter;

import java.util.concurrent.TimeUnit;

import static utils.BaseUtilities.BaseUtils.*;
import static utils.BrowserManagementUtilities.BrowserManager.browserRun;


public class Hookable {

	private static boolean isSuiteInitialized = false;
	private static String featureName = "";

	@Before(order = 1)
	public void setUp(Scenario scenario) throws Exception {
		if (!isSuiteInitialized) {
			featureName = scenario.getUri().getPath();
			featureName = featureName.substring(featureName.lastIndexOf("/") + 1);

			featureName = featureName.replaceAll("[^a-zA-Z0-9\\._]", "_");  // Replace illegal characters with '_'

			Reporter.extent = new ExtentReports();
			Reporter.setupReport(featureName);  // Use sanitized feature name
			isSuiteInitialized = true;
		}

		browserRun();
		LoggerUtil.setLogFileName(scenario.getName());
		Reporter.createTest(scenario.getName());  // Create a new test for each scenario
		logStep("Scenario: " + scenario.getName());
	}

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
		driver.quit();

		logResultStatus(scenario);  // Log the result of the scenario
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


