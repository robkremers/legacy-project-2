package nl.bkwi.gebruikersadministratie.gebruiker;


import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;

import java.nio.charset.StandardCharsets;

import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.gebruikersadministratie.exceptions.GebruikersAdministratieException;
import nl.bkwi.gebruikersadministratie.gebruiker.GebruikerController;
import nl.bkwi.gebruikersadministratie.gebruiker.GebruikerService;
import nl.bkwi.gebruikersadministratie.gebruiker.UiGebruikerDTO;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.FouteInvoerException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.NietGeautoriseerdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GebruikerController.class)
class GebruikerRestControllerTest {

    public static final String BKWI_BEHEERDER_DN = "cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl";
    public static final String BASE_URI = "/backend/gebruikers";

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private GebruikerService gebruikerService;

    @Test
    void testRetrieveGebruikers() {
        String beheerder = BKWI_BEHEERDER_DN;

        Mockito.when(gebruikerService.getGebruikers(Mockito.any()))
                .thenReturn(getGebruikers());

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header("am_uid", "uid")
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UiGebruikerDTO[].class)
                .value(x -> x[0].getAchternaam(), equalTo("Treintje"))
                .value(x -> x[1].getAchternaam(), equalTo("Busje"));

    }

    @Test
    void testRetrieveGebruikersZonderHeadersIsUnauthorized() {

        Mockito.when(gebruikerService.getGebruikers(Mockito.any()))
                .thenReturn(getGebruikers());

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testRetrieveGebruikersMetIncompleteHeadersIsUnauthorized() {
        String beheerder = BKWI_BEHEERDER_DN;

        Mockito.when(gebruikerService.getGebruikers(Mockito.any()))
                .thenReturn(getGebruikers());

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testRetrieveGebruikersMetIncorrecteHeaderInhoudIsUnauthorized() {
        String beheerderZonderCn = "ou=BKWI,o=suwi,c=nl";

        Mockito.when(gebruikerService.getGebruikers(Mockito.any()))
                .thenReturn(getGebruikers());

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header("am_uid", "uid")
                .header("am_dn", beheerderZonderCn)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testExportGebruikers() {
        String beheerder = BKWI_BEHEERDER_DN;

        Mockito.when(gebruikerService.exportGebruikers(Mockito.any()))
                .thenReturn(getGebruikersExportMock());

        Flux<InputStreamResource> inputStreamResourceFlux = webTestClient.get()
                .uri(BASE_URI + "/export")
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .header("am_uid", "uid")
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isOk()
                .returnResult(InputStreamResource.class).getResponseBody()
                .log();

        StepVerifier.create(inputStreamResourceFlux)
                .expectComplete()
                .verify();
    }

    @Test
    void testRetrieveGebruiker() {
        String beheerder = BKWI_BEHEERDER_DN;
        String uid = "T. Treintje";

        Mockito.when(gebruikerService.getGebruiker(Mockito.any(), eq(uid)))
                .thenReturn(Mono.just(getMockGebruiker1()));

        webTestClient.get()
                .uri(BASE_URI + "/" + uid)
                .accept(MediaType.APPLICATION_JSON)
                .header("am_uid", "uid")
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UiGebruikerDTO.class)
                .value(UiGebruikerDTO::getAchternaam, equalTo("Treintje"));
    }

    @Test
    void testRetrieveGebruikerZonderHeadersIsUnauthorized() {
        String uid = "T. Treintje";

        Mockito.when(gebruikerService.getGebruiker(Mockito.any(), eq(uid)))
                .thenReturn(Mono.just(getMockGebruiker1()));

        webTestClient.get()
                .uri(BASE_URI + "/" + uid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testRetrieveGebruikerMetIncompleteHeadersIsUnauthorized() {
        String beheerder = BKWI_BEHEERDER_DN;
        String uid = "T. Treintje";

        Mockito.when(gebruikerService.getGebruiker(Mockito.any(), eq(uid)))
                .thenReturn(Mono.just(getMockGebruiker1()));

        webTestClient.get()
                .uri(BASE_URI + "/" + uid)
                .accept(MediaType.APPLICATION_JSON)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testRetrieveGebruikerMetIncorrecteHeaderInhoudIsUnauthorized() {
        String beheerderZonderCn = "ou=BKWI,o=suwi,c=nl";
        String uid = "T. Treintje";

        Mockito.when(gebruikerService.getGebruiker(Mockito.any(), eq(uid)))
                .thenReturn(Mono.just(getMockGebruiker1()));

        webTestClient.get()
                .uri(BASE_URI + "/" + uid)
                .accept(MediaType.APPLICATION_JSON)
                .header("am_uid", "uid")
                .header("am_dn", beheerderZonderCn)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testUpdateUserPasswordOk() throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class)))
                .thenReturn(Mono.empty());

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", "uid")
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    void testResetUserPasswordOk() throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(gebruikerService.resetPassword(eq(uid), Mockito.any())).thenReturn(Mono.just(passwordDTO));

        // Now make the rest call
        webTestClient.get()
                .uri(BASE_URI + "/" + uid + "/generated_password")
                .header("am_uid", "uid")
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    void testUpdateUserPasswordError() throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class))).thenReturn(Mono.error(new GebruikersAdministratieException("Dat ging even fout!")));

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();

    }

    // Deze test heb ik toegevoegd om een andere exception constructor te testen, namelijk de constructor met een
    // message en een cause. In onze applicatie wordt deze constructor nog niet gebruikt, maar om Sonar tevreden te
    // houden test ik hem  hier wel.
    @Test
    void testUpdateUserPasswordError2() throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito
                .when(gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class)))
                .thenReturn(Mono.error(new GebruikersAdministratieException("Dat ging even fout!", new NullPointerException())));

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();

    }

    @Test
    void testUpdateUserPasswordException() throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class))).thenThrow(new GebruikersAdministratieException("Dat ging even fout!", new NullPointerException()));

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();

    }

    @Test
    void testUpdateUserPasswordGebruikerNietGevondenException()
            throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(
                        gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class)))
                .thenReturn(Mono.error(new GebruikerNietGevondenException()));

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testResetUserPasswordGebruikerNietGevondenException()
            throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        Mockito.when(
                        gebruikerService.resetPassword(eq(uid), Mockito.any()))
                .thenReturn(Mono.error(new GebruikerNietGevondenException())
                );

        // Now make the rest call
        webTestClient.get()
                .uri(BASE_URI + "/" + uid + "/generated_password")
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateUserPasswordFouteInvoerException()
            throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(
                        gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class)))
                .thenReturn(Mono.error(new FouteInvoerException()));

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testResetUserPasswordFouteInvoerException()
            throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        Mockito.when(
                        gebruikerService.resetPassword(eq(uid), Mockito.any()))
                .thenReturn(Mono.error(new FouteInvoerException())
                );

        // Now make the rest call
        webTestClient.get()
                .uri(BASE_URI + "/" + uid + "/generated_password")
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateUserPasswordNietGeauthoriseerdException()
            throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        PasswordDTO passwordDTO = new PasswordDTO("new");
        Mockito.when(
                        gebruikerService.updateUserPassword(eq(uid), Mockito.any(), Mockito.any(PasswordDTO.class)))
                .thenReturn(Mono.error(new NietGeautoriseerdException()));

        // Now make the rest call
        webTestClient.put()
                .uri(BASE_URI + "/" + uid + "/password")
                .bodyValue(passwordDTO)
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testResetUserPasswordNietGeauthoriseerdException()
            throws GebruikersAdministratieException {
        String beheerder = BKWI_BEHEERDER_DN;
        String beheerderUid = "beheerder";
        String uid = "someuser";

        Mockito.when(
                        gebruikerService.resetPassword(eq(uid), Mockito.any()))
                .thenReturn(Mono.error(new NietGeautoriseerdException())
                );

        // Now make the rest call
        webTestClient.get()
                .uri(BASE_URI + "/" + uid + "/generated_password")
                .header("am_uid", beheerderUid)
                .header("am_dn", beheerder)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    private Flux<UiGebruikerDTO> getGebruikers() {
        var uiGebruikerDto1 = getMockGebruiker1();
        var uiGebruikerDto2 = getMockGebruiker2();

        var arr = new UiGebruikerDTO[2];
        arr[0] = uiGebruikerDto1;
        arr[1] = uiGebruikerDto2;

        return Flux.fromArray(arr);
    }

    private UiGebruikerDTO getMockGebruiker1() {
        var uiGebruikerDTO = UiGebruikerDTO.builder()
                .userId("T. Treintje")
                .achternaam("Treintje")
                .voornaam("Truus")
                .email("ttreintje@haarlem.nl")
                .afdeling("Bezwaar en beroep")
                .employeeNr("Employee1")
                .build();
        return uiGebruikerDTO;
    }

    private UiGebruikerDTO getMockGebruiker2() {
        var uiGebruikerDTO = UiGebruikerDTO.builder()
                .userId("B. Busje")
                .achternaam("Busje")
                .voornaam("Ben")
                .email("bbus@haarlem.nl")
                .afdeling("UWVWb")
                .employeeNr("Employee2")
                .build();
        return uiGebruikerDTO;
    }

    private Flux<DataBuffer> getGebruikersExportMock() {
        return Flux.just(makeDataBuffer("line1\n"), makeDataBuffer("line2\n"));
    }

    private Flux<DataBuffer> getGebruikersExportMockMetFout() {
        return Flux.just(makeDataBuffer("line1"))
                .concatWith(Flux.error(new RuntimeException("intentional error")));
    }

    private DataBuffer makeDataBuffer(String text) {
        DataBuffer dataBuffer = null;
        boolean release = false;
        try {
            DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
            dataBuffer = bufferFactory.allocateBuffer();
            release = true;
            dataBuffer.write(text, StandardCharsets.UTF_8);
            release = false;
            return dataBuffer;
        } finally {
            if (release) {
                DataBufferUtils.release(dataBuffer);
            }
        }
    }
}
