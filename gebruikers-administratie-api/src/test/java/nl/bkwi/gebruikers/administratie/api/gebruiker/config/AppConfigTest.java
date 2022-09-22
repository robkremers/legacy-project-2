package nl.bkwi.gebruikers.administratie.api.gebruiker.config;

import static org.junit.jupiter.api.Assertions.*;

import nl.bkwi.services.useradmin.dao.OpenDJUserAdminDao;
import org.junit.jupiter.api.Test;

class AppConfigTest {

  @Test
  void createLdapConnectionPool() {
    var config = new AppConfig();
    config.setHosts("");
    config.setPort(1);
    config.setUsername("cn=dummy");
    config.setConnectionPoolSize("1");
    config.ldapConnectionPool();
    OpenDJUserAdminDao dao = config.userAdminDao();
    assertNotNull(dao);
  }

}
