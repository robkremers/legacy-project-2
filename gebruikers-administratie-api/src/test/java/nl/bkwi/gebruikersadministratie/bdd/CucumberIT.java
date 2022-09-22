package nl.bkwi.gebruikersadministratie.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import nl.bkwi.GebruikersAdministratieApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = GebruikersAdministratieApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    glue = {"nl.bkwi.gebruikersadministratie"},
    features = {"src/test/resources"})
//src/test/java/resources/cucumber
public class CucumberIT {

}

