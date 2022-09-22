package nl.bkwi.gebruikersadministratie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class AppConfig {


  @Bean
  public WebClient webClient(@Value("${gebruikers-administratie-api.uri}") String uri) {
    return WebClient.builder()
      .baseUrl(uri)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }

  @Bean
  public OpenAPI customOpenApi() {

    Info info = new Info()
            .title("Gebruikersadministratie backend")
            .version("001")
            .description("In de backend wordt het Angular frontend ondersteund.");

    return new OpenAPI()
            .components(new Components())
            .info(info)
            ;
  }

}
