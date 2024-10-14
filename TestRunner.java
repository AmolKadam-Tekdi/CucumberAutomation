/*
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/main/java/feature",  // Path to your feature files
        glue = {"src/main/java/stepdefinitions"},                // Package containing step definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"}, // Report generation
        monochrome = true ,                         // Makes the console output readable
        tags = "@SmokeTest"                        // Optional: Add tags to run specific scenarios
)
public class TestRunner {
}
*/



import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/feature",   // Path to your feature files
        glue = {"src/main/java/stepdefinitions"},                // Package containing step definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"}, // Generates HTML report
        monochrome = true                          // Makes console output more readable
        // No need for tags here, as you are passing them via Maven
)
public class TestRunner {
}