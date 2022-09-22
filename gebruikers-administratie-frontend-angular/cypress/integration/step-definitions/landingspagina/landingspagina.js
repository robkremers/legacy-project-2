import {When, And, Given, Then, } from "cypress-cucumber-preprocessor/steps"
import HomePagina from "../common/homePagina";

Given ('dat de landingspagina is geopend en de gebruiker {string} is ingelogd', (beheerder) => {
  HomePagina.visitHomePagina(beheerder)
  cy.get('#dropdownMenuLogin')
    .should('be.visible')
    .contains(beheerder)
})

Then ('zijn de volgende knoppen zichtbaar', (buttonTable) => {
  // Voor elke rij pakken we het eerste (en enige) element
  buttonTable.rows().forEach(knop => {
    cy.get('.nav')
      .contains(knop[0])
      .should('be.visible')
  })
})

Then ('is de header zichtbaar met het logo van Suwinet', () => {
  cy.get('.navbar-fixed-top').should('be.visible')
  cy.get('[alt="logo-suwinet"]')
    .should('be.visible')
    .and(($img) => {
      expect($img[0].naturalWidth).to.be.greaterThan(0)
    })
})

And ('is de footer zichtbaar met het logo van BKWI', () => {
  cy.get('#footer').should('be.visible')
  cy.get('[alt="bkwi-logo"]')
    .should('be.visible')
    .and(($img) => {
      expect($img[0].naturalWidth).to.be.greaterThan(0)
    })
})
