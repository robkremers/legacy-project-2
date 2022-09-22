# Angular Unit and Cypress end-to-end Tests

[[_TOC_]]

## Introduction

This is a short overview of a study on unit and integration testing. It is not meant to encompass all possible topics,
but is intended to give a better understanding of how Karma / Jasmine for unit testing and Cypress for integration testing 
works en which libraries support the functionality.

## Documentation

### Angular

Genka: Angular Unit Test Tutorial - Crash Course for Angular & Jasmine
- https://www.youtube.com/watch?v=ibatZSCgXLY
- https://github.com/tamani-coding/angular-testing-examples
- https://dev.to/angular/understanding-async-tests-in-angular-f8n
    - Must read!

Angular general documentation:
- https://angular.io/guide/testing

Angular API documentation:
- https://angular.io/api/core/testing
    - https://angular.io/api/core/testing/fakeAsync

Jasmine documentation:
- https://scriptverse.academy/tutorials/jasmine.html
    - Study completely.
- https://scriptverse.academy/tutorials/jasmine-createspy-createspyobj.html
- https://scriptverse.academy/tutorials/jasmine-spyon.html
- https://jasmine.github.io/api/edge/jasmine.html
- https://jasmine.github.io/tutorials/async
- Juri Strumpflohner: async await vs waitForAsync in Angular tests
    - https://www.youtube.com/watch?v=LWjDG9D-DZU
- https://angular-university.io/lesson/angular-testing-understanding-jasmine-done

Angular test setup:

- Angular uses Jasmine which is a Behaviour-Driven Test framework.
  - https://jasmine.github.io/pages/docs_home.html
- Angular uses a test runner called Karma: it runs the tests and provides a test report.
  - https://angular.io/guide/testing
  - https://codecraft.tv/courses/angular/unit-testing/overview/
    - https://codecraft.tv/courses/angular/unit-testing/jasmine-and-karma/
    - https://codecraft.tv/courses/angular/unit-testing/asynchronous/
      - Jawache: EP 13.7 - Angular / Unit Testing / Asynchronous Code
        - https://www.youtube.com/watch?v=V50kMQ5QhgA
        - https://github.com/codecraft-tv/angular-course/tree/current/13.unit-testing/7.asynchronous
  - https://www.digitalocean.com/community/tutorials/testing-angular-with-jasmine-and-karma-part-1

### Cypress

- https://www.cypress.io/
- Cypres in a nutshell: https://docs.cypress.io/guides/overview/why-cypress#In-a-nutshell
- Cypress Cucumber/Gherkin: https://github.com/TheBrainFamily/cypress-cucumber-preprocessor
- Cypress
  Mochawsomereport: https://medium.com/tech-learn-share/attach-screenshot-into-mochawesome-html-report-in-cypress-ca3792081474
- Cypress HTML Cucumber report
  implementatie: https://blog.knoldus.com/generating-cucumber-html-report-cypress/
- Cypress HTML Cucumber
  report: https://github.com/wswebcreation/multiple-cucumber-html-reporter/blob/HEAD/README.MD#custommetadata
- Cypress multiple
  environments: https://dzone.com/articles/configure-cypress-tests-to-run-on-multiple-environments
- Run Cypress met verschillende
  browsers: https://docs.cypress.io/guides/guides/launching-browsers#Browsers
- https://github.com/TheBrainFamily/cypress-cucumber-preprocessor
- https://github.com/wswebcreation/multiple-cucumber-html-reporter


## Cypress end-to-end Tests

### Introductie

Cypress wordt gebruikt voor het maken van de testen

### Cypress toevoegen aan project

Als je een nieuwe project begint is het nodig om de Cypress dependencies toe te voegen aan je
project,
gebruik hiervoor het commando: `npm install cypress --save-dev`

Als Cypress toegevoegd is aan je project dan zal een leeg bestand met de naam `cypress.json` en de
folder `cypress` erbij hebben gekregen.

#### Cypress folder structuur

In de folder cypress staan na installatie een 4 tal folders:

