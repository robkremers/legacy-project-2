import {When, And, Given, Then } from "cypress-cucumber-preprocessor/steps";
import HomePagina from "../common/homePagina";


Given ('dat de landingspagina is geopend en de {string} is ingelogd', (beheerder) => {
  HomePagina.visitHomePagina(beheerder)
})

When ('de gebruikersoverzicht-pagina wordt geopend', () => {
  cy.get('#OverzichtGebruikersId').click()
  cy.get('#gebruikerspanelTitleId')
    .contains('Zoek-/bladerresultaat')
    .should('be.visible')
})

And ('er wordt op de details-knop van de gebruiker {string} geklikt', (gebruikersnaam) => {
  cy.contains('td', gebruikersnaam)
    .parents('tr')
    .find('a > .mat-icon').click()
})

Then ('wordt de details-pagina van de gebruiker {string} weergegeven', (gebruikersnaam) => {
  cy.get('.panel-heading')
    .contains('Persoonsgegevens')       
})

And ('bevat het veld {string} {string}', (veldnaam, waarde) => {
  cy.get('#' + veldnaam)
    .contains(waarde)
    .should('have.text', waarde)
})
