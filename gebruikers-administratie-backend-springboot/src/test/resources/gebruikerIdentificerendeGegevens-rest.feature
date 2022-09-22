#language: nl
Functionaliteit: Testen of de service om de LDAP lijst van gebruikers te halen

  Achtergrond: de LDAP lijst van gebruikers halen

  Scenario: een BKWI beheerder vraagt de gegevens van gebruikers op
    Gegeven een BKWI beheerder is ingelogd
    Wanneer de identificerende gegevens van "gebruiker_bkwi" wordt opgehaald
    Dan  bevat de lijst de verwachte identificerende gegevens van de gebruiker
    En bevat de lijst geen "userPassword"
    En  de waarde van "userId" is "gebruiker_bkwi"

#gebruiker_identificerende_gegevens