- fixtures =>  In deze folder kan je data bestanden neerzetten die door je cypress testen gebruikt
  kunnen worden.
- integration => In deze folder komen al je testen te staan
- plugins => place-holder voor de plugins die je kan gebruiken binnen Cypress
- support => de index.js in deze folder wordt altijd als eerste aangeroepen.
  In dit bestand kan je configuratie plaatsen die voordat de testen worden gestart eerst moet worden
  uitgevoerd.

#### Cypress `cypress.json`

In de cypress.json kan je configuratie settings opnemen die door cypress en de daaronder
geinstallerde componenten gebruikt wordt.

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

Een test wordt geplaatst binnen de integration folder onder de naam <mijn_test>.spec.js.

### Cypress tests draaien 'npx cypress open'

Om de tests te laat draaien ga je naar projectfolder en gebruik hiervoor het commando :
'npx cypress open'
Dan wordt de volgende pagina geopend

Pagina moet nog toegevoegd worden

### Cucumber/Gherking toevoegen

Om cucumber/Gherking te gebruiken dien je het hiervoor eerst de dependencies te installeren en
configuratie toe te voegen.

De dependencies kan je installeren met het
commando: `npm install --save-dev cypress-cucumber-preprocessor`

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

In het bestand `cypress.json` dient minimaal de volgende configuratie aan wezig te zijn als je wilt
alleen .feature test runnen:

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

In de `package.json` kan je vervolgens scripts toevoegen om het uitvoeren van de testen en het
samenvoegen van de rapportages te vergemakkelijken.

Om de JSON output te converteren naar HTML-rapport voeg de volgende bestand `cucumber-report.js` toe
en zorg ervoor dat je dit bestand op het hoofdniveau van het project plaatst
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

Voor meer informatie rapportage bekijk over de site:
https://blog.knoldus.com/generating-cucumber-html-report-cypress/

### HTML-rapport te genereren

Om het HTML-rapport te genereren, voer je het bestand `cucumber-report.js` uit. Maar zorg ervoor dat
u dit bestand op het hoofdniveau van het project plaatst, niet in de Cypress folder. Omdat het
slechts een javaScript-bestand is, kunnen we het uitvoeren met de volgende commando's:

    1- npx cypress open
    2- node `cucumber-report.js`

### Openen van het HTML-rapport

Het html rapportage wordt opgeslagen in `testReports/cucumber_report/index.html`

### Cypress Testen

Cypress testen worden in dit project gebruikt om automatisch regressietesten uit te voeren.

De testen zijn in de stijl van Cucumber geschreven.

#### Draaien van de Cypress tetsen:

#### Dependencies

Voordat je de Cypress testen kan draaien zul je wat dependencies moeten installeren:

```bash
npm install cypress neat-csv
```

#### Opstarten van afhankelijkheden

De Cypress testen zijn afhankelijk van zowel de frontend- als backend-services, en een LDAP-stub.

##### Backend

De backend onderdelen die gestart moeten worden zijn:

1. `gebruikers-administratie-backend-sprinboot > GebruikersAdministratieApplication`
2. `gebruikers-administratie-api > GebruikersAdministratieApplication`

NB: Deze services moeten gestart worden met het 'dev' profiel, door hetvolgende toe te voegen aan
de `Environment variables` in IntelliJ onder `Run > Edit Configurations...`

```
-spring.profiles.active=dev
```

De ldap-stub draait in een docker container en wordt opgestart in het sas-project, dat gaat als
volgt:

```bash
cd <WORKSPACE>/sas/
docker-compose --file ./docker/docker-compose-ldap.yml --project-name inkijk up
```

Het frontend-project is dit project zelf, en hiervoor wordt een server gestart:

```bash
cd <WORKSPACE>/gebruikers-administratie-frontend-angular
ng serve
```

Als het goed is draaien nu alle services en is de gebruikersadministratie lokaal te benaderen
op `localhost:4200`

### Cypress draaien :

#### Cypress openen

