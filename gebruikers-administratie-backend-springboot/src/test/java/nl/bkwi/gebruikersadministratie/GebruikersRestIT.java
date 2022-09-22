package nl.bkwi.gebruikersadministratie;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:gebruiker-rest.feature",
    plugin = {"pretty", "html:target/cucumber",
        "json:target/cucumber-gebruikers-rest.json",
        "html:target/cucumber-html-report",
        "json:target/cucumber-reports/cucumber.json", "junit:target/cucumber-reports/cucumber.xml"},
    tags = "not @Ignore",
    glue = "nl.bkwi",
    monochrome = true
)

public class GebruikersRestIT {

}
