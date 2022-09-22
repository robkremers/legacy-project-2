package nl.bkwi.gebruikers.administratie.api.gebruiker;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LdapDirectory {

  private String node;

}
