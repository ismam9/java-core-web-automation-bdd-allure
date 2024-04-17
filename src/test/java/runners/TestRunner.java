package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features =  "src/test/resources/features",
        glue = {"stepDefinitions", "runners"},
        monochrome=true,
        //dryRun=true,
        plugin = {
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "pretty",
                "html:target/cucumber-report/cucumber.html",
                "json:target/cucumber.json",
                "rerun:target/rerun.txt"
                }
        )
public class TestRunner {

}
