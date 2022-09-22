package nl.bkwi.gebruikersadministratie.systemuser;

import java.security.Principal;

public class SystemUser implements Principal {

  private final String userid;
  private final String name;
  private final String dn;


  public SystemUser(String userid, String name, String dn) {
    this.userid = userid;
    this.name = name;
    this.dn = dn;
  }

  /**
   * The identifier used by the authorization system.
   *
   * @return the identifier for this user.
   */
  public String getUserid() {
    return userid;
  }

  /**
   * Obtain the full name of this user. That is his first name and his last name concatenated with a space inbetween.
   *
   * @return the full name of this user.
   */
  @Override
  public String getName() {
    return name;
  }

  public String getDn() {
    return dn;
  }
}
