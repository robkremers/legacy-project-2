package nl.bkwi.gebruikers.administratie.api.gebruiker;

import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.services.useradmin.domain.LdapUser;

public class LdapUserToGebruikerDTOMapper {

  private LdapUserToGebruikerDTOMapper() {
    // prevent instantiation
  }

  public static GebruikerDTO mapLdapUser2GebruikerDTO(LdapUser ldapUser) {
    return GebruikerDTO.builder()
        .naam(ldapUser.getCommonName())
        .achternaam(ldapUser.getLastName())
        .voornaam(ldapUser.getFirstName())
        .userId(ldapUser.getUserId())
        .initialen(ldapUser.getInitials())
        .email(ldapUser.getEmail())
        .telefoonnummer(ldapUser.getPhoneNr())
        .employeeNr(ldapUser.getEmployeeNr())
        .afdeling("afdeling").build();
  }
}