Nu alle services draaien, kan Cypress gestart worden:

```bash
cd <WORKSPACE>/gebruikers-administratie-frontend-angular
npx cypress open
```

Nu opent de Cypress-browser zich, en zijn de feature-files zichtbaar.

Rechtsbovenin kan gekozen worden met welke browser getest wordt (Chrome/Firefox).

Door op een feature-file te klikken, wordt deze specifieke feature-test gestart.

Rechtsbovenin kunnen ook alle feature-files achter elkaar gestart worden door
op `Run <X> integration specs` te klikken.

#### Cypress draaien in headless mode

Voor het draaien van Cypress in headless mode is een ander commando nodig:

```bash
npx cypress run
```

Nu draaien alle tests in headless mode, en komt er een overzicht van de resultaten.

### Aanmaken/aanpassen van testen

#### Structuur

De testen staan in de feature-files:

`./cypress/integration/feature-files/`

De testen worden per onderdeel in een eigen feature-file geschreven, in de Gherkin-syntax.

Elke feature file heeft een eigen step-definition-folder met

`./cypress/integration/step-definitions/<feature>/`

### Test Overzicht

Op dit moment zijn de volgende testen aanwezig:

| BDD-test             | Feature-file        | Omschrijving                                               |
|----------------------|---------------------|------------------------------------------------------------|
| Landingspagina       | landingspagina      | Testen of de menu items van de landingspagina aawezig zijn |
| Overzicht gebruikers | gebruikersoverzicht | Overzicht gebruikers tonen                                 |
| Gebruikers details   | gebruikersdetails   | Details van gebruikers tonen                               |
| Wachtwoord wijzigen  | wachtwoordwijzigen  | Wachtwoord van een gebruiker wijzigen                      |

## Karma / Jasmine unit testing

### Introduction

Karma handles the process of creating HTML files, opening browsers and running tests and returning
the results of those tests to the command line.

Jasmine is a JavaScript testing framework that supports a software development practice called
Behaviour-Driven Development

### The use of the Angular test functionality

Run `ng test` to execute tests.

The standard setting is something like:
```shell
ng test --watch --progress --code-coverage
```
This continuously checks progress / updates in the functionality and reruns the test when this occurs.  
This is the standard situation so `ng test` would normally suffice.  

Starting `ng test` will start a (Chrome) web page:
- http://localhost:9876/?id=59720442 # The id will differ of course.
- An overview of the test results will be visible

In order to provide an overview of the code coverage:
```shell
$ ng test --code-coverage

# Result:
=============================== Coverage summary ===============================
Statements   : 92.3% ( 12/13 )
Branches     : 100% ( 0/0 )
Functions    : 100% ( 5/5 )
Lines        : 90% ( 9/10 )
================================================================================
```

### Test coverage

`ng test` also allows for continuous checks on progress / updates. So this is preferred.
The ./coverage directory is created which holds the results.
- Run ./coverage/index.html in order to see the results more detailed than on the terminal.
  - This web page will show a detailed overview of the test coverage per component.
  - Click on the components to see where the coverage is still lacking.

If you want to create code-coverage reports every time you test,
set the following option in the CLI configuration file, **angular.json**:

```json
"test": {
  "options": {
    "codeCoverage": true
  }
}
```

Note:
- This has been set up in the above-mentioned way in gebruikers-administratie-frontend-angular.
- This is the standard situation.


### Debugging tests

- https://angular.io/guide/test-debugging

If your tests aren't working as you expect them to, you can inspect and debug them in the browser.

Debug specs in the browser in the same way that you debug an application.

1. Reveal the Karma browser window. See Set up testing if you need help with this step.
2. Click the DEBUG button to open a new browser tab and re-run the tests.
3. Open the browser's Developer Tools. On Windows, press Ctrl-Shift-I. On macOS, press Command-Option-I.
4. Pick the Sources section.
5. Press Control/Command-P, and then start typing the name of your test file to open it.
6. Set a breakpoint in the test.
7. Refresh the browser, and notice how it stops at the breakpoint.

### Tips & Tricks

- After downloading an example project from e.g. Github the application still needs to import all libraries.
    - Execute:
```shell
- $ npm install
```

- When executing the tests also check the content of the Web Developer Tools | Console.
    - It is possible that a test seems to have finished successfully but in reality caused errors during execution.

### (De)Activation of test classes and specific test methods

If you put an 'x' before a test function:
- describe() --> xdescribe()
  or before the 'it' method:
- it() --> xit()
  The test will be ignored.

If you put an 'f' before a test function Karma will focus on that test function or method.  
Other tests will not be executed.

### Different keywords used for testing asynchronous situations

For more instructive background:
- Jawache: EP 13.7 - Angular / Unit Testing / Asynchronous Code
  - https://www.youtube.com/watch?v=V50kMQ5QhgA
  - https://github.com/codecraft-tv/angular-course/tree/current/13.unit-testing/7.asynchronous
- https://angular.io/api/core/testing

In the following the different keywords and their use in code will be discussed.
For the different tests a beforeEach() is defined.

```typescript
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authService: AuthService;
    let el: DebugElement;

    beforeEach(() => {

        // refine the test module by declaring the test component
        TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [AuthService]
        });

        // create component and test fixture
        fixture = TestBed.createComponent(LoginComponent);

        // get test component from the fixture
        component = fixture.componentInstance;

        // UserService provided to the TestBed
        authService = TestBed.get(AuthService);

        //  get the "a" element by CSS selector (e.g., by class name)
        el = fixture.debugElement.query(By.css('a'));
    });
```

#### async
- operations (as in the example above) go inside the beforeEach() function.
- functions implicitly return a promise.
- Defined in @angular/core/testing.
- Will intercept and keep track of all Promises in a body.
- is deprecated. Use **waitForAsync()** instead.
- In the test methods thereafter the end result will be tested.
- Example:
```typescript
it('Button label via async() and whenStable()', async(() => {
  // async() knows about all the pending promises defined in it's function body.
  fixture.detectChanges();
  expect(el.nativeElement.textContent.trim()).toBe('Login');
  // So mocking the authService instead of using an actual authService.
  // We don't need to know the inner workings of AuthService.
  spyOn(authService, 'isAuthenticated').and.returnValue(Promise.resolve(true));
  
  component.ngOnInit();

  // Remember: this is an asynchronous call.
  // Waiting until the pending promises have been resolved.
  fixture.whenStable().then(() => {
    // This is called when ALL pending promises have been resolved
    fixture.detectChanges();
    expect(el.nativeElement.textContent.trim()).toBe('Logout');
  });

  component.ngOnInit();

}));
```

#### fakeAsync
- Defined in @angular/core/testing.
- Wraps a function to be executed in the fakeAsync zone:
    - Microtasks are manually executed by calling flushMicrotasks().
    - Will intercept and keep track of all Promises in a body.
    - Timers are synchronous; tick() simulates the asynchronous passage of time.
- Allows more finegrained control over timer.
- The code in the body is written as if the code is synchronous.
- It does not track HTTP requests but that should not be necessary in an isolated test.

```typescript
it('Button label via fakeAsync() and tick()', fakeAsync(() => {
    expect(el.nativeElement.textContent.trim()).toBe('');
    fixture.detectChanges();
    expect(el.nativeElement.textContent.trim()).toBe('Login');

    spyOn(authService, 'isAuthenticated').and.returnValue(Promise.resolve(true));

    component.ngOnInit();
    // Simulates the passage of time until all pending asynchronous activities complete
    // Replaces 'fixture.whenStable()' used in 'async'.
    tick();
    fixture.detectChanges();
    expect(el.nativeElement.textContent.trim()).toBe('Logout');
}));
```

#### waitForAsync()
- Situations in which is invoked:
    - a component
    - a method call on a component or a service that in turn triggers another call(e.g. HTTP calls or timeouts).
        - characterized by lack of control.
- In such a case use wrap the test in **waitForAsync()**:
- Example:
```typescript

  it(`should have as title 'angular-testing-examples'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('angular-testing-examples');
  });
// Is wrapped. Will handle all asynchcronous tasks that might happen within that invocation.
  it(`should have as title 'angular-testing-examples'`, waitForAsync(() => {
      const fixture = TestBed.createComponent(AppComponent);
      const app = fixture.componentInstance;
      expect(app.title).toEqual('angular-testing-examples');
    })
  );
```

#### async await
- This is native to JavaScript
- This is just a nicer way to handle promises.
- Instead of using **async await** it is also possible to use: **waitForAsync()**. See above.
- If situations like HTTP calls, over which you have no control, it is better to use **waitForAsync()**.
- Example:
```typescript
// app.component.spec.ts:
/**
 * Async operations go inside the beforeEach() function, to which the done() function is passed as an argument 
 * causing Jasmine to wait until it is called before executing the spec in the it() block.
 * For more options see: https://jasmine.github.io/tutorials/async
 */
describe('AppComponent', () => {
    // async: Tells JavaScript that this is an asynchronous function, to ensure that it is properly handled.
  beforeEach(async () => {
      // await: do not continue the execution until the promise has been resolved.
    await TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
    }).compileComponents(); // returns a promise.
  });
  //it()
});
```

### Jasmine functionality for implementing unit tests

- https://jasmine.github.io/pages/getting_started.html
- https://jasmine.github.io/api/edge/module-jasmine-core.html
- https://jasmine.github.io/api/edge/global.html

Some important functions are described below.

Note:
- A `spy` is a mock of a method.

#### spyOn()

- Is equal to mocking in Java.
- Is intended for synchronous tests.
- Can be called only on existing methods.
    - In unit testing, while testing a particular method/function we may not always want
      to call through all the other dependent methods/functions.
- takes two parameters: the first parameter is the name of the object and the second parameter
  is the name of the method to be spied upon.
  It replaces the spied method with a stub, and does not actually execute the real method.
- Syntax:
    - spyOn(obj, methodName) → {Spy}
- Example:
    - https://scriptverse.academy/tutorials/jasmine-spyon.html

#### createSpy()

- Useful when you do not have any function to spy upon or when the call to the original function
  would inflict a lag in time (especially if it involves HTTP requests) or has other dependencies
  which may not be available in the current context.
- Syntax:
  - jasmine.createSpy(name, originalFunction)
  - (static) createSpy(nameopt, originalFnopt) → {Spy}
  - Both parameters are optional.
  - The `originalFunction` will be replaced with a spy returning a fake method.
- Example:
    - https://scriptverse.academy/tutorials/jasmine-createspy-createspyobj.html

#### createSpyObj()
- creates a mock object with multiple spies. 
The created object has the spy methods as its properties, with their respective return values as its values.
- Syntax:
  - jasmine.createSpyObj(baseName, methodNames)
  - (static) createSpyObj(baseNameopt, methodNames, propertyNamesopt) → {Object}
    - Create an object with multiple Spys as its members.
- Example:
  - https://scriptverse.academy/tutorials/jasmine-createspy-createspyobj.html

Note that many of the functions are not so well described.  
Therefore looking for examples and tutorials is essential.

### Jasmine basic unit-test setup

#### Example of a basic function and the corresponding Jasmine unit-test

Example of a function:
```typescript
function helloWorld() {
  return 'Hello world!';
}
```

A corresponding Jasmine test:
```typescript
describe('Hello world', () => { (1)
  it('says hello', () => { (2)
    expect(helloWorld()) (3)
        .toEqual('Hello world!'); (4)
  });
});
```

- The **describe(string, function)** function:
  - defines what we call a Test Suite, a collection of individual Test Specs.
- The **it(string, function)** function:
  - defines an individual Test Spec, this contains one or more Test Expectations.
- The **expect(actual)** expression:
  - is what we call an Expectation. 
  In conjunction with a Matcher it describes an expected piece of behaviour in the application.
- The **matcher(expected)** expression:
  - is what we call a Matcher. 
  It does a boolean comparison with the expected value passed in vs. the actual value passed to the expect function, 
  if they are false the spec fails.

#### Setup and Teardown

- beforeAll
  - This function is called once, before all the specs in a test suite (describe function) are run.
- afterAll
  - This function is called once after all the specs in a test suite are finished.
- beforeEach
  - This function is called before each test specification (it function) is run.
- afterEach
  - This function is called after each test specification is run.

So an alternative for the above mentioned unit-test might look like this:

```typescript
describe('Hello world', () => {

  let expected = "";

  beforeEach(() => {
    expected = "Hello World";
  });

  afterEach(() => {
    expected = "";
  });

  it('says hello', () => {
    expect(helloWorld())
        .toEqual(expected);
  });
});
```

### Jasmine unit-tests in which the entire Angular component is tested

An Angular component will normally consist of:
- html file: ```*.component.html``` 
- test file: ```*.component.spec.ts```
- scss file: ```*.component.scss```
- typescript file containing a controller class: ```*.component.ts```

Since the component will also contain a html part Jasmine unit-tests will also test the html part.  
The identifiers of the html items can be found via the Web Developer Tool.  
Of course it is good practice to use id's to make them unique.

### Example of Jasmine unit-tests for a service and a component

For these examples is used:
- Genka: Angular Unit Test Tutorial - Crash Course for Angular & Jasmine
  - See the references above.

#### constants.ts

```typescript
export const API_URL = 'https://randomuser.me/api/';
```

#### user.service.ts

```typescript
import { API_URL } from './../interfaces/constants';
import { UsersResult } from './../interfaces/users';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getUser(): Observable<UsersResult> {
    return this.httpClient.get<UsersResult>(API_URL);
  }
}
```

#### user.service.spec.ts

```typescript
import {API_URL} from './../interfaces/constants';
import {UsersResult} from './../interfaces/users';

