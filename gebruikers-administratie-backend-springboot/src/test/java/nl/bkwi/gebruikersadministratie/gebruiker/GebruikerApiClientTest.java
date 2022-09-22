package nl.bkwi.gebruikersadministratie.gebruiker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import java.util.function.Function;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GebruikerApiClientTest {

  @Captor
  private ArgumentCaptor<Consumer<Throwable>> captor;

  public static final String DUO_BEHEERDER_DN = "cn=DUO Beheerder,ou=DUO,o=suwi,c=nl";

  @Mock
  private WebClient webClientMock;

  @SuppressWarnings("rawtypes")
  @Mock
  private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @Mock
    private WebClient.RequestBodySpec requestBodySpecMock;


  @InjectMocks
    GebruikerApiClient gebruikerApiClient;

    @Test
    void getGebruikers() {

        GebruikerDTO gebruikerDTO = new GebruikerDTO();
        gebruikerDTO.setVoornaam("Anton");
        gebruikerDTO.setAchternaam("Bakker");

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(
                requestHeadersSpecMock);
        when(requestHeadersSpecMock.header(Mockito.anyString(), Mockito.anyString())).thenReturn(
                requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(
                ArgumentMatchers.<Class<GebruikerDTO>>notNull())).thenReturn(Flux.just(gebruikerDTO));

        Flux<GebruikerDTO> gebruikerFlux = gebruikerApiClient.getGebruikers(
                DUO_BEHEERDER_DN);
        assertNotNull(gebruikerFlux);
        StepVerifier.create(gebruikerFlux)
                .expectNext(gebruikerDTO)
                .verifyComplete();
    }

    @Test
    void getGebruiker() {

        GebruikerDTO gebruikerDTO = new GebruikerDTO();
        String userId = "userId";
        gebruikerDTO.setUserId(userId);
        gebruikerDTO.setVoornaam("Anton");
        gebruikerDTO.setAchternaam("Bakker");
        gebruikerDTO.setAfdeling("Afdeling");
        gebruikerDTO.setEmail("email@email.org");
        gebruikerDTO.setInitialen("A");
        gebruikerDTO.setTelefoonnummer("0612345678");
        gebruikerDTO.setEmployeeNr("employee1");

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(
                requestHeadersSpecMock);
        when(requestHeadersSpecMock.header(Mockito.anyString(), Mockito.anyString())).thenReturn(
                requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<GebruikerDTO>>notNull())).thenReturn(Mono.just(gebruikerDTO));

        Mono<GebruikerDTO> gebruikerDTOMono = gebruikerApiClient.getGebruiker(
                DUO_BEHEERDER_DN, userId);
        assertNotNull(gebruikerDTOMono);
        StepVerifier.create(gebruikerDTOMono)
                .expectNext(gebruikerDTO)
                .verifyComplete();
    }

    @Test
    void getGebruikerMetBuilderDTO() {

        String userId = "userId";
        GebruikerDTO gebruikerDTO = new GebruikerDTO()
                .userId(userId)
                .voornaam("Anton")
                .achternaam("Bakker")
                .afdeling("Afdeling")
                .email("email@email.org")
                .initialen("A")
                .telefoonnummer("0612345678")
                .employeeNr("employee1");

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.header(Mockito.anyString(), Mockito.anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<GebruikerDTO>>notNull())).thenReturn(Mono.just(gebruikerDTO));

        Mono<GebruikerDTO> gebruikerDTOMono = gebruikerApiClient.getGebruiker(DUO_BEHEERDER_DN, userId);
        assertNotNull(gebruikerDTOMono);
        StepVerifier.create(gebruikerDTOMono)
                .expectNext(gebruikerDTO)
                .verifyComplete();
    }

    @Test
    void getGebruikerNotFound() {
      when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
      when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
      when(requestHeadersSpecMock.header(Mockito.anyString(), Mockito.anyString())).thenReturn(requestHeadersSpecMock);
      when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
      when(responseSpecMock.bodyToMono(GebruikerDTO.class)).thenReturn(Mono.error(Exception::new));

      Mono<GebruikerDTO> gebruikerDTOMono = gebruikerApiClient.getGebruiker(DUO_BEHEERDER_DN, "uid");

      StepVerifier.create(gebruikerDTOMono)
        .expectNext()
        .expectError(IllegalStateException.class)
        .verify();
    }


  @Test
    void testUpdateUserPassword() {
        String uid = "uid";
        PasswordDTO passwordDTO = new PasswordDTO().password("password");
        ResponseEntity<Void> responseEntityMock = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<Void> expectedResponseEntity = new ResponseEntity<>(HttpStatus.OK);

        whenPutRequestThenReturnMockResponseEntity(responseEntityMock);

        Mono<ResponseEntity<Void>> responseEntityMono = gebruikerApiClient.updateUserPassword(uid, DUO_BEHEERDER_DN, passwordDTO);

        StepVerifier.create(responseEntityMono).expectNext(expectedResponseEntity).verifyComplete();
    }

    @Test
    void testResetUserPassword() {

        String uid = "uid";
        PasswordDTO passwordDTO = new PasswordDTO("password");

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.header(Mockito.anyString(), Mockito.anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<PasswordDTO>>notNull())).thenReturn(Mono.just(passwordDTO));
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);

        Mono<PasswordDTO> passwordDTOMono = gebruikerApiClient.resetUserPassword(uid, DUO_BEHEERDER_DN);
        assertNotNull(passwordDTOMono);

        StepVerifier.create(passwordDTOMono)
                .expectNext(passwordDTO)
                .verifyComplete();
    }

    @Test
    void updateUserPasswordGebruikerNietGevonden() {
        String uid = "uid";
        PasswordDTO passwordDTO = new PasswordDTO().password("password");

        ResponseEntity<Void> responseEntityMock = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity<Void> expectedResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        whenPutRequestThenReturnMockResponseEntity(responseEntityMock);

        Mono<ResponseEntity<Void>> responseEntityMono = gebruikerApiClient.updateUserPassword(
                DUO_BEHEERDER_DN, uid, passwordDTO);

        StepVerifier.create(responseEntityMono).expectNext(expectedResponseEntity).verifyComplete();
    }

    private void whenPutRequestThenReturnMockResponseEntity(ResponseEntity<Void> mockResponseEntity) {
        when(webClientMock.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(any(Function.class))).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(Mockito.anyString(), Mockito.anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
        when(responseSpecMock.toBodilessEntity()).thenReturn(Mono.just(mockResponseEntity));
    }
}
