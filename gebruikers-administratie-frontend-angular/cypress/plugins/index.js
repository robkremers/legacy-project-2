/// <reference types="cypress" />

// This function is called when a project is opened or re-opened (e.g. due to
// the project's config changing)

const { rmdir } = require('fs')
const fs = require('fs-extra')
const path = require('path')
const cucumber = require('cypress-cucumber-preprocessor').default

function getConfigurationByFile(file) {
  const pathToConfigFile = path.resolve('cypress', 'config', `${file}.json`)
  console.log(pathToConfigFile)
  return fs.readJson(pathToConfigFile)
}

module.exports = (on, config) => {
  // `on` is used to hook into various events Cypress emits
  // `config` is the resolved Cypress config
  on('file:preprocessor', cucumber())

  // Set and get Page Url
  on('task', {
    setPageUrl: (val) => { pageUrl = val; return null },
    getPageUrl: () => { return pageUrl; }
  })

  on('task', {
    setPasswordHash: (val) => { passwordHash = val; return null },
    getPasswordHash: () => { return passwordHash; }
  })


  // deleteFolder task
  // Used for deleting the ./downloads/ folder before running the csv-test in gebruikersoverzicht.js
  on('task', {
    deleteFolder(folderName) {
      console.log('deleting folder %s', folderName)

      return new Promise((resolve, reject) => {
        rmdir(folderName, { maxRetries: 10, recursive: true }, (err) => {
          if (err && err.code !== 'ENOENT') {
            console.error(err)

            return reject(err)
          }

          resolve(null)
        })
      })
    },
  })

  // Read config file and set config settings
  const file = 'default'
  return getConfigurationByFile(file)
}
