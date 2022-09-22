package nl.bkwi.gebruikersadministratie.bdd;

import static org.junit.jupiter.api.Assertions.fail;

import io.cucumber.java.nl.Als;
import io.cucumber.java.nl.Dan;
import io.cucumber.java.nl.Gegeven;
import io.cucumber.java.nl.Wanneer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.BeheerderConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@Slf4j
public class GebruikerRestSteps {
  RequestSpecification requestSpecification;
  Response response;

  @Gegeven("^een BKWI beheerder is ingelogd")
  public void een_bkwi_beheerder_is_ingelogd() {
    requestSpecification = RestAssured
            .given()
            .baseUri(BeheerderConstants.BASE_URL)
            .header("am_uid", "beheerder_bkwi")
            .header("am_dn", BeheerderConstants.BKWI_BEHEERDER_DN);
  }

  @Gegeven("^een DUO beheerder is ingelogd")
  public void een_duo_beheerder_is_ingelogd() {
    requestSpecification = RestAssured
            .given()
            .baseUri(BeheerderConstants.BASE_URL)
            .header("am_uid", "beheerder_duo")
            .header("am_dn", BeheerderConstants.DUO_BEHEERDER_DN);
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
  @Dan("^komt er een status 400 terug")
  public void komt_er_een_status_400_terug() {
    requestSpecification
        .when()
        .get(BeheerderConstants.PATH_URL).then()
        .assertThat()
        .statusCode(BeheerderConstants.HTTP_STATUS_CODE_Unauthorized);
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
    String expectedJSON = readExpectedJSONFromFile(expectedGebruikersJSONFile);

    assertExpectedJsonIsSubsetOfActualJson(expectedJSON, body, "gebruiker", "userId");
  }

  private void assertExpectedJsonIsSubsetOfActualJson(String expectedJson, String actualJson,
      String nameOfEntity, String nameOfUniqueKey) {
    Map<String, JSONObject> jsonObjectMap = new HashMap<>();
    try {
      // 1. plaats alle JSON objecten in de daadwerkelijke JSON in een Map gebaseerd op de unieke sleutel, zodat we JSON objecten makkelijk op basis van de unieke sleutel kunnen opzoeken.
      JSONArray jsonArray = new JSONArray(actualJson);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String uniqueKey = jsonObject.getString(nameOfUniqueKey);
        jsonObjectMap.put(uniqueKey, jsonObject);
      }
      // 2. assert dat alle JSON objecten in de verwachte JSON voorkomen in de daadwerkelijke JSON
      JSONArray expectedJsonArray = new JSONArray(expectedJson);
      for (int j = 0; j < expectedJsonArray.length(); j++) {
        JSONObject expectedJSONObject = expectedJsonArray.getJSONObject(j);
        // zoek de entiteit in de actual json
        String uniqueKey = expectedJSONObject.getString(nameOfUniqueKey);
        JSONObject jsonObject = jsonObjectMap.get(uniqueKey);
        if (jsonObject == null) {
          fail("Verwachte " + nameOfEntity + " met " + nameOfUniqueKey + " '" + uniqueKey
              + "' is niet gevonden in de daadwerkelijke lijst.");
        }
        // 3. assert dat waarden in verwachte object in daadwerkelijk object staan.
        // Omdat het verwachte object slechts een subset hoeft te zijn van het daadwerkelijke object gebruiken we LENIENT als compare mode.
        JSONAssert.assertEquals(
            "Fout in " + nameOfEntity + " met " + nameOfUniqueKey + " '" + uniqueKey + "':"
                + "\n", expectedJSONObject,
            jsonObject, JSONCompareMode.LENIENT);
      }
    } catch (JSONException e) {
      fail("Er is een fout opgetreden", e.getCause());
    }
  }

  private String readExpectedJSONFromFile(String pathRelativeToSrcTestResources)
      throws IOException {
    InputStream inputStream = GebruikerRestSteps.class.getResourceAsStream(
        pathRelativeToSrcTestResources);
    return readFromInputStream(inputStream);
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
}







