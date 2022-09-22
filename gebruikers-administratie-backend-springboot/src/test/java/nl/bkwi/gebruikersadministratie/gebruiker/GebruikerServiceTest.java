package nl.bkwi.gebruikersadministratie.gebruiker;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Optional;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.gebruikersadministratie.exceptions.GebruikersAdministratieException;
import nl.bkwi.gebruikersadministratie.gebruiker.support.TestUtils;
import nl.bkwi.gebruikersadministratie.systemuser.SystemUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GebruikerServiceTest {
  public static final String BKWI_BEHEERDER_DN = "cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl";

  @InjectMocks
  private GebruikerService gebruikerService;

  @Mock
  private GebruikerApiClient gebruikerApiClient;

  @Test
  void getGebruikersTest() {

    String beheerder = "cn=DUO Beheerder,ou=DUO,o=suwi,c=nl";

    GebruikerDTO[] mockGebruikers = getMockGebruikers();
    UiGebruikerDTO[] mockUiGebruikers = getUiMockGebruikers();

    SystemUser systemUser = new SystemUser("beheerder", "beheerder", beheerder);
    Mockito.when(gebruikerApiClient.getGebruikers(beheerder))
        .thenReturn(Flux.fromArray(mockGebruikers));

    Flux<UiGebruikerDTO> gebruikerFlux = gebruikerService.getGebruikers(Optional.of(systemUser));

    assertNotNull(gebruikerFlux);
    StepVerifier.create(gebruikerFlux).expectNext(mockUiGebruikers[0])
        .expectNext(mockUiGebruikers[1])
        .expectComplete().verify();
  }

  @Test
  void getGebruikerTest() {

    String beheerder = "cn=DUO Beheerder,ou=DUO,o=suwi,c=nl";

    GebruikerDTO[] mockGebruikers = getMockGebruikers();
    UiGebruikerDTO[] mockUiGebruikers = getUiMockGebruikers();

    GebruikerDTO mockGebruiker = mockGebruikers[0];
    UiGebruikerDTO mockUiGebruiker = mockUiGebruikers[0];
    String userId = mockGebruiker.getUserId();

    SystemUser systemUser = new SystemUser("beheerder", "beheerder", beheerder);
    Mockito.when(gebruikerApiClient.getGebruiker(beheerder, userId))
        .thenReturn(Mono.just(mockGebruiker));

    Mono<UiGebruikerDTO> uiGebruikerDTOMono = gebruikerService.getGebruiker(Optional.of(systemUser),
        userId);

    assertNotNull(uiGebruikerDTOMono);
    StepVerifier.create(uiGebruikerDTOMono).expectNext(mockUiGebruiker)
        .expectComplete().verify();
  }

  @Test
  void testExportGebruikers() throws IOException {
    String beheerder = BKWI_BEHEERDER_DN;

    GebruikerDTO[] mockGebruikers = getMockGebruikers();

    SystemUser systemUser = new SystemUser("beheerder", "beheerder", beheerder);
    Mockito.when(gebruikerApiClient.getGebruikers(beheerder))
        .thenReturn(Flux.fromArray(mockGebruikers));

    Flux<DataBuffer> actual = gebruikerService.exportGebruikers(Optional.of(systemUser));

    assertNotNull(actual);

    StepVerifier.create(actual)
        .consumeNextWith(TestUtils.assertDataBufferEquals(gebruikerService.csvHeader()))
        .consumeNextWith(TestUtils.assertDataBufferEquals(
            "\"Treintje\";\"Truus\";\"ttreintje@haarlem.nl\";\"Bezwaar en beroep\";;\"T. Treintje\"\n"))
        .consumeNextWith(TestUtils.assertDataBufferEquals(
            "\"Busje\";\"Ben\";\"bbus@haarlem.nl\";\"UWVWb\";;\"B. Busje\"\n"))
        .expectComplete()
        .verify();
  }

  @Test
  void testFoutBijExportGebruikers() throws IOException {
    String beheerder = BKWI_BEHEERDER_DN;

    SystemUser systemUser = new SystemUser("beheerder", "beheerder", beheerder);
    Mockito.when(gebruikerApiClient.getGebruikers(beheerder))
        .thenReturn(Flux.error(new RuntimeException("Intentional error")));

    Flux<DataBuffer> actual = gebruikerService.exportGebruikers(Optional.of(systemUser));

    assertNotNull(actual);

    StepVerifier.create(actual)
        .consumeNextWith(TestUtils.assertDataBufferEquals(gebruikerService.csvHeader()))
        .expectError()
        .verify();
  }
  @Test
  void testUpdateUserPassword() throws GebruikersAdministratieException {
    String beheerder = "cn=DUO Beheerder,ou=DUO,o=suwi,c=nl";

    GebruikerDTO[] mockGebruikers = getMockGebruikers();

    GebruikerDTO mockGebruiker = mockGebruikers[0];
    String userId = mockGebruiker.getUserId();

    SystemUser systemUser = new SystemUser("beheerder", "beheerder", beheerder);
    PasswordDTO passwordDTO = new PasswordDTO("password");

    Mono<ResponseEntity<Void>> voidReturn  = Mono.empty();
    Mockito.when(gebruikerApiClient.updateUserPassword("beheerder", beheerder, passwordDTO))
            .thenReturn(voidReturn); // <ResponseEntity<Void>>just(ResponseEntity.ok()

    Mono<ResponseEntity<Void>> mono = gebruikerService.updateUserPassword("beheerder", Optional.of(systemUser), passwordDTO);

    assertNotNull(mono);

  }

  @Test
  void testResetPassword() throws GebruikersAdministratieException {
    String beheerder = "cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl";
    SystemUser systemUser = new SystemUser("beheerder", "beheerder", beheerder);

    PasswordDTO passwordDTO = new PasswordDTO("password");

    Mockito.when(gebruikerApiClient.resetUserPassword("beheerder", beheerder))
            .thenReturn(Mono.just(passwordDTO));

    Mono<PasswordDTO> passwordDTOMono = gebruikerService.resetPassword("beheerder", Optional.of(systemUser));
    assertNotNull(passwordDTOMono);
  }

  private GebruikerDTO[] getMockGebruikers() {
    var geb = GebruikerDTO.builder()
        .userId("T. Treintje")
        .achternaam("Treintje")
        .voornaam("Truus")
        .email("ttreintje@haarlem.nl")
        .afdeling("Bezwaar en beroep")
        .employeeNr("Employee1")
        .build();

    var geb1 = GebruikerDTO.builder()
        .userId("B. Busje")
        .achternaam("Busje")
        .voornaam("Ben")
        .email("bbus@haarlem.nl")
        .afdeling("UWVWb")
        .employeeNr("Employee2")
        .build();

    var arr = new GebruikerDTO[2];
    arr[0] = geb;
    arr[1] = geb1;
    return arr;
  }

  private UiGebruikerDTO[] getUiMockGebruikers() {
    var geb = UiGebruikerDTO.builder()
        .userId("T. Treintje")
        .achternaam("Treintje")
        .voornaam("Truus")
        .email("ttreintje@haarlem.nl")
        .afdeling("Bezwaar en beroep")
        .employeeNr("Employee1")
        .build();

    var geb1 = UiGebruikerDTO.builder()
        .userId("B. Busje")
        .achternaam("Busje")
        .voornaam("Ben")
        .email("bbus@haarlem.nl")
        .afdeling("UWVWb")
        .employeeNr("Employee2")
        .build();

    var arr = new UiGebruikerDTO[2];
    arr[0] = geb;
    arr[1] = geb1;
    return arr;
  }
}