import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {UserService} from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController; // Enables me to expect http calls and handle fake responses.

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a user result', () => {
    const result: UsersResult = {
      results: [
        {
          name: {
            title: 'Mr',
            first: 'Peter',
            last: 'Parker'
          }
        }
      ]
    }

    service.getUser().subscribe(result => {
      expect(result).toBeTruthy();
      expect(result.results).toBeTruthy();
      expect(result.results.length).toEqual(1);
      console.log('result verified');
    });

    /**
     * The following statement will fail if no request has been done up to this point
     * Error message:
     * UserService > should return a user result
     * Error: Expected one matching request for criteria "Match URL: https://randomuser.me/api/", found none.
     *
     * This has been done in method service.getUser().subscribe() above.
     */
    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    // req.flush(result);
    req.flush({
      results: [
        {
          name: {
            title: 'Mr',
            first: 'Peter',
            last: 'Parker'
          }
        }
      ]
    })
    ;
  });
});
```

#### user.component.html

```html
<div id="wrapper" *ngIf="user" class="wrapper">
  <div id="userName" class="item">{{ user.name?.first + ' '  + user.name?.last }}</div>
  <div id="age" class="item">{{ user.dob?.age }}</div>
  <div id="picture" class="item"><img src="{{ user.picture?.thumbnail }}"></div>
