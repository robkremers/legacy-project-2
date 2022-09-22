package nl.bkwi.gebruikers.administratie.api.gebruiker;

import io.reactivex.annotations.NonNull;
import javax.naming.InvalidNameException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikers.administratie.api.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.services.useradmin.ldap.LDAPException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/gebruikers")
@Slf4j
public class GebruikerController {

    public static final String ERROR_MESSAGE_NOT_AUTHORIZED = "Not authorized";
    public static final String ERROR_MESSAGE_INVALID_NAME = "Invalid name";
    private static final String ERROR_MESSAGE_ILLEGAL_ARGUMENT = "Illegal argument";
    private static final String ERROR_MESSAGE_NOT_FOUND = "Not found";

    private GebruikerService gebruikerService;

    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    @GetMapping("/{uid}")
    public Mono<GebruikerDTO> getGebruiker(@PathVariable String uid,
                                           @RequestHeader("am_dn") String dn) {
        try {
            return gebruikerService.getGebruiker(dn, uid);
        } catch (LDAPException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE_NOT_AUTHORIZED, e);
        } catch (InvalidNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE_INVALID_NAME, e);
        }
    }

    @GetMapping(produces = "application/json")
    public Flux<GebruikerDTO> getGebruikers(@RequestHeader("am_dn") String dn) {
        try {
            return gebruikerService.getGebruikers(dn);
        } catch (LDAPException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE_NOT_AUTHORIZED, e);
        } catch (InvalidNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE_INVALID_NAME, e);
        }
    }

    /**
     * This endpoint will be used for a manual update of the password of an existing account.
     *
     * @param uid         id van de gebruiker van wie het wachtwoord veranderd worden moet.
     * @param dn          De system user, die de password-reset uitvoert.
     * @param passwordDTO Bevat het wachtwoord, dat in het frontend manueel gezet is.
     */
    @PutMapping("/{uid}/password")
    public void manualUpdateUserPassword(
            @PathVariable String uid,
            @RequestHeader("am_dn") String dn,
            @Valid @RequestBody PasswordDTO passwordDTO) {
        try {
            gebruikerService.manualUpdateUserPassword(dn, uid, passwordDTO.getPassword());
        } catch (LDAPException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE_NOT_AUTHORIZED, e);
        } catch (InvalidNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE_INVALID_NAME, e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE_ILLEGAL_ARGUMENT, e);
        } catch (GebruikerNietGevondenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MESSAGE_NOT_FOUND, e);
        }
    }

    /**
     * This endpoint will be used for a request to generate a new password for an existing account.
     *
     * @param uid id van de gebruiker van wie het wachtwoord veranderd worden moet.
     * @param dn  De system user, die de password-reset uitvoert.
     * @return Mono<PasswordDTO> Bevat het gegenereerde en gesavede wachtwoord.
     */
    @GetMapping("/{uid}/generated_password")
    public Mono<PasswordDTO> resetUserPassword(
            @PathVariable @NonNull String uid,
            @RequestHeader("am_dn") String dn
    ) {
        try {
            return gebruikerService.resetUserPassword(dn, uid);
        } catch (LDAPException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE_NOT_AUTHORIZED, e);
        } catch (InvalidNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE_INVALID_NAME, e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_MESSAGE_ILLEGAL_ARGUMENT, e);
        } catch (GebruikerNietGevondenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MESSAGE_NOT_FOUND, e);
        }
    }
}
