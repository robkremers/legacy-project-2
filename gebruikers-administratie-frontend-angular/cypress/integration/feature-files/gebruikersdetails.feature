Feature: Gebruikersdetails-pagina

  Scenario Outline: Details van een gebruiker tonen
    Given dat de landingspagina is geopend en de 'BKWI Beheerder' is ingelogd
    When de gebruikersoverzicht-pagina wordt geopend
    And  er wordt op de details-knop van de gebruiker '<gebruikersnaam>' geklikt
    Then wordt de details-pagina van de gebruiker '<gebruikersnaam>' weergegeven
    And  bevat het veld 'naam-id' '<naam>'
    And  bevat het veld 'inlognaam-id' '<inlognaam>'
    And  bevat het veld 'voornaam-id' '<voornaam>'
    And  bevat het veld 'achternaam-id' '<achternaam>'
    And  bevat het veld 'telefoonnummer-id' '<telefoonnumer>'
    And  bevat het veld 'email-id' '<emailadres>'
    And  bevat het veld 'werknemernummer-id' '<werknemernummer>' 

    Examples:
        | gebruikersnaam | naam            | inlognaam      | voornaam | achternaam | telefoonnumer | emailadres               | werknemernummer |
        | gebruiker_bkwi | BKWI Gebruiker  | gebruiker_bkwi | BKWI     | Gebruiker  | -             | gebruiker_bkwi@bkwi.nl   | -               |
        | bkwi_fr1030    | BKWI FR1030     | bkwi_fr1030    | BKWI     | FR1030     | -             | bkwi_fr1030@bkwi.nl      | -               |
        | bkwi_fr1022    | BKWI FR1022     | bkwi_fr1022    | BKWI     | FR1022     | -             | bkwi_fr1022@bkwi.nl      | -               |