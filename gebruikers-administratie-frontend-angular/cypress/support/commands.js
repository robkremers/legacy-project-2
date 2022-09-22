// TODO: Replace this 'login' command when login functionality is added to Gebruikers Administratie
Cypress.Commands.add('login', (gebruikersNaam) => {
  var am_uid;
  var am_dn;
  
  if (gebruikersNaam == 'BKWI Beheerder') {
    am_uid = 'beheerder_bkwi'
    am_dn = 'cn=BKWI Beheerder,ou=BKWI,o=suwi,c=nl'
  }

  cy.intercept("*", (req) => {
    req.headers['am_uid'] = am_uid
    req.headers['am_dn'] = am_dn
  })
})
