import { When, And, Given, Then, } from "cypress-cucumber-preprocessor/steps"
import { ldapSearchQueryBuilder } from "../common/utils"


Given('dat de beheerder {string} is ingelogd en de gebruikersdetail pagina van {string} is geopend', (beheerderString, gebruikersNaamString) => {
    const ldapSearchQuery = ldapSearchQueryBuilder(gebruikersNaamString)
    cy.exec(ldapSearchQuery).then((result) => {
        cy.task('setPasswordHash', result.stdout)
    })

    cy.login(beheerderString)
    cy.task('setPageUrl', (Cypress.env('baseUrl') + Cypress.env('gebruikersViewSuffix') + gebruikersNaamString))
    cy.task('getPageUrl').then((pageUrl) => {
        cy.visit(pageUrl)
        cy.get('#inlognaam-id')
            .contains(gebruikersNaamString)
    })
})

Given('dat de beheerder {string} is ingelogd en de gebruikersoverzichtpagina is geopened', (beheerderString) => {
    cy.login(beheerderString)
    cy.task('setPageUrl', (Cypress.env('baseUrl') + Cypress.env('gebruikersOverzichtSuffix')))
    cy.task('getPageUrl').then((pageUrl) => {
        cy.visit(pageUrl)
    })
})

Then('zijn de gebruikersdetails van {string} zichtbaar', (gebruikersNaamString) => {
    cy.get('#inlognaam-id')
        .contains(gebruikersNaamString)
        .should('have.text', gebruikersNaamString)
})

And('zijn twee wachtwoord-velden zichtbaar', () => {
    cy.get('#wachtwoord-id')
        .should('be.visible')
        .should('be.empty')
    cy.get('#bevestig-wachtwoord-id')
        .should('be.visible')
        .should('be.empty')
})

And('zijn de textvelden rood', () => {
    cy.contains('Nieuw wachtwoord')
        .should('have.class', 'text-danger')
        .should('be.visible')
    cy.contains('Bevestig nieuw wachtwoord')
        .should('have.class', 'text-danger')
        .should('be.visible')
})

And('wordt het wachtwoord {string} in het {string} veld ingevuld', (wachtwoordString, veldNaamString) => {
    cy.get('#' + veldNaamString + '-id')
        .type(wachtwoordString)
})

And('wordt de waarschuwing {string} weergegeven', (waarschuwingString) => {
    cy.contains(waarschuwingString)
        .should('have.class', 'text-danger')
        .should('be.visible')
})

Then('wordt de foutmelding {string} weergegeven', (foutmeldingString) => {
    cy.contains(foutmeldingString)
        .should('be.visible')
        .parent()
        .should('have.class', 'alert-danger')
})

Then('wordt de Opslaan knop niet actief', () => {
    cy.contains('button', 'Opslaan')
        .should('be.visible')
        .should('be.disabled')
})

Then('wordt de Opslaan knop actief', () => {
    cy.contains('button', 'Opslaan')
        .should('be.visible')
        .should('not.be.disabled')
})

And('als op Opslaan wordt geklikt', () => {
    cy.contains('button', 'Opslaan')
        .click()
})

Then('wordt een bevestiging weergegeven', () => {
    cy.contains('Wachtwoord bijgewerkt')
        .should('be.visible')
        .parent()
        .should('have.class', 'alert-info')
})

And('wordt het nieuwe wachtwoord opgeslagen voor de gebruiker {string}', (gebruikersNaamString) => {
    const ldapSearchQuery = ldapSearchQueryBuilder(gebruikersNaamString)
    cy.exec(ldapSearchQuery).then((result) => {
        cy.task('getPasswordHash').then((passwordHash) => {
            expect(passwordHash).not.to.eq(result.stdout)
        })
    })
})

And('wordt terug genavigeerd in de browser', () => {
    cy.go('back')
})

And('wordt het nieuwe wachtwoord niet opgeslagen voor de gebruiker {string}', (gebruikersNaamString) => {
    const ldapSearchQuery = ldapSearchQueryBuilder(gebruikersNaamString)

    // Execute the command and compare the returned password to the saved password
    cy.exec(ldapSearchQuery).then((result) => {
        cy.task('getPasswordHash').then((passwordHash) => {
            expect(passwordHash).to.eq(result.stdout)
        })
    })
})

And('er wordt op de details-knop van de gebruiker {string} geklikt', (gebruikersNaamString) => {
    const ldapSearchQuery = ldapSearchQueryBuilder(gebruikersNaamString)
    cy.exec(ldapSearchQuery).then((result) => {
        cy.task('setPasswordHash', result.stdout)
    })
    cy.contains('td', gebruikersnaam)
        .parents('tr')
        .find('a > .mat-icon').click()
})

