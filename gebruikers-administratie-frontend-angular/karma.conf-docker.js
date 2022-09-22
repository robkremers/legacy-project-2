// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage'),
      require('@angular-devkit/build-angular/plugins/karma'),
      require("karma-sonarqube-reporter")
    ],
    client: {
      jasmine: {
        // you can add configuration options for Jasmine here
        // the possible options are listed at https://jasmine.github.io/api/edge/Configuration.html
        // for example, you can disable the random execution with `random: false`
        // or set a specific seed with `seed: 4321`
      },
      clearContext: false, // leave Jasmine Spec Runner output visible in browser
    },
    jasmineHtmlReporter: {
      suppressAll: true // removes the duplicated traces
    },
    coverageReporter: {
      dir: require('path').join(__dirname, './coverage/gebruikers-administratie-frontend-angular'),
      subdir: '.',
      reporters: [
        { type: 'html' },
        { type: 'text-summary' },
        { type: "lcov" }
      ]
    },
    sonarqubeReporter: {
      basePath: "src/app", // test files folder
      filePattern: "**/*spec.ts", // test files glob pattern
      encoding: "utf-8", // test files encoding
      outputFolder: "reports", // report destination
      legacyMode: false, // report for Sonarqube < 6.2 (disabled)
      reportName: (
          metadata // report name callback, but accepts also a
      ) =>
          // string (file name) to generate a single file
          /**
           * Report metadata array:
           * - metadata[0] = browser name
           * - metadata[1] = browser version
           * - metadata[2] = plataform name
           * - metadata[3] = plataform version
           */
          metadata.concat("xml").join("."),
    },
    reporters: ['progress', 'kjhtml', "sonarqube"],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: false,
    browsers: ['ChromeHeadless', 'BKWIChromeHeadless'],
    singleRun: true,
    // restartOnFileChange: true,
    concurrency: Infinity,
    customLaunchers: {
      BKWIChromeHeadless: {
        base: 'ChromeHeadless',
        flags: ['--no-sandbox'],
      },
    },
  });
};