package nl.bkwi.gebruikersadministratie.gebruiker;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UiGebruikerDTO {

  private String naam;

  private String inlognaam;

  private String achternaam;

  private String voornaam;

  @EqualsAndHashCode.Include
  private String userId;

  private String initialen;

  private String email;

  private String telefoonnummer;

  private String afdeling;

  private String employeeNr;
}
