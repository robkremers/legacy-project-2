const report = require('multiple-cucumber-html-reporter');

report.generate({
	jsonDir: './testReports/cucumber_report/cucumber-json/',
	reportPath: './testReports/cucumber_report/',

    reportName : '<center><p><h1>Testresultaten BDD-testen Gebruikers-Adminstratie</h1></p></center>',
    pageTitle : 'Testresultaten BDD-testen',
    pageFooter : '<center><p><strong>Gebruikers-Adminstratie</strong></p></center>',
    displayDuration: true,
    displayReportTime: true
	
});