Feature: Landingspagina

  Scenario: Controleer dat alle knoppen aanwezig zijn
    Given dat de landingspagina is geopend en de gebruiker 'BKWI Beheerder' is ingelogd
    Then  zijn de volgende knoppen zichtbaar
      | [knopTekst]                  |
      | Reset wachtwoord             |
      | Beheer gebruikersaccount     |
      | Overzicht gebruikers         |
      | Controle gebruik accounts    |
      | Schonen ongebruikte accounts |
      | Beheer rollen                |
      | Beheer admin accounts        |
      | Beheer gemandateerden        |

  Scenario: Controleer dat de header en footer zichtbaar zijn
    Given dat de landingspagina is geopend en de gebruiker 'BKWI Beheerder' is ingelogd
    Then  is de header zichtbaar met het logo van Suwinet
    And   is de footer zichtbaar met het logo van BKWI

