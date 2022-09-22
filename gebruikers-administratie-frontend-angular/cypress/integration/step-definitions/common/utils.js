const fs = require('fs')
const path = require('path')

/// <reference types="cypress" />
const neatCSV = require('neat-csv')

/**
 * @param {string} username Name of the user whose password will be returned
 * @returns String that will be executed by Cypress
 */
export const ldapSearchQueryBuilder = (username) => {
  const ldapSearchQuery = 'docker exec ' + Cypress.env('ldapDockerContainer') +
    ' /opt/opendj/bin/ldapsearch ' +
    ' --port '   + Cypress.env('ldapPort') +
    ' -D '       + Cypress.env('ldapAdminDN') +
    ' -w '       + Cypress.env('ldapAdminPassword') +
    ' --baseDn ' + Cypress.env('ldapUserBaseDN') +
    ' "(uid='    + username + ')"' +
    ' userPassword ' +
    ' | grep "^userPassword" | awk {\'print$2\'}'
  return ldapSearchQuery
}


/**
 * Delete the downloads folder to make sure the test has "clean"
 * slate before starting.
 */
export const deleteDownloadsFolder = () => {
  const downloadsFolder = Cypress.config('downloadsFolder')
  cy.task('deleteFolder', downloadsFolder)
}

/**
 * @param {string} name File name in the downloads folder
 */
export const validateCsvFile = (name) => {
  const downloadsFolder = Cypress.config('downloadsFolder')
  const filename = path.join(downloadsFolder, name)

  cy.readFile(filename, 'utf8').then(validateCsv)
}

/**
 * @param {string} csv
*/
export const validateCsv = (csv) => {
  cy.wrap(csv)
  .then(neatCSV)
  .then(validateCsvList)
}

export const validateCsvList = (list) => {
  expect(list, 'number of records').to.have.length(64)
  // TODO: - Uncomment code below when csv-issue (AEB-1455) has been resolved
  //       - Find actual key-names and values
  // expect(list[0], 'first record').to.deep.equal({
  //   <KEY1>: <VALUE1>,
  //   <KEY2>: <VALUE2>,
  //   etc.
  // })
}


