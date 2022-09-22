package nl.bkwi.gebruikersadministratie.gebruiker;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import lombok.extern.slf4j.Slf4j;
import nl.bkwi.gebruikersadministratie.dto.GebruikerDTO;
import nl.bkwi.gebruikersadministratie.dto.PasswordDTO;
import nl.bkwi.gebruikersadministratie.exceptions.GebruikersAdministratieException;
import nl.bkwi.gebruikersadministratie.systemuser.SystemUser;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class GebruikerService {

    private static final char SEPARATOR = ';';
    private final GebruikerApiClient gebruikerApiClient;

    public GebruikerService(GebruikerApiClient gebruikerApiClient) {
        this.gebruikerApiClient = gebruikerApiClient;
    }

    private UiGebruikerDTO mapGebruikerDTO2UiGebruikerDTO(GebruikerDTO dto) {
        return UiGebruikerDTO.builder()
                .naam(dto.getNaam())
                // de inlognaam is gelijk aan de user id
                .inlognaam(dto.getUserId())
                .achternaam(dto.getAchternaam())
                .email(dto.getEmail())
                .afdeling(dto.getAfdeling())
                .initialen(dto.getInitialen())
                .telefoonnummer(dto.getTelefoonnummer())
                .voornaam(dto.getVoornaam())
                .userId(dto.getUserId())
                .employeeNr(dto.getEmployeeNr())
                .build();
    }

    public Flux<UiGebruikerDTO> getGebruikers(Optional<SystemUser> systemUser) {
        log.debug("retrieving all gebruiker(s)");

        if (systemUser.isEmpty()) {
            return Flux.empty();
        }

        return gebruikerApiClient.getGebruikers(systemUser.get().getDn())
                .map(this::mapGebruikerDTO2UiGebruikerDTO);
    }

    public Flux<DataBuffer> exportGebruikers(Optional<SystemUser> systemUser) {
        log.debug("export gebruiker(s)");

        if (systemUser.isEmpty()) {
            return Flux.empty();
        }

        Flux<GebruikerDTO> gebruikers = gebruikerApiClient.getGebruikers(systemUser.get().getDn());

        return convertToCsv(gebruikers);
    }

    public Mono<UiGebruikerDTO> getGebruiker(Optional<SystemUser> systemUser, String uid) {
        if (systemUser.isEmpty()) {
            return Mono.empty();
        } else {

            Mono<GebruikerDTO> gebruikerDTO = gebruikerApiClient.getGebruiker(systemUser.get().getDn(), uid);

            return gebruikerDTO.map(this::mapGebruikerDTO2UiGebruikerDTO);
        }
    }

    /**
     * Deze functie is bedoeld om een manuele wijziging van het wachtwoord in het frontend te ondersteunen.
     *
     * @param uid         id van de gebruiker van wie het wachtwoord veranderd worden moet.
     * @param systemUser  De system user, die de wachtwoord-reset uitvoert.
     * @param passwordDTO Bevat nieuwe wachtwoord.
     * @return HttpStatus 200 in case of success.
     * @throws GebruikersAdministratieException
     */
    public Mono<ResponseEntity<Void>> updateUserPassword(
            String uid,
            Optional<SystemUser> systemUser,
            PasswordDTO passwordDTO) throws GebruikersAdministratieException {
        if (systemUser.isEmpty()) {
            throw new GebruikersAdministratieException("systemUser is null");
        }
        return gebruikerApiClient.updateUserPassword(uid, systemUser.get().getDn(), passwordDTO);
    }

    /**
     * Deze functie is bedoeld om een aanvraag in het frontend van het genereren van een wachtwoord te ondersteunen.
     *
     * @param uid        id van de gebruiker van wie het wachtwoord veranderd worden moet.
     * @param systemUser De system user, die de password-reset uitvoert.
     * @return Mono<PasswordDTO> Bevat het gegenereerde wachtwoord. Dit moet in het frontend getoond worden.
     * @throws GebruikersAdministratieException
     */
    public Mono<PasswordDTO> resetPassword(
            String uid,
            Optional<SystemUser> systemUser) throws GebruikersAdministratieException {
        if (systemUser.isEmpty()) {
            throw new GebruikersAdministratieException("systemUser is null");
        }

        return gebruikerApiClient.resetUserPassword(uid, systemUser.get().getDn());
    }

    private Flux<DataBuffer> convertToCsv(Flux<GebruikerDTO> gebruikers) {
        Flux<DataBuffer> csvHeader = makeCSVHeader();
        Flux<DataBuffer> csvBody = makeCSVBody(gebruikers);
        return csvHeader.concatWith(csvBody);
    }

    private Flux<DataBuffer> makeCSVHeader() {
        try {
            return Flux.from(convertToDataBuffer(csvHeader()));
        } catch (IOException e) {
            return Flux.error(e);
        }
    }

    private Flux<DataBuffer> makeCSVBody(Flux<GebruikerDTO> gebruikers) {

        // Deze publisher functie converteert een GebruikerDTO naar een Mono met een CSV regel en zet eventuele IOExceptions om naar Mono errors.
        Function<GebruikerDTO, Publisher<String>> gebruikerDTOToCSVLinePublisherFunction = gebruikerDTO -> {
            try {
                String csvLine = convertToCSVLine(gebruikerDTO);
                return Mono.just(csvLine);
            } catch (IOException e) {
                return Mono.error(e);
            }
        };

        return gebruikers
                .concatMap(gebruikerDTOToCSVLinePublisherFunction)
                .concatMap(this::convertToDataBuffer);
    }

    private String convertToCSVLine(GebruikerDTO gebruikerDTO) throws IOException {
        StringWriter out = new StringWriter();
        ICSVWriter csvWriter = new CSVWriterBuilder(out).withSeparator(SEPARATOR).build();
        csvWriter.writeAll(Collections.singleton(convertToColumns(gebruikerDTO)));
        csvWriter.close();
        out.close();
        return out.toString();
    }

    private Mono<DataBuffer> convertToDataBuffer(String text) {
        DataBuffer dataBuffer = null;
        boolean release = false;
        try {
            DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
            dataBuffer = bufferFactory.allocateBuffer();
            release = true;
            dataBuffer.write(text, StandardCharsets.UTF_8);
            release = false;
            return Mono.just(dataBuffer);
        } finally {
            if (release) {
                DataBufferUtils.release(dataBuffer);
            }
        }
    }

    public String csvHeader() throws IOException {
        return convertToCSVHeader(headerColumnNames());
    }

    public String convertToCSVHeader(String[] headerColumnNames) throws IOException {
        StringWriter out = new StringWriter();
        ICSVWriter csvWriter = new CSVWriterBuilder(out).withSeparator(SEPARATOR).build();
        csvWriter.writeAll(Collections.singleton(headerColumnNames));
        csvWriter.close();
        out.close();
        return out.toString();
    }

    private String[] convertToColumns(GebruikerDTO gebruikerDTO) {
        return new String[]{
                gebruikerDTO.getAchternaam(),
                gebruikerDTO.getVoornaam(),
                gebruikerDTO.getEmail(),
                gebruikerDTO.getAfdeling(),
                gebruikerDTO.getTelefoonnummer(),
                gebruikerDTO.getUserId()};
    }

    public static String[] headerColumnNames() {
        return new String[]{"achternaam", "voornaam", "email", "afdeling", "telefoonnummer", "userid"};
    }

    public String determineCSVFileName() {
        return String.format("export-gebruikers-%s.csv", nowInAmsterdam());
    }

    private String nowInAmsterdam() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Amsterdam"));
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));
    }
}
