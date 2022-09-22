package nl.bkwi.gebruikersadministratie.gebruiker;

import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.gebruikersadministratie.exceptions.GebruikersAdministratieException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.FouteInvoerException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.GebruikerNietGevondenException;
import nl.bkwi.gebruikersadministratie.gebruiker.exceptions.NietGeautoriseerdException;
import nl.bkwi.gebruikersadministratie.systemuser.SystemUser;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/backend/gebruikers")
@Slf4j
public class GebruikerController {

    private final GebruikerService gebruikerService;

    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    @GetMapping()
    public Flux<UiGebruikerDTO> retrieveGebruikers(
            @RequestAttribute("principal") Optional<SystemUser> systemUser) {
        return gebruikerService.getGebruikers(systemUser);
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<InputStreamResource>> exportGebruikers(
            @RequestAttribute("principal") Optional<SystemUser> systemUser) throws IOException {

        Flux<DataBuffer> gebruikersCSV = gebruikerService.exportGebruikers(systemUser);

        // We have issues with setting the name of the downloaded file and handling errors. We chose to transform
        // the flux into a Mono<ResponseEntity<InputStreamResource>>

        InputStream inputStream = getInputStreamFromFluxDataBuffer(gebruikersCSV);

        InputStreamResource resource = new InputStreamResource(inputStream);

        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + gebruikerService.determineCSVFileName())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(resource)
        );
    }

    @GetMapping("/{uid}")
    public Mono<UiGebruikerDTO> getGebruiker(
            @RequestAttribute("principal") Optional<SystemUser> systemUser, @PathVariable(name = "uid") String uid) {

        return gebruikerService.getGebruiker(systemUser, uid);
    }

    /**
     *
     * @param uid id van de gebruiker van wie het password veranderd worden moet.
     * @param systemUser De system user, die de password reset uitvoert.
     * @param passwordDTO Het nieuwe password en de confirmation.
     * @return  No result
     */
    @PutMapping("/{uid}/password")
    public Mono<ResponseEntity<Void>> updateUserPassword(
            @PathVariable String uid,
            @RequestAttribute("principal") Optional<SystemUser> systemUser,
            @Valid @RequestBody PasswordDTO passwordDTO
    ) {
        try {
            log.info("password = " + passwordDTO.getPassword());
            return gebruikerService.updateUserPassword(uid, systemUser, passwordDTO)
                .doOnError(
                    FouteInvoerException.class,
                    error -> {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                )
                .doOnError(
                    NietGeautoriseerdException.class,
                    error -> {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                    }
                )
                .doOnError(
                    GebruikerNietGevondenException.class,
                    error -> {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                    }
                );
        } catch (GebruikersAdministratieException e) {
            return Mono.error(e);
        }
    }

    @GetMapping("/{uid}/generated_password")
    public Mono<PasswordDTO> resetUserPassword(
            @PathVariable String uid,
            @RequestAttribute("principal") Optional<SystemUser> systemUser
    ) {
        try {
            return gebruikerService.resetPassword(uid, systemUser)
                    .doOnError(
                            FouteInvoerException.class,
                            error -> {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                            }
                    )
                    .doOnError(
                            NietGeautoriseerdException.class,
                            error -> {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                            }
                    )
                    .doOnError(
                            GebruikerNietGevondenException.class,
                            error -> {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                            }
                    );
        } catch (GebruikersAdministratieException e) {
            return Mono.error(e);
        }
    }

    private InputStream getInputStreamFromFluxDataBuffer(Flux<DataBuffer> data) throws IOException {

        try (PipedOutputStream osPipe = new PipedOutputStream()) {
            PipedInputStream isPipe = new PipedInputStream(osPipe);
            DataBufferUtils.write(data, osPipe)
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(DataBufferUtils.releaseConsumer());

            return isPipe;

        }
    }

}
