package stepdefinitions;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/feature",   // Path to your feature files
        glue = {"stepdefinitions"},                // Package containing step definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"}, // Generates an HTML report
        monochrome = true                          // Makes console output more readable
)
public class TestRunner {
}