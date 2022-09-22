# Cypress Testen

Cypress testen worden in dit project gebruikt om automatisch regressietesten uit te voeren.

De testen zijn in de stijl van Cucumber geschreven.

# Draaien van de Cypress tetsen

## Dependencies

Voordat je de Cypress testen kan draaien zul je wat dependencies moeten installeren:

```bash
npm install cypress neat-csv
```

## Opstarten van afhankelijkheden

De Cypress testen zijn afhankelijk van zowel de frontend- als backend-services, en een LDAP-stub.

### Backend

De backend onderdelen die gestart moeten worden zijn:

1. `gebruikers-administratie-backend-sprinboot > GebruikersAdministratieApplication`
2. `gebruikers-administratie-api > GebruikersAdministratieApplication`

NB: Deze services moeten gestart worden met het 'dev' profiel, door hetvolgende toe te voegen aan de `Environment variables` in IntelliJ onder `Run > Edit Configurations...`

```
-spring.profiles.active=dev
```

De ldap-stub draait in een docker container en wordt opgestart in het sas-project, dat gaat als volgt:

```bash
cd <WORKSPACE>/sas/
docker-compose --file ./docker/docker-compose-ldap.yml --project-name inkijk up
```

Het frontend-project is dit project zelf, en hiervoor wordt een server gestart:

```bash
cd <WORKSPACE>/gebruikers-administratie-frontend-angular
ng serve
```

Als het goed is draaien nu alle services en is de gebruikersadministratie lokaal te benaderen op `localhost:4200`

## Cypress draaien

### Cypress openen

Nu alle services draaien, kan Cypress gestart worden:

```bash
cd <WORKSPACE>/gebruikers-administratie-frontend-angular
npx cypress open
```

Nu opent de Cypress-browser zich, en zijn de feature-files zichtbaar.

Rechtsbovenin kan gekozen worden met welke browser getest wordt (Chrome/Firefox).

Door op een feature-file te klikken, wordt deze specifieke feature-test gestart.

Rechtsbovenin kunnen ook alle feature-files achter elkaar gestart worden door op `Run <X> integration specs` te klikken.

### Cypress draaien in headless mode

Voor het draaien van Cypress in headless mode is een ander commando nodig:

```bash
npx cypress run
```

Nu draaien alle tests in headless mode, en komt er een overzicht van de resultaten.

## Aanmaken/aanpassen van testen

### Structuur

De testen staan in de feature-files:

`./cypress/integration/feature-files/`

De testen worden per onderdeel in een eigen feature-file geschreven, in de Gherkin-syntax. 

Elke feature file heeft een eigen step-definition-folder met 

`./cypress/integration/step-definitions/<feature>/`

## Test Overzicht

Op dit moment zijn de volgende testen aanwezig:

| BDD-test             | Feature-file        | Omschrijving                                               |
|----------------------|---------------------|------------------------------------------------------------|
| Landingspagina       | landingspagina      | Testen of de menu items van de landingspagina aawezig zijn |
| Overzicht gebruikers | gebruikersoverzicht | Overzicht gebruikers tonen                                 |
| Gebruikers details   | gebruikersdetails   | Details van gebruikers tonen                               |
| Wachtwoord wijzigen  | wachtwoordwijzigen  | Wachtwoord van een gebruiker wijzigen                      |
