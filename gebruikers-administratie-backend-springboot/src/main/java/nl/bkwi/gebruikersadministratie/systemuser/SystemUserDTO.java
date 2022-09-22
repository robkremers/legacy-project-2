package nl.bkwi.gebruikersadministratie.systemuser;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class SystemUserDTO {

  private String naam;

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  @Override
  public String toString() {
    return naam;
  }
}
