Feature: Wachtwoord wijzigen

  Scenario: Wachtwoord van gebruiker wijzigen door beheerder
    Given dat de beheerder 'BKWI Beheerder' is ingelogd en de gebruikersdetail pagina van 'bkwi_fr1000' is geopend
    Then  zijn de gebruikersdetails van 'bkwi_fr1000' zichtbaar
    And   zijn twee wachtwoord-velden zichtbaar
    And   zijn de textvelden rood
    And   wordt het wachtwoord 'W8woord!' in het 'wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop niet actief
    And   wordt de waarschuwing 'Wachtwoord komt niet overeen' weergegeven
    And   wordt het wachtwoord 'W8woord!' in het 'bevestig-wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop actief
    And   als op Opslaan wordt geklikt
    Then  wordt een bevestiging weergegeven
    And   wordt het nieuwe wachtwoord opgeslagen voor de gebruiker 'bkwi_fr1000'
    # TODO: Onderstaand kan alleen getest worden als de Gebruikers Administratie inlog-functionaliteit heeft
    #And   kan de gebruiker 'bkwi_fr1000' met het nieuwe wachtwoord inloggen

  Scenario: Wachtwoord moet gelijk zijn controleren
    Given dat de beheerder 'BKWI Beheerder' is ingelogd en de gebruikersdetail pagina van 'bkwi_fr1000' is geopend
    Then  zijn de gebruikersdetails van 'bkwi_fr1000' zichtbaar
    And   zijn twee wachtwoord-velden zichtbaar
    And   wordt het wachtwoord '123' in het 'wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop niet actief
    And   wordt het wachtwoord 'ABC' in het 'bevestig-wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop niet actief
    And   wordt de waarschuwing 'Wachtwoord komt niet overeen' weergegeven

  Scenario: Wachtwoord wijzigen afbreken met 'back' knop
    Given dat de beheerder 'BKWI Beheerder' is ingelogd en de gebruikersoverzichtpagina is geopened
    And   er wordt op de details-knop van de gebruiker 'bkwi_fr1000' geklikt
    Then  zijn de gebruikersdetails van 'bkwi_fr1000' zichtbaar
    And   zijn twee wachtwoord-velden zichtbaar
    And   zijn de textvelden rood
    And   wordt het wachtwoord 'W8woord!' in het 'wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop niet actief
    And   wordt de waarschuwing 'Wachtwoord komt niet overeen' weergegeven
    And   wordt het wachtwoord 'W8woord!' in het 'bevestig-wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop actief
    And   wordt terug genavigeerd in de browser
    Then  wordt het nieuwe wachtwoord niet opgeslagen voor de gebruiker 'bkwi_fr1000'

  Scenario: Beheerder kan het eigen wachtwoord niet wijzigen
    Given dat de beheerder 'BKWI Beheerder' is ingelogd en de gebruikersdetail pagina van 'beheerder_bkwi' is geopend
    Then  zijn de gebruikersdetails van 'beheerder_bkwi' zichtbaar
    And   zijn twee wachtwoord-velden zichtbaar
    And   zijn de textvelden rood
    And   wordt het wachtwoord 'W8woord!' in het 'wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop niet actief
    And   wordt de waarschuwing 'Wachtwoord komt niet overeen' weergegeven
    And   wordt het wachtwoord 'W8woord!' in het 'bevestig-wachtwoord' veld ingevuld
    Then  wordt de Opslaan knop actief
    And   als op Opslaan wordt geklikt
    Then  wordt de foutmelding 'Er is een fout opgetreden' weergegeven
    Then  wordt het nieuwe wachtwoord niet opgeslagen voor de gebruiker 'beheerder_bkwi'
