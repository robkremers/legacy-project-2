package nl.bkwi.gebruikersadministratie;

import static org.junit.jupiter.api.Assertions.fail;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import nl.bkwi.gebruikersadministratie.bdd.GebruikerRestSteps;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import nl.bkwi.gebruikersadministratie.BeheerderConstants;

public class RestServiceMethods {

  public static RequestSpecification inloggen_beheerder(String am_uid, String am_dn) {
    return RestAssured
        .given()
        .baseUri(BeheerderConstants.BASE_URL)
        .header("am_uid", am_uid)
        .header("am_dn", am_dn);

  }

  public static void assertExpectedJsonIsSubsetOfActualJson(String expectedJson, String actualJson,
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

  public static String readExpectedJSONFromFile(String pathRelativeToSrcTestResources)
      throws IOException {
    InputStream inputStream = GebruikerRestSteps.class.getResourceAsStream(
        pathRelativeToSrcTestResources);
    return readFromInputStream(inputStream);
  }

  private static String readFromInputStream(InputStream inputStream)
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

  public static String updateGebruikerPassword(String password, String confirmation) {
    return "{ \"password\": \"" + password + "\",  \"confirmation\": \"" + confirmation + "\"}";

  }
}
