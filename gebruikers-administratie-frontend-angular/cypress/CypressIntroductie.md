# Cypress

Cypress wordt gebruikt voor het maken van de testen

### Cypress toevoegen aan project

Als je een nieuwe project begint is het nodig om de Cypress dependencies toe te voegen aan je project,
gebruik hiervoor het commando: `npm install cypress --save-dev`

Als Cypress toegevoegd is aan je project dan zal een leeg bestand met de naam `cypress.json` en de folder `cypress` erbij hebben gekregen.

#### Cypress folder structuur

In de folder cypress staan na installatie een 4 tal folders:
- fixtures =>  In deze folder kan je data bestanden neerzetten die door je cypress testen gebruikt kunnen worden.
- integration => In deze folder komen al je testen te staan
- plugins => place-holder voor de plugins die je kan gebruiken binnen Cypress
- support => de index.js in deze folder wordt altijd als eerste aangeroepen.
  In dit bestand kan je configuratie plaatsen die voordat de testen worden gestart eerst moet worden uitgevoerd.

#### Cypress `cypress.json`

In de cypress.json kan je configuratie settings opnemen die door cypress en de daaronder geinstallerde componenten gebruikt wordt.

Een voorbeeld van een cypress.json:

    {  
      "testFiles": [
        "**/*.{feature,features}",
        "**/*.spec.js"
      ],

      "viewportHeight" : 768,  
      "viewportWidth" : 1024,
      "video": false,

      "compilerOptions": {
        "target": "es5",
        "lib": ["es5", "dom"],
        "types": ["cypress"]
      },
      
      "include": ["**/*.ts"]
    }

### Cypress tests schrijven
een test (mijn_test) schrijven wordt het binnen integration folder onder de naam mijn_test.spec.js

### Cypress tests draaien 'npx cypress open'
Om de tests te laat draaien ga je naar projectfolder en gebruik hiervoor het commando :
'npx cypress open'
Dan wordt de volgende  pagina geopend

Pagina moet nog toegevoegd worden

### Cucumber/Gherking toevoegen
Om cucumber/Gherking te gebruiken dien je het hiervoor eerst de dependencies te installeren en configuratie toe te voegen.

De dependencies kan je installeren met het commando: `npm install --save-dev cypress-cucumber-preprocessor`

In het bestand `cypress/plugins/index.js` dient de volgende configuratie toegevoegd te worden:

    const cucumber = require('cypress-cucumber-preprocessor').default

    module.exports = (on, config) => {
    on('file:preprocessor', cucumber())
    }

In het bestand `package.json` dient de volgende configuratie toegevoegd te worden:

      "cypress-cucumber-preprocessor": {
      "nonGlobalStepDefinitions": false,
      "stepDefinitions": "cypress/integration/step-definitions",
      "cucumberJson": {
        "generate": true,
        "outputFolder": "testReports/cucumber_report/cucumber-json",
        "filePrefix": "",
        "fileSuffix": ".cucumber"
      }
    }

In het bestand `cypress.json` dient minimaal de volgende configuratie aan wezig te zijn als je wilt alleen .feature test runnen:

    {
    "testFiles": "**/*.feature"
    }

Meer informatie over cucumber/Gherking binnen Cypress kan gevonden worden op:
https://github.com/TheBrainFamily/cypress-cucumber-preprocessor
https://www.youtube.com/watch?v=uOpItjWtaFE

### Mochawsome HTML rapportage toevoegen

Rapportages kunnen op verschillende manieren toegepast worden.
Hieronder beschrijf ik hoe Mochawsome rapportage toepast:

Installatie van de dependencies gebeurd met:
`npm install --save-dev mocha cypress-multi-reporters mochawesome mochawesome-merge mochawesome-report-generator`

Voeg de volgende report setting toe aan `cypress.json`

    {  
      "testFiles": [
        "**/*.{feature,features}",
        "**/*.spec.js"
      ],

      "viewportHeight" : 768,  
      "viewportWidth" : 1024,
      "video": false,

      "compilerOptions": {
        "target": "es5",
        "lib": ["es5", "dom"],
        "types": ["cypress"]
      },
      "include": ["**/*.ts"]
    }

In de `package.json` kan je vervolgens scripts toevoegen om het uitvoeren van de testen en het samenvoegen van de rapportages te vergemakkelijken.

Om de  JSON output te converteren naar HTML-rapport voeg de volgende bestand `cucumber-report.js` toe en zorg ervoor dat je dit bestand op het hoofdniveau van het project plaatst
NB: de naam van het bestaand mag je zelf kiezen (mijnbestaand.js)

Voeg toe aan `cucumber-report.js`:

    const report = require('multiple-cucumber-html-reporter');
    report.generate({
      jsonDir: './testReports/cucumber_report/cucumber-json/',
      reportPath: './testReports/cucumber_report/',

      reportName : '<center><p><h1>Testresultaten BDD-testen Gebruikers-Adminstratie</h1></p></center>',
      pageTitle : 'Testresultaten BDD-testen',
      pageFooter : '<center><p><strong>Gebruikers-Adminstratie</strong></p></center>',
      displayDuration: true,
      displayReportTime: true
	
    });
Voor meer informatie rapportage bekijk over  de site:
https://blog.knoldus.com/generating-cucumber-html-report-cypress/

### HTML-rapport te genereren

Om het HTML-rapport te genereren, voer je het bestand `cucumber-report.js` uit. Maar zorg ervoor dat u dit bestand op het hoofdniveau van het project plaatst, niet in de Cypress folder. Omdat het slechts een javaScript-bestand is, kunnen we het uitvoeren met de volgende commando's:

    1- npx cypress open
    2- node `cucumber-report.js`

### Openen van het HTML-rapport
Het html rapportage wordt opgeslagen in `testReports/cucumber_report/index.html`

# Documentatie links:

- https://www.cypress.io/
- Cypres in a nutshell: https://docs.cypress.io/guides/overview/why-cypress#In-a-nutshell
- Cypress Cucumber/Gherkin: https://github.com/TheBrainFamily/cypress-cucumber-preprocessor
- Cypress Mochawsomereport: https://medium.com/tech-learn-share/attach-screenshot-into-mochawesome-html-report-in-cypress-ca3792081474
- Cypress HTML Cucumber report implementatie: https://blog.knoldus.com/generating-cucumber-html-report-cypress/
- Cypress HTML Cucumber report: https://github.com/wswebcreation/multiple-cucumber-html-reporter/blob/HEAD/README.MD#custommetadata
- Cypress multiple environments: https://dzone.com/articles/configure-cypress-tests-to-run-on-multiple-environments
- Run Cypress met verschillende browsers: https://docs.cypress.io/guides/guides/launching-browsers#Browsers
- https://github.com/TheBrainFamily/cypress-cucumber-preprocessor
- https://github.com/wswebcreation/multiple-cucumber-html-reporter
