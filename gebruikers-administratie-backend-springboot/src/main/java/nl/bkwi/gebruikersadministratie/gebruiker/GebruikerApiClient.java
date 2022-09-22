package nl.bkwi.gebruikersadministratie.gebruiker;

import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.FouteInvoerException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.NietGeautoriseerdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class used for connectivity with gebruikers-administratie-api (handling with ldap).
 */
@Component
@Slf4j
public class GebruikerApiClient {

    public static final String AM_DN = "am_dn";
    public static final String GEBRUIKER_URI_PATHSEGMENT = "gebruikers";
    public static final String PASSWORD = "password";
    public static final String RESET_PASSWORD = "generated_password";
    private final WebClient webClient;

    public GebruikerApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<GebruikerDTO> getGebruikers(String dn) {

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment(GEBRUIKER_URI_PATHSEGMENT).build())
                .header(AM_DN, dn)
                .retrieve()
                .bodyToFlux(GebruikerDTO.class)
                .doOnError(e -> {
                    log.error("Rest call to API failed", e);
                    throw new IllegalStateException(e.getMessage(), e);
                });
    }

    public Mono<GebruikerDTO> getGebruiker(String dn, String uid) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment(GEBRUIKER_URI_PATHSEGMENT, uid).build())
                .header(AM_DN, dn)
                .retrieve()
                .bodyToMono(GebruikerDTO.class)
                .doOnError(e -> {
                    log.error("Rest call to API failed", e);
                    throw new IllegalStateException("Failed to retrieve user by userId " + uid, e);
                });
    }

    /**
     * Functie: Doorvoer van een manueel aangemaakt nieuw password naar ldap voor een gegeven gebruiker.
     *
     * @param uid         id van de gebruiker van wie het password veranderd worden moet.
     * @param dn          De system user, die de password reset uitvoert.
     * @param passwordDTO Het nieuwe password en de confirmation.
     * @return Een mono response entity met Http status maar zonder body
     */
    public Mono<ResponseEntity<Void>> updateUserPassword(
            String uid,
            String dn,
            PasswordDTO passwordDTO) {

        return webClient
                .put()
                .uri(uriBuilder -> uriBuilder.pathSegment(GEBRUIKER_URI_PATHSEGMENT, uid, PASSWORD).build())
                .header(AM_DN, dn)
                .bodyValue(passwordDTO)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono.error(new GebruikerNietGevondenException()))
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        clientResponse -> Mono.error(new NietGeautoriseerdException()))
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        clientResponse -> Mono.error(new FouteInvoerException()))
                .toBodilessEntity();
    }

    /**
     * Functie: Genereren van een password in ga-api en opgeslagen in ldap, dat vervolgens voor het frontend beschikbaar
     * is, voor een gegeven gebruiker.
     *
     * @param uid id van de gebruiker van wie het password veranderd worden moet.
     * @param dn  De system user, die de password reset uitvoert.
     * @return Mono<PasswordDTO> bevattend het gegenereerde password.
     */
    public Mono<PasswordDTO> resetUserPassword(
            String uid,
            String dn
    ) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment(GEBRUIKER_URI_PATHSEGMENT, uid, RESET_PASSWORD).build())
                .header(AM_DN, dn)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono.error(new GebruikerNietGevondenException()))
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        clientResponse -> Mono.error(new NietGeautoriseerdException()))
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        clientResponse -> Mono.error(new FouteInvoerException()))
                .bodyToMono(PasswordDTO.class)
                ;
    }
}
