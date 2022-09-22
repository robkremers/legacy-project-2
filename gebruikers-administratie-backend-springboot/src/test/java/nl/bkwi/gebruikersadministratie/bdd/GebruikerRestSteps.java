package nl.bkwi.gebruikersadministratie.bdd;

import io.cucumber.java.nl.Als;
import io.cucumber.java.nl.Dan;
import io.cucumber.java.nl.Gegeven;
import io.cucumber.java.nl.Wanneer;
import io.cucumber.java.nl.En;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikersadministratie.BeheerderConstants;
import nl.bkwi.gebruikersadministratie.RestServiceMethods;
import org.json.JSONException;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;

@Slf4j
public class GebruikerRestSteps {
  RequestSpecification requestSpecification;
  Response response;


  @Gegeven("^een BKWI beheerder is ingelogd")
  public void een_bkwi_beheerder_is_ingelogd() {
    requestSpecification =
        RestServiceMethods
            .inloggen_beheerder("beheerder_bkwi", BeheerderConstants.BKWI_BEHEERDER_DN);
  }

  @Gegeven("^een DUO beheerder is ingelogd")
  public void een_duo_beheerder_is_ingelogd() {
    requestSpecification =
        RestServiceMethods.inloggen_beheerder("beheerder_duo", BeheerderConstants.DUO_BEHEERDER_DN);
  }

  @Wanneer("^de lijst van gebruikers wordt opgehaald")
  public void de_lijst_van_gebruikers_wordt_opgehaald() {
    response = requestSpecification
        .when()
        .get(BeheerderConstants.PATH_URL);
  }

  @Dan("bevat de lijst de verwachte lijst van BKWI gebruikers")
  public void bevat_de_lijst_de_gemockte_lijst_van_gebruikers()
      throws IOException {
    assertVerwachteGebruikersAlsInJSONFile("/expected-responses/bkwi-gebruikerslijst.json");
  }

  @Dan("bevat de lijst de verwachte lijst van DUO gebruikers")
  public void bevat_de_lijst_de_verwachte_lijst_van_DUO_gebruikers()
      throws IOException {
    assertVerwachteGebruikersAlsInJSONFile("/expected-responses/duo-gebruikerslijst.json");
  }

  //er een verkeerde header wordt bevraagd
  @Als("^er een verkeerde header wordt bevraagd")
  public void er_een_verkeerde_header_wordt_bevraagd() {
    requestSpecification = RestAssured
        .given()
        .baseUri(BeheerderConstants.BASE_URL)
        .header("am_uid1", "beheerder_duo")
        .header("am_dn1", "cn=DUO Beheerder,ou=DUO,o=suwi,c=nl");
  }

  //komt er een status 401 terug
  @Dan("komt er een status {int} terug")
  public void komt_er_een_status_400_terug(int statusCode) {
    requestSpecification
        .when()
        .get(BeheerderConstants.PATH_URL).then()
        .assertThat()
        .statusCode(statusCode);
  }

  //Wanneer de identificerende_gegevens van "gebruiker_bkwi" wordt opgehaald
  @Wanneer("de identificerende gegevens van {string} wordt opgehaald")
  public void de_identificerende_gegevens_wordt_opgehaald(String gebruiker) {
    response = requestSpecification
        .when()
        .get(BeheerderConstants.PATH_URL + "/" + gebruiker);
    response.getBody().peek();//userId
  }

  //bevat de lijst de verwachte identificerende gegevens van de gebruiker
  @Dan("bevat de lijst de verwachte identificerende gegevens van de gebruiker")
  public void bevat_de_lijst_de_verwachte_identificerende_gegevens_van_de_gebruiker()
      throws IOException, JSONException {
    assertVerwachteDetailGebruikersAlsInJSONFile(
        "/expected-responses/gebruiker_identificerende_gegevens.json");
  }

  //En bevat de lijst geen "userPassword"
  @En("bevat de lijst geen {string}")
  public void bevat_de_gegevens_geen(String userPassword) {
    ResponseBody body = response.getBody();
    String bodyAsString = body.asString();
    Assert.assertFalse(bodyAsString.contains(userPassword));
  }

  //En bevat de lijst  de inlognaam
  @En("de waarde van {string} is {string}")
  public void de_waarde_van_de_key(String userId, String gebruiker) {
    ResponseBody body = response.getBody();
    String bodyAsString = body.asString();
    Assert.assertTrue(bodyAsString.contains(gebruiker));

    JsonPath jpath = new JsonPath(bodyAsString);
    String placeId = jpath.getString(userId);
    Assert.assertEquals(placeId, gebruiker);
  }

  @Als("de waarde van password {string} en de waarde van confirmation {string} voor {string} dan is de statuscode {int}")
  public void de_waarde_van_password_en_de_waarde_van_confirmation_is_de_statuscode(String password,
      String confirmation, String inlogNaam, int verwachtestatusCode) {
    //Hier wordt bepaald met welke beheerder ga je  inloggen
    String inlogBeheerder = "";
    String am_uid = "";
    if (inlogNaam.equals("beheerder_bkwi") || inlogNaam.equals("gebruiker_bkwi")) {
      inlogBeheerder = BeheerderConstants.BKWI_BEHEERDER_DN;
      am_uid = "beheerder_bkwi";
    }
    if (inlogNaam.equals("beheerder_duo") || inlogNaam.equals("gebruiker_duo")) {
      inlogBeheerder = BeheerderConstants.DUO_BEHEERDER_DN;
      am_uid = "beheerder_duo";
    }
    response = RestAssured
        .given()
        .baseUri(BeheerderConstants.BASE_URL)
        .header("Content-type", "application/json")
        .header("am_uid", am_uid)
        .header("am_dn", inlogBeheerder)
        .body(RestServiceMethods.updateGebruikerPassword(password, confirmation))
        .when()
        .put(BeheerderConstants.PATH_URL_UPDATEPASSWORD + inlogNaam)
        .then()
        .extract()
        .response();
    int statusCode = response.getStatusCode();
    Assert.assertEquals(verwachtestatusCode, statusCode);
  }


  private void assertVerwachteGebruikersAlsInJSONFile(String expectedGebruikersJSONFile)
      throws IOException {
    ResponseBody responseBody = response.then()
        .assertThat()
        .statusCode(BeheerderConstants.HTTP_STATUS_CODE_OK)
        .contentType(ContentType.JSON)
        .extract()
        .response()
        .body();
    String body = responseBody.asString();
    String expectedJSON = RestServiceMethods.readExpectedJSONFromFile(expectedGebruikersJSONFile);
    RestServiceMethods
        .assertExpectedJsonIsSubsetOfActualJson(expectedJSON, body, "gebruiker", "userId");
  }

  private void assertVerwachteDetailGebruikersAlsInJSONFile(String expectedGebruikersJSONFile)
      throws IOException, JSONException {
    ResponseBody responseBody = response.then()
        .assertThat()
        .statusCode(BeheerderConstants.HTTP_STATUS_CODE_OK)
        .contentType(ContentType.JSON)
        .extract()
        .response()
        .body();
    String body = responseBody.asString();
    String expectedJSON = RestServiceMethods.readExpectedJSONFromFile(expectedGebruikersJSONFile);
    JSONAssert.assertEquals(expectedJSON, body, true);
  }

}







