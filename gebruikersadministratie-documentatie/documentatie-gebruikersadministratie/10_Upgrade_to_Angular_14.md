# Beschrijving van de upgrade van Angular 13 naar Angular 14.

## Introductie

Angular wordt gebruikt voor de frontend applicatie:
- gebruikers-administratie-frontend-angular

Omdat nu een nieuwe main version geïntroduceerd is is besloten om de Gitlab bouwstraat voor Angular te upgraden
naar de laatste versie.  
En dus moet onze applicatie eveneens ge-upgrade worden.

In het volgende wordt in het kort beschreven:
- Op welke manier is de applicatie ge-update.
- Hoe kan lokaal de omgeving ge-update worden.

Voor de duidelijkheid:
De versie van een gegeven project kan anders zijn dan de versie van de lokale omgeving.

Voor de update is al leidraad gebruikt:

https://update.angular.io/?l=2&v=13.0-14.0
- I use Angular Material

## Update van de lokale omgeving.

Een overzicht van lokale versies van relevante tools in mijn lokale omgeving. Dit is up-to-date, maar
kan voor anderen verschillen. Indien nodig updaten.

```shell
Lokale versies:
$ node --version
v16.13.1
$ npm --version
8.1.2
$ tsc --version
Version 4.7.3
```
Noot:
- TypeScript moet volgens de ene bron versie 4.7 hebben en volgens een andere bron 4.6.
- Bij install kan het zijn, dat toch versie 4.6 gevraagd wordt. Daarom deze indien nodig installeren.

```shell
# Voer het volgende uit in een algemene omgeving. B.v. in de parent-directory van het angular project.
$ ng update @angular/core@14 @angular/cli@14 --force
$ ng update @angular/material@14
```

## Update van het project gebruikers-administratie-frontend-angular

In het project [gebruikers-administratie-frontend-angular](https://gitlab.bkwi.nl/bp/pm/ba/services-inkijk/gebruikers-administratie-frontend-angular)
is lokaal het volgende uitgevoerd:

```shell
$ git pull
$ git checkout -b "AEB-1020"
$ rm -rf node_modules
$ ng update @angular/core@14 @angular/cli@14
$ ng update @angular/material@14

# Vervolgens in het project package.json aanpassen:
  "devDependencies": {
    "@angular-devkit/build-angular": "^14.0.0",

# In het project heb ik package-lock.json verwijderd.
# Na de installatie van de nieuwe functionaliteit wordt file package-lock.json weer aangemaakt.
$ npm install
# Opstarten om te controleren of alles lokaal werkt.
$ ng serve --port 4200
```

## Update van het project gebruikers-administratie-frontend-angular bij anderen:

Nadat de commit AEB-1020 / AEB-1550: migrate to Angular 14 gemerged is moet het volgende uitgevoerd worden in
het project gebruikers-administratie-frontend-angular.

```shell
# Ga naar het project gebruikers-administratie-frontend-angular
$ git checkout master
$ git pull
# De directory node-modules zit in root file .gitignore en een wijziging hierin wordt dus niet meegenomen.
$ rm -rf node_modules
# Op basis van package.json wordt vervolgens het project opnieuw opgebouwd.
$ npm install
# Opstarten om te controleren of alles lokaal werkt.
$ ng serve --port 4200
```

De applicatie werkt lokaal en is in de Gitlab buildstraat succesvol deployed.

[back to main](../README.md) |
[previous](./8_TechnischOntwerp.md) |
[next](./11_Uitleg_Functionele_en_Technische_Werking_GA_Frontend.md)