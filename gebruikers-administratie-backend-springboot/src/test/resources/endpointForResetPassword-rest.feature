#language: nl
Functionaliteit: endpoint for reset password

  Achtergrond: het userPassword gebruiker resetten

  Abstract Scenario: een BKWI beheerder haalt de identificerende gegevens van zich zelf op
    Gegeven een BKWI beheerder is ingelogd
    Wanneer de identificerende gegevens van <inlogNaam> wordt opgehaald
    En bevat de lijst geen <userPassword>
    En de waarde van <userId> is <inlogNaam>
    Als de waarde van password <password> en de waarde van confirmation <confirmation> voor <inlogNaam> dan is de statuscode <verwachtestatusCode>
    Wanneer de identificerende gegevens van <inlogNaam> wordt opgehaald
    En bevat de lijst geen <userPassword>
    Voorbeelden:
      | inlogNaam        | userPassword   | userId   | password          | confirmation      | verwachtestatusCode |
      | "beheerder_bkwi" | "userPassword" | "userId" | ""                | ""                | 400                 |
      | "beheerder_bkwi" | "userPassword" | "userId" | "1"               | ""                | 400                 |
      | "beheerder_bkwi" | "userPassword" | "userId" | "2"               | "3"               | 400                 |
      | "beheerder_bkwi" | "userPassword" | "userId" | "J5.1+b/4Ii^>V2z" | "J5.1+b/4Ii^>V2z" | 401                 |
      | "beheerder_bkwi" | "userPassword" | "userId" | "c1<It–M.Xm,Ga8#" | "c1<It–M.Xm,Ga8#" | 401                 |

  Abstract Scenario: een BKWI beheerder haalt de identificerende gegevens van gebruiker_bkwi op
    Gegeven een BKWI beheerder is ingelogd
    Wanneer de identificerende gegevens van <inlogNaam> wordt opgehaald
    En bevat de lijst geen <userPassword>
    En de waarde van <userId> is <inlogNaam>
    Als de waarde van password <password> en de waarde van confirmation <confirmation> voor <inlogNaam> dan is de statuscode <verwachtestatusCode>
    Wanneer de identificerende gegevens van <inlogNaam> wordt opgehaald
    Dan bevat de lijst de verwachte identificerende gegevens van de gebruiker
    En bevat de lijst geen <userPassword>
    Voorbeelden:
      | inlogNaam        | userPassword   | userId   | password          | confirmation      | verwachtestatusCode |
      | "gebruiker_bkwi" | "userPassword" | "userId" | ""                | ""                | 400                 |
      | "gebruiker_bkwi" | "userPassword" | "userId" | "1"               | ""                | 400                 |
      | "gebruiker_bkwi" | "userPassword" | "userId" | "2"               | "3"               | 400                 |
      | "gebruiker_bkwi" | "userPassword" | "userId" | "2+M,Q._}:gs!/J+" | "2+M,Q._}:gs!/J+" | 200                 |
      | "gebruiker_bkwi" | "userPassword" | "userId" | "_XI^9!vbU6,Q7NV" | "_XI^9!vbU6,Q7NV" | 200                 |


  Abstract Scenario: een DUO beheerder haalt de identificerende gegevens van gebruiker_duo op
    Gegeven een DUO beheerder is ingelogd
    Wanneer de identificerende gegevens van <inlogNaam> wordt opgehaald
    En bevat de lijst geen <userPassword>
    En de waarde van <userId> is <inlogNaam>
    Als de waarde van password <password> en de waarde van confirmation <confirmation> voor <inlogNaam> dan is de statuscode <verwachtestatusCode>
    Wanneer de identificerende gegevens van <inlogNaam> wordt opgehaald
    En bevat de lijst geen <userPassword>
    Voorbeelden:
      | inlogNaam       | userPassword   | userId   | password          | confirmation      | verwachtestatusCode |
      | "gebruiker_duo" | "userPassword" | "userId" | ""                | ""                | 400                 |
      | "gebruiker_duo" | "userPassword" | "userId" | "1"               | ""                | 400                 |
      | "gebruiker_duo" | "userPassword" | "userId" | "2"               | "3"               | 400                 |
      | "gebruiker_duo" | "userPassword" | "userId" | "34Ns:25n?)Sj8S@" | "34Ns:25n?)Sj8S@" | 200                 |
      | "gebruiker_duo" | "userPassword" | "userId" | "e(PzP*H17:ih@Su" | "e(PzP*H17:ih@Su" | 200                 |