package nl.bkwi.gebruikers.administratie.api.gebruiker.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains configuration for the generation of Swagger API documentation.
 */
@Configuration
@Slf4j
//@ImportResource("classpath:spring/soap-and-dao.xml")
//@ImportResource("classpath:spring/roleadmin.xml")
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi() {

    Info info = new Info()
      .title("Gebruikersadministratie API")
      .version("002")
      .description("In de API wordt de koppeling met ldap/opendj/openam gelegd om gebruikers te beheren.");

    return new OpenAPI()
      .components(new Components())
      .info(info)
      ;
  }
}
