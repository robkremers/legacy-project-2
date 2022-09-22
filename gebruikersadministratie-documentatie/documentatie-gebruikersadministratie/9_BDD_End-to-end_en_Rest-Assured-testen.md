# BDD End-to-end en Rest-Assured-testen

## Introductie

Voor gebruiksadministratie hebben we ervoor gekozen om acceptatiecriteria te formuleren bij de user stories aan de hand
van de Behaviour Driven Development (BDD) aanpak. Per feature of functionaliteit zijn er dan scenario's geformuleerd
volgens het 'Given - when - then' principe. In het Nederlands ook wel: Gegeven - Wanneer - Dan. Deze formulering van
acceptatiecriteria en daarmee van de verwachte werking van de applicatie wordt door de ontwerper gemaakt. Deze
gestructureerde formuleringen zijn door de ontwikkelaar en tester makkelijk te interpreteren en te vertalen naar (test)
code. Dit doen we dankzij de Gherkin syntax.

Eerst een overzicht van de technieken die we gebruiken om onze BDD testen te realiseren:

## Technieken

Bij onze BDD testen gebruiken we verschillende frameworks:

- [Cucumber](http://angiejones.tech/rest-assured-with-cucumber-using-bdd-for-web-services-automation?refreshed=y) ->
  voor de verbinding tussen de BDD userstories en de RestAssured test code met behulp van de Gherkin syntax.
    - Zie [voorbeeld project](https://github.com/EnlightenedSoftware/restassured-with-cucumber-demo)
- [Rest-Assured](http://rest-assured.io/) voor het lezen en controleren van de responses. Dankzij Rest-Assured hebben we
  hiervoor geen user interface nodig.

## Stap 1: Van acceptatiecriteria naar feature files

Wanneer we de beschikking hebben over een feature (de user story ofwel een stukje functionaliteit) dan kunnen we deze in
het applicatieproject toevoegen als een feature file. Onder src-test-resources kunnen we een map 'features' maken.
Daaronder kun je eventueel nog submappen aanmaken. Dan wordt daarin een file geplaatst met een naam die eindigt op
'.feature'.

Om deze feature file te kunnen interpreteren heb je een aantal dependencies nodig in je pom.xml:

- cucumber-java
- cucumber-junit
- rest-assured

Zie voor de volledige dependencies de pom.xml van het de `gebruikers-administratie-api` en
`gebruikers-administratie-backend-springboot` projecten als voorbeeld.

- pom gebruikers-administratie-api:
    - Path: `gebruikers-administratie-api/pom.xml`
- pom gebruikers-administratie-backend-springboot:
    - Path: `gebruikers-administratie-backend-springboot/pom.xml`

Een feature file kan uit meerdere scenario's bestaan en kan er bijvoorbeeld als volgt uitzien:

``` gherkin
#language: nl
Functionaliteit: Testen of de menu items van de landingspagina aawezig zijn

  Scenario: Controleer dat de 8 items aanwezig zijn bij inlogen van een BKWI Beheerder
    Wanneer een BKWI Beheerder gaat inlogen
    Dan     het gebruikersnaam BKWI Beheerder is aanwezig
    En      de acht items zijn aanwezig

  Scenario: Controleer dat de 8 items aanwezig zijn bij inlogen van een DUO Beheerder
    Wanneer een DUO Beheerder gaat inlogen
    Dan     het gebruikersnaam DUO Beheerder is aanwezig
    En      de acht items zijn aanwezig
```

Ieder scenario is als het ware een uit te voeren test. Dankzij de Cucumber/Gherkin plugin worden de feature files goed
leesbaar. De woorden die in Gherkin een speciale betekenis hebben worden dan blauw gekleurd. Bijvoorbeeld '
Functionaliteit', 'Scenario', 'Gegeven' en 'Wanneer'. Je kunt Cucumber ook instellen in het Engels, maar wij hebben voor
Nederlands gekozen dankzij de '#language: nl' aan het begin. Vanaf ieder blauw codewoord begint een nieuwe stap in het
scenario.

## Stap 2: het maken van een algemene cucumber klasse

Om de stappen in de scenario's uit te kunnen voeren, moeten deze gekoppeld worden aan testcode, die daadwerkelijk iets
'doen' of 'controleren' in de applicatie. Daarvoor moet Cucumber 'weten' welke feature files bij welke 'Step
definitions' horen. Step definitions zijn klassen waarin de verschillende stappen met behulp van java code worden
uitgevoerd. Dankzij annotaties worden de stappen gekoppeld aan de woorden uit de Gherkin syntax (Gegeven - Wanneer -
Dan).

Maar ook moet Cucumber centraal weten in welke mappen de feature files en step definition klassen gevonden kunnen
worden. Dit doe je door middel van een algemene Cucumber test klasse met annotaties die de configuratie verzorgen. Deze
klasse heet 'CucumberIT.java'.

- ClassPath: gebruikers-administratie-api:
    - `gebruikers-administratie-api/src/test/java/nl/bkwi/gebruikersadministratie/bdd/CucumberIT.java`
- ClassPath: gebruikers-administratie-backend-springboot:
    - `gebruikers-administratie-backend-springboot/src/test/java/nl/bkwi/gebruikersadministratie/bdd/CucumberIT.java`

De klasse kan er bijvoorbeeld zo uitzien (in het `gebruikers-administratie-backend-springboot` project):

``` java
package nl.bkwi.gebruikersadministratie.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import nl.bkwi.gebruikersadministratie.GebruikersAdministratieApplication;
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
public class CucumberIT {

}
```

Dankzij de annotatie `@CucumberOptions` en de directories onder `glue` en `features` weet het framework waar de
betrokken informatie te vinden is.

## Stap 3: van feature files naar step definitions

Zoals gezegd, bij alle stappen in de scenario's moet code geschreven worden. Wanneer je een nieuwe stap toevoegt in de
feature file, die nog geen implementatie heeft, dan zie je dat doordat deze stap licht gemarkeerd wordt. Je kunt dan op
het gele lampje klikken en kiezen voor `Create step definition`. Vervolgens kun je kiezen uit een bestaande Step
Definition klasse of voor het maken van een nieuwe klasse. De klasse naam eindigt op `.StepDefinitions.java`

In de Step Definition klasse wordt dan automatisch het Gherkin woord uit de stap toegevoegd als annotatie met daarbij de
stap in tekst en een lege methode. Deze kun je vervolgens zelf verder invullen met code. Wij schrijven bij
regression-test en samenwerkingsverband-api-v001 deze code bij de teststappen met behulp van jUnit en Rest-Assured.

Bijvoorbeeld, uit het project `gebruikers-administratie-backend-springboot`:

- Path:
    - `/src/test/java/nl/bkwi/gebruikersadministratie/bdd/LandingspaginaSteps.java`
    - `/src/test/java/nl/bkwi/gebruikersadministratie/bdd/GebruikerRestSteps.java`

Voorbeeld:

``` java
 @Wanneer("^de landingspagina is geopend")
  public void de_landingspagina_is_geopend() {
    //firefoxModheader();
    chromeModheader();
    inlogFunction(Bkwi_Header);
    driver.get(BaseUrl);
  }

  //Wanneer een BKWI beheerder gaat inlogen
  @Wanneer("^een BKWI Beheerder gaat inlogen")
  public void een_BKWI_Beheerder_gaat_inlogen() {
    //firefoxModheader();
    chromeModheader();
    inlogFunction(Bkwi_Header);
    driver.get(BaseUrl);
  }
```    

U ziet dat de tekst achter `@Gegeven`, `@Wanneer`, `@Dan` en `@En` overeenkomt met de tekst in de feature file.

#### Gebruik van Rest-Assured

In de step definitions maken we gebruik van Rest-Assured dependencies om de testen uit te kunnen voeren tegen de
applicatie.

**Rest-Assured:**

Met behulp van Rest-assured kunnen we HTTP-requests sturen en de response opvangen en verifiëren. Zo kunnen we dankzij
Rest-assured bijvoorbeeld aangeven om welke soort request het gaat (POST of GET bijvoorbeeld) en om welke headers en
welke body er aan de request worden meegegeven.

### Gebruiken van expected-responses file

Om de expected-response file te gebruiken hebben we drie java methoden gebruikt

- Project: gebruikers-administratie-backend-springboot:
    - `src/test/resources/expected-responses`
- Project: gebruikers-administratie-api:
    - `src/test/resources/expected-responses`

``` java
@Dan("bevat de lijst de verwachte lijst van BKWI gebruikers")
  public void bevat_de_lijst_de_verwachte_lijst_van_BKWI_gebruikers()
    throws IOException, JSONException {
    assertVerwachteGebruikersAlsInJSONFile("/expected-responses/bkwi-gebruikerslijst.json");
  }
```

- De drie java methoden zijn te vinden in:
    - `src/test/java/nl/bkwi/gebruikersadministratie/bdd/GebruikerRestSteps.java`

``` java
  private void assertVerwachteGebruikersAlsInJSONFile(String expectedGebruikersJSONFile)
    throws IOException, JSONException {
    ResponseBody responseBody = response.then()
      .assertThat()
      .statusCode(HttpStatus.OK.value())
      .contentType(ContentType.JSON)
      .extract()
      .response()
      .body();
    String body = responseBody.asString();
    String expectedJSON = readExpectedJSONFromFile(expectedGebruikersJSONFile);
    JSONAssert.assertEquals(expectedJSON, body, false);
  }
  
  private String readExpectedJSONFromFile(String pathRelativeToSrcTestResources)
    throws IOException {
    InputStream inputStream = GebruikerRestSteps.class.getResourceAsStream(
      pathRelativeToSrcTestResources);
    String expectedJSON = readFromInputStream(inputStream);
    return expectedJSON;
  }

  private String readFromInputStream(InputStream inputStream)
    throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br
      = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }
``` 

# Testen

De testen worden per onderdeel in een eigen feature-file geschreven.

Op dit moment zijn de volgende testen aangemaakt:

### gebruikers-administratie-backend-springboot

In de gebruikers-administratie-backend-springboot project zijn de volgende testen aanwezig:

- `./src/test/resources/gebruikersoverzicht.feature`
- `./src/test/resources/gebruiker-rest.feature`
- `./src/test/resources/Landingspagina.feature`

| BDD-test                                                           | Feature-file        | Omschrijving                                                  |
|--------------------------------------------------------------------|---------------------|---------------------------------------------------------------|
| Testen of de menu items van de landingspagina aanwezig zijn        | Landingspagina      | Testen of de menu items van de landingspagina aanwezig zijn   |
| De LDAP lijst van gebruikers halen                                 | gebruikersoverzicht | Testen of de service om de LDAP lijst van gebruikers te halen |
| Testen of de service om een gemockte lijst van gebruikers te maken | gebruiker-rest      | een BKWI beheerder vraagt een lijst van gebruikers op         |

### gebruikers-administratie-api

In de gebruikers-administratie-api project zijn de volgende testen aanwezig:

- `/src/test/resources/gebruiker-rest.feature`

| BDD-test                                                      | Feature-file   | Omschrijving                                         |
|---------------------------------------------------------------|----------------|------------------------------------------------------|
| Testen of de service om de LDAP lijst van gebruikers te halen | gebruiker-rest | een BKWI beheerder vraagt een lijst van gebruikers op |
| Testen of de service om de LDAP lijst van gebruikers te halen | gebruiker-rest | een DUO beheerder vraagt een lijst van gebruikers op |
| Testen of de service om de LDAP lijst van gebruikers te halen | gebruiker-rest | Verkeerde haeder in request levert foutmelding |
| Testen of de service om de LDAP lijst van gebruikers te halen | gebruikerIdentificerendeGegevens-rest | een BKWI beheerder vraagt de gegevens van gebruikers op |
| endpoint for reset password | endpointForResetPassword-rest | een BKWI beheerder haalt de identificerende gegevens van beheerder_bkwi op |
| endpoint for reset password | endpointForResetPassword-rest | een BKWI beheerder haalt de identificerende gegevens van gebruiker_bkwi op |
| endpoint for reset password | endpointForResetPassword-rest | een DUO beheerder haalt de identificerende gegevens van beheerder_duo op |


---

[back to main](../README.md) |
[previous](./8_TechnischOntwerp.md) |
[next](./10_Upgrade_to_Angular_14.md)
