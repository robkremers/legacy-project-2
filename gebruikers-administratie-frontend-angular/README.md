# GebruikersAdministratieFrontendAngular

[[_TOC_]]

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 13.0.1.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running Cypress tests

```bash
npx cypress run
```

Voor het uitvoeren van de Cypress testen

Zie het [CypressTesten.md](cypress/CypressTesten.md) document voor meer informatie. 

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Added libraries

### Angular Material library

- [Angular Material library](https://material.angular.io/)
  - Installatie: 
  
```shell
$ cd $ws/gebruikers-administratie-frontend-angular
$ ng add @angular/material
```
### sas-loader t.b.v. omzetten van scss naar css.

Nodig: css heeft geen variabelen. scss wel.

- http://man.hubwiz.com/docset/webpack.docset/Contents/Resources/Documents/loaders-sass-loader.html
```shell
$ cd $ws/gebruikers-administratie-frontend-angular
$npm install sass-loader node-sass webpack --save-dev
```

## Styling

Voor styling zal gekeken worden naar de opzet van Nikita:
- https://office.bkwi.nl/
  - Openbare bestanden / Algemeen / Nikita / opleveren / Suwinet_inkijk / Prototype

Verder zal gebruik gemaakt worden van:
- Angular Material
- gebruikers-administratie-frontend-angular/src/assets/bootstrap/src/main/webapp/bootstrap/css/bootstrap.css
- https://www.developerhandbook.com/webpack/how-to-configure-scss-modules-for-webpack/
  - Nodig om de scss files, met variabelen, om te zetten naar css files met hard-codes waarden. 
- http://man.hubwiz.com/docset/webpack.docset/Contents/Resources/Documents/loaders-sass-loader.html
