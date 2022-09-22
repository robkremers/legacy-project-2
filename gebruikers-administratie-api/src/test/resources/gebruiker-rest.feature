#language: nl
Functionaliteit: Testen of de service om de LDAP lijst van gebruikers te halen

  Achtergrond: de LDAP lijst van gebruikers halen

  Scenario: een BKWI beheerder vraagt een lijst van gebruikers op
    Gegeven een BKWI beheerder is ingelogd
    Wanneer de lijst van gebruikers wordt opgehaald
    Dan bevat de lijst de verwachte lijst van BKWI gebruikers

  Scenario: een DUO beheerder vraagt een lijst van gebruikers op
    Gegeven een DUO beheerder is ingelogd
    Wanneer de lijst van gebruikers wordt opgehaald
    Dan bevat de lijst de verwachte lijst van DUO gebruikers

  Scenario: Verkeerde header in request levert foutmelding
    Als er een verkeerde header wordt bevraagd
    Dan komt er een status 400 terug