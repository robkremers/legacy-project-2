Feature: Gebruikersoverzicht-pagina

  Scenario: Overzicht gebruikers tonen
    Given dat de landingspagina is geopend en de 'BKWI Beheerder' is ingelogd
    When de gebruikersoverzicht-pagina wordt geopend
    Then is het aantal kolommen van het gebruikersoverzicht 6
    And  worden er 64 gebruikers weergegeven in het gebruikersoverzicht
    And  zijn de headers van het gebruikersoverzicht als volgt
      | Afdeling | Naam | Achternaam | Voornaam | E-mail | Bekijk details |
    And  de 'Export gebruikers' knop is zichtbaar
    #And  als ik de 'Export gebruikers' knop indruk wordt het gebruikersoverzicht gedownload
