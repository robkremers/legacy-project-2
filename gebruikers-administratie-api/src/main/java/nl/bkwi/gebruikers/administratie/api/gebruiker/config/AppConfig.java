package nl.bkwi.gebruikers.administratie.api.gebruiker.config;

import lombok.Setter;
import nl.bkwi.services.useradmin.dao.OpenDJUserAdminDao;
import nl.bkwi.services.useradmin.ldap.AuthenticatedConnectionPool;
import nl.bkwi.services.useradmin.security.policy.openam.OpenAmUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
public class AppConfig {

  @Value("${spring.ldap.hosts}")
  private String hosts;

  @Value("${spring.ldap.port}")
  private Integer port;

  @Value("${spring.ldap.username}")
  private String username;

  @Value("${spring.ldap.password}")
  private String password;

  @Value("${spring.ldap.connectionPoolSize}")
  private String connectionPoolSize;

  @Bean
  public AuthenticatedConnectionPool ldapConnectionPool() {
    AuthenticatedConnectionPool acp = new AuthenticatedConnectionPool();
    acp.setHosts(hosts);
    acp.setPort(port);
    acp.setPoolSize(Integer.parseInt(connectionPoolSize));
    acp.setUsername(username);
    acp.setPassword(password);
    acp.initialize();
    return acp;
  }

  @Bean
  public OpenDJUserAdminDao userAdminDao() {
    OpenAmUtil.setVersionGlobal("6.5");
    OpenDJUserAdminDao dao = new OpenDJUserAdminDao();
    dao.setConnectionPool(ldapConnectionPool());
    dao.setUserControler(null);
    return dao;
  }

}
