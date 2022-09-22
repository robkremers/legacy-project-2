package nl.bkwi.gebruikers.administratie.api.gebruiker;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikers.administratie.api.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.services.useradmin.ldap.LDAPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = GebruikerController.class)
@Slf4j
@ActiveProfiles("test")
class GebruikerControllerTest {

  @MockBean
  private GebruikerService gebruikerService;

  @Autowired
  WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testGetUser() throws Exception {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";
    GebruikerDTO gebruikerDTO = GebruikerDTO.builder().userId(uid).achternaam("achternaam").voornaam("voornaam").build();

    // When
    Mockito.when(gebruikerService.getGebruiker(dn, uid)).thenReturn(Mono.just(gebruikerDTO));

    // Then
    webTestClient.get()
        .uri("/gebruikers/" + uid)
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange()
        .expectStatus().isOk()
        .expectBody(GebruikerDTO.class)
        .value(GebruikerDTO::getAchternaam, equalTo("achternaam"))
        .value(GebruikerDTO::getVoornaam, equalTo("voornaam"));
  }

  @Test
  void testGetUserMetOnbekendeGebruikerLevertUnauthorizedOp() throws Exception {
    // Given
    String dn = "cn=onbekend,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";

    // When
    Mockito.when(gebruikerService.getGebruiker(dn, uid)).thenThrow(new LDAPException("Fout"));

    // Then
    webTestClient.get()
        .uri("/gebruikers/" + uid)
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void testGetUserMetInvalideDNLevertBadRequestOp() throws Exception {
    // Given
    String dn = "invalid";
    String uid = "uid";

    // When
    Mockito.when(gebruikerService.getGebruiker(dn, uid))
        .thenThrow(new InvalidNameException("Fout"));

    // Then
    webTestClient.get()
        .uri("/gebruikers/" + uid)
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testGetUsers() throws InvalidNameException, LDAPException {
    // Given

    GebruikerDTO gebruikerDTO1 = GebruikerDTO.builder().userId("uid").achternaam("achternaam")
        .voornaam("voornaam").build();
    GebruikerDTO gebruikerDTO2 = GebruikerDTO.builder().userId("uid").achternaam("achternaam")
        .voornaam("voornaam").build();
    List<GebruikerDTO> list = new ArrayList<>();
    list.add(gebruikerDTO1);
    list.add(gebruikerDTO2);

    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";

    // When
    Mockito.when(gebruikerService.getGebruikers(dn)).thenReturn(Flux.fromIterable(list));

    // Then
    webTestClient.get()
        .uri("/gebruikers/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(GebruikerDTO.class)
        .value(List::size, equalTo(2))
    ;
  }

  @Test
  void testGetUsersOnbekendeGebruikerLevertUnauthorizedOp()
      throws InvalidNameException, LDAPException {
    // Given

    String dn = "cn=onbekend,ou=BKWI,o=suwi,c=nl";

    // When
    Mockito.when(gebruikerService.getGebruikers(dn)).thenThrow(new LDAPException("Fout"));

    // Then
    webTestClient.get()
        .uri("/gebruikers/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange()
        .expectStatus().isUnauthorized()
    ;
  }

  @Test
  void testGetUsersMetInvalidDNLevertBadRequestOp() throws InvalidNameException, LDAPException {
    // Given

    String dn = "invalid";

    // When
    Mockito.when(gebruikerService.getGebruikers(dn)).thenThrow(new InvalidNameException("Fout"));

    // Then
    webTestClient.get()
        .uri("/gebruikers/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange()
        .expectStatus().isBadRequest()
    ;
  }

  @Test
  void testUpdateUserPasswordMetCorrecteParametersLevertOkOp()
      throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given

    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "u1";
    String newPassword = "eennieuwwachtwoord";
    PasswordDTO passwordDTO = new PasswordDTO().password(newPassword);

    // When

    ResponseSpec responseSpec = webTestClient.put()
        .uri("/gebruikers/" + uid + "/password/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .bodyValue(passwordDTO)
        .exchange();

    // Then
    responseSpec.expectStatus().isOk();
    verify(gebruikerService, times(1)).manualUpdateUserPassword(dn, uid, newPassword);
  }

  @Test
  void testResetUserPassword() throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";
    PasswordDTO passwordDTO = new PasswordDTO("Password");

    // When
    Mockito.when(gebruikerService.resetUserPassword(dn, uid)).thenReturn(Mono.just(passwordDTO));

    String newPassword = webTestClient.get()
            .uri("/gebruikers/" + uid + "/generated_password")
            .accept(MediaType.APPLICATION_JSON)
            .header("am_dn", dn)
            .exchange()
            .expectStatus().isOk()
            .expectBody(PasswordDTO.class)
            .returnResult()
            .getResponseBody()
            .getPassword()
            ;

    assertNotNull(newPassword);
    assertEquals(newPassword, passwordDTO.getPassword());
    verify(gebruikerService, times(1)).resetUserPassword(dn, uid);
  }

  @Test
  void testUpdateUserPasswordMetInvalidDNLevertBadRequestOp()
      throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "!!invalid";
    String uid = "u1";
    String newPassword = "nieuwwachtwoord";
    PasswordDTO passwordDTO = new PasswordDTO().password(newPassword);

    // When
    Mockito.doThrow(new InvalidNameException("fout")).when(gebruikerService)
        .manualUpdateUserPassword(anyString(), anyString(), anyString());

    ResponseSpec responseSpec = webTestClient.put()
        .uri("/gebruikers/" + uid + "/password/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .bodyValue(passwordDTO)
        .exchange();

    // Then
    responseSpec.expectStatus().isBadRequest();
  }

  @Test
  void testResetUserPasswordMetInvalidDNLevertBadRequestOp() throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "cn=onbekend,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";

    // When
    Mockito.when(gebruikerService.resetUserPassword(anyString(), anyString())).thenThrow(new InvalidNameException("fout"));

    // Then
    webTestClient.get()
            .uri("/gebruikers/" + uid + "/generated_password")
            .accept(MediaType.APPLICATION_JSON)
            .header("am_dn", dn)
            .exchange()
            .expectStatus().isBadRequest();
    ;
  }

  @Test
  void testUpdateUserPasswordDoorOnbekendeAdminLevertUnauthorizedOp() throws Exception {
    // Given
    String dn = "cn=onbekend,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";
    String newPassword = "nieuwwachtwoord";
    PasswordDTO passwordDTO = new PasswordDTO().password(newPassword);

    // When
    Mockito.doThrow(new LDAPException("fout")).when(gebruikerService)
        .manualUpdateUserPassword(anyString(), anyString(), anyString());

    ResponseSpec responseSpec = webTestClient.put()
        .uri("/gebruikers/" + uid + "/password/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .bodyValue(passwordDTO)
        .exchange();

    // Then
    responseSpec.expectStatus().isUnauthorized();
  }

  @Test
  void testResetUserPasswordDoorOnbekendeAdminLevertUnauthorizedOp() throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "cn=onbekend,ou=BKWI,o=suwi,c=nl";
    String uid = "uid";

    // When
    Mockito.when(gebruikerService.resetUserPassword(dn, uid)).thenThrow(new LDAPException("Fout"));

    // Then
    webTestClient.get()
            .uri("/gebruikers/" + uid + "/generated_password")
            .accept(MediaType.APPLICATION_JSON)
            .header("am_dn", dn)
            .exchange()
            .expectStatus().isUnauthorized()
    ;
  }

  @Test
  void testUpdateUserPasswordMetOnbekendeGebruikerLevertNotFoundOp()
      throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given

    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "onbekendegebruiker";
    String newPassword = "eennieuwwachtwoord";

    PasswordDTO passwordDTO = new PasswordDTO().password(newPassword);

    // When
    Mockito.doThrow(new GebruikerNietGevondenException("not found")).when(gebruikerService)
        .manualUpdateUserPassword(dn, uid, newPassword);

    ResponseSpec responseSpec = webTestClient.put()
        .uri("/gebruikers/" + uid + "/password/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .bodyValue(passwordDTO)
        .exchange();

    // Then
    responseSpec.expectStatus().isNotFound();
  }

  @Test
  void testResetUserPasswordmetOnbekendeGebruikerLevertNotFoundOp() throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "onbekendegebruiker";

    // When
    Mockito.when(gebruikerService.resetUserPassword(dn, uid)).thenThrow(new GebruikerNietGevondenException("not found"));

    // Then
    webTestClient.get()
            .uri("/gebruikers/" + uid + "/generated_password")
            .accept(MediaType.APPLICATION_JSON)
            .header("am_dn", dn)
            .exchange()
            .expectStatus().isNotFound()
    ;
  }

    @Test
  void testUpdateUserPasswordZonderParametersLevertBadRequestOp() throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given

    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "u1";

    // When
    Mockito.when(gebruikerService.resetUserPassword(dn, uid)).thenThrow(new GebruikerNietGevondenException("not found"));

      // Then
    ResponseSpec responseSpec = webTestClient.put()
        .uri("/gebruikers/" + uid + "/password/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .exchange();

    // Then
    responseSpec.expectStatus().isBadRequest();
  }

  @Test
  void testResetUserPasswordZonderParametersLevertBadRequestOp() throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "onbekendegebruiker";

    // When
    Mockito.when(gebruikerService.resetUserPassword(dn, uid)).thenThrow(new IllegalArgumentException("Illegal argument"));

    // Then
    webTestClient.get()
            .uri("/gebruikers/" + uid + "/generated_password")
            .accept(MediaType.APPLICATION_JSON)
            .header("am_dn", dn)
            .exchange()
            .expectStatus().isBadRequest()
    ;
  }

  @Test
  void testUpdateUserPasswordMetLeegWachtwoordLevertBadRequestOp()
      throws InvalidNameException, LDAPException, GebruikerNietGevondenException {
    // Given
    String dn = "cn=rkremers,ou=BKWI,o=suwi,c=nl";
    String uid = "u1";
    String newPassword = "";
    PasswordDTO passwordDTO = new PasswordDTO().password(newPassword);

    // When
    Mockito.doThrow(new IllegalArgumentException()).when(gebruikerService)
        .manualUpdateUserPassword(dn, uid, newPassword);

    ResponseSpec responseSpec = webTestClient.put()
        .uri("/gebruikers/" + uid + "/password/")
        .accept(MediaType.APPLICATION_JSON)
        .header("am_dn", dn)
        .bodyValue(passwordDTO)
        .exchange();

    // Then
    responseSpec.expectStatus().isBadRequest();
  }
}