</div>
```

#### user.component.ts

```typescript
import { UserService } from './../../services/user.service';
import { User } from './../../interfaces/users';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user!: User

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getUser().toPromise().then( user => {
      this.user = user.results[0];
    });
  }

}
```

#### user.component.spec.ts

````typescript
import { UserService } from './../../services/user.service';
import {ComponentFixture, fakeAsync, flush, TestBed, tick} from '@angular/core/testing';

import { UserComponent } from './user.component';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

/**
 * In order to understand this:
 * - https://dev.to/angular/understanding-async-tests-in-angular-f8n
 * - https://angular.io/api/core/testing
 *
 * fixture.detectChanges() must be called every time the component changes.
 * The result is that the content will be updated.
 * Example:
 * - A button is clicked which causes a text to be changed.
 *  - This can only be tested after calling fixture.detectChanges().
 *
 *  Mocking is implemented by using a spy:
 *  Example:
 *  - const userServiceMock = jasmine.createSpyObj<UserService>(['getUser']);
 *
 */
describe('UserComponent', () => {
  let component: UserComponent;
  // The fixture can be used to call specific html items.
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(async () => {
    // A spy, i.e. a mock is created of the UserService instead of using a real instance of the UserService.
    const userServiceSpy = jasmine.createSpyObj<UserService>(['getUser']);
    // mock user service function
    userServiceSpy.getUser.and.callFake(function () {
      return of({
        results: [
          {
            name: {
              title: 'Mr',
              first: 'Peter',
              last: 'Parker'
            },
            dob: {
              age: 25
            },
            picture: {
              thumbnail: 'test'
            }
          }
        ]
      });
    });

    await TestBed.configureTestingModule({
      declarations: [UserComponent],
      providers: [
        {
          provide: UserService,
          useValue: userServiceSpy // Instead of the real UserService instance a spy is used.
        }
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
    // // Initialise our app component which creates our grid
    // According to https://dev.to/angular/understanding-async-tests-in-angular-f8n
    // The following function should not be executed in a beforeEach() method. This can cause lots of problems.
    // fixture.detectChanges();

  });

  /**
   * In UserComponent the user data is received asynchronously via method ngOnInit().
   * Therefore `fakeAsync()` needs to be used to wrap the test functionality and trigger the fetching of user data.
   * Within `fakeAsync()` we can call the `tick()` function.
   * `tick()` simulates the asynchronous passage of time for the timers in the fakeAsync zone.
   * Without input `tick()` will wait until all asynchronous activities are completed.
   * After `tick()` the change detection is triggered using `fixture.detectChanges()`.
   */
  it('should create', fakeAsync(() => {
    // Initialise our app component which creates our grid
    fixture.detectChanges();
    component.ngOnInit();
    tick();
    // Run detection to update situation.
    fixture.detectChanges();
    flush();
    expect(component).toBeTruthy();

    expect(fixture.debugElement.query(By.css('#wrapper'))).toBeTruthy();

    expect(fixture.debugElement.query(By.css('#userName'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#userName')).nativeNode.textContent).toBe('Peter Parker');

    expect(fixture.debugElement.query(By.css('#age'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#age')).nativeElement.textContent).toBe('25');

    expect(fixture.debugElement.query(By.css('#picture'))).toBeTruthy();
  }));
});
````

### More examples of unit-tests regarding services, components, etc

- https://angular.io/guide/testing-services
- https://angular.io/guide/testing-components-basics
- https://angular.io/guide/testing-components-scenarios
- https://angular.io/guide/testing-attribute-directives
- https://angular.io/guide/testing-pipes
- https://codecraft.tv/courses/angular/unit-testing/overview/
  - Contains lots of detailed information regarding the writing of Jasmine unit-tests.

### More unit-testing items

Study examples of:
- https://angular.io/api/core/testing/
  - TestBed
  - ComponentFixture

----
It is necesssary to:
- Create an application-model, e.g. in Word or in another tool.
- Create an overview of tests (not up to the last detail).

UserComponent -> UserService -> External API

Tests:

UserComponent -> UserService Spy

                  UserService -> External API HTTP Mock

- https://angular.io/api/common/http/testing
- Study:
    - HttpClientModule vs HttpClientTestingModule
    - HttpTestingController
    - @angular/core/testing
        - TestBed
    - user.component.spec.ts: jasmine.createSpyObj<UserService>(['getUser']);
    - await
        - https://dev.to/angular/understanding-async-tests-in-angular-f8n

For the test of UserService:
- https://randomuser.me/api/ is mocked.

For the test of UserComponent.

[back to main](../README.md) |
[previous](./13_Gitlab_BKWI_Getting_Started.md)

