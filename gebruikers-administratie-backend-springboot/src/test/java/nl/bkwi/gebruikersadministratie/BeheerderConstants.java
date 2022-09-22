package nl.bkwi.gebruikersadministratie;

public class BeheerderConstants {

  public static final String DUO_BEHEERDER_DN = "cn=DUO Beheerder,ou=DUO,o=suwi,c=nl";
  public static final String BKWI_BEHEERDER_DN = "cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl";
  public static final int HTTP_STATUS_CODE_OK = 200;
  public static final int HTTP_STATUS_CODE_Unauthorized = 401;
  public static final String BASE_URL = "http://localhost:2020";
  public static final String PATH_URL = "/backend/gebruikers";
  public static final String PATH_URL_UPDATEPASSWORD = "/backend/gebruikers/updatePassword/";
}
