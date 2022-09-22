import {When, And, Given, Then } from "cypress-cucumber-preprocessor/steps";
import { validateCsvFile, deleteDownloadsFolder } from '../common/utils'
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

Then ('is het aantal kolommen van het gebruikersoverzicht {int}', (aantal_kolommen) => {
  cy.get('#gebruikerstabelId>tr>th')
    .should('have.length', aantal_kolommen)
})

And ('worden er {int} gebruikers weergegeven in het gebruikersoverzicht', (aantal_gebruikers) => {
  cy.get('#gebruikerstabelId>tr')
    .should('have.length', aantal_gebruikers)
})

And ('zijn de headers van het gebruikersoverzicht als volgt' , (dataTable) => {
  let expected_header_list = dataTable.raw()[0]
  cy.get('#gebruikerstabelId>tr>th')
    .each(($header, i) => {
      expect($header.text()).to.equal(expected_header_list[i]);
    })
})

And ('de {string} knop is zichtbaar', (knopText) => {
  cy.get('#ExportGebruikersButtonId')
    .should('be.visible')
    .contains(knopText)
})

When ('als ik de {string} knop indruk wordt het gebruikersoverzicht gedownload', (knopText) => {
  // Remove any downloads before starting this test
  deleteDownloadsFolder()

  cy.get('#ExportGebruikersButtonId')
    .click({ force: true })

  // Zet de huidige tijd en datum, die ook gebruikt worden in het csv-bestand
  let date = new Date()

  // Get the current locale time in HH:MM format and remove the ':' by replacing it
  let currentTime = date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}).replace(':', '')

  // Get the current DateTime in ISO 8601 format, and split off the relevant YYYY-MM-DD, then remove the '-'
  let currentDate = date.toISOString()
    .split('T')[0]
    .replace(/-/g, '')

  // Validate the downloaded csv-file
  validateCsvFile('export-gebruikers-' + currentDate + '-' + currentTime + '.csv')
})
