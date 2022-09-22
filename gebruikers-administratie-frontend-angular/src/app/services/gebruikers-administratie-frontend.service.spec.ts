import {waitForAsync} from '@angular/core/testing';
// import { doesNotReject } from 'assert';
import {of, throwError} from "rxjs";
import {GebruikerDto, PasswordDto, SystemUser} from "../types/project.endpoint.model";
import {GebruikersAdministratieBackendService} from './gebruikers-administratie-backend.service';
import {GebruikersFrontendService} from './gebruikers-administratie-frontend.service';

let gebruikersAdministratieBackendServiceSpy: jasmine.SpyObj<GebruikersAdministratieBackendService>;
let serviceToTest: GebruikersFrontendService;

beforeEach(() => {
  gebruikersAdministratieBackendServiceSpy = jasmine.createSpyObj('GebruikersAdministratieBackendService',
          ['getGebruikers', 'getGebruiker', 'getCurrentSystemUser', 'postWijzigWachtwoord', 'getGegenereerdWachtwoord']);
  serviceToTest = new GebruikersFrontendService(gebruikersAdministratieBackendServiceSpy);
});

it('should be created', () => {
  expect(serviceToTest).toBeTruthy();
});

it('should return expected gebruiker (HttpClient called once)', (done: DoneFn) => {

  const expectedGebruiker: GebruikerDto =
      {
        userId: "1",
        naam: 'A',
        achternaam: 'A',
        email: 'A',
        afdeling: 'B',
        rollen: [
          {id: 'A'}
        ],
        voornaam: 'A',
        inlognaam: 'A',
        tijdVanlaatsteInlog: 'A',
        employeeNr: '1',
        telefoonnummer: '020-1234567'
      };

  const expectedGebruikerMessage = {
    data: expectedGebruiker
  }

  gebruikersAdministratieBackendServiceSpy.getGebruiker.and.returnValue(of(expectedGebruiker));

  serviceToTest.getGebruiker("1").subscribe({
    next: gebruiker => {
        expect(gebruiker)
      .withContext('expected gebruiker')
      .toEqual(expectedGebruikerMessage);
      done();
      },
      error: () => done.fail('A gebruiker should be returned, not an error')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getGebruiker.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('should return error (HttpClient called once)', (done: DoneFn) => {

  const expectedGebruikerMessage = {
      errorMessage : 'Error: Er is iets fout gegaan'
  };

  gebruikersAdministratieBackendServiceSpy.getGebruiker.and.returnValue(throwError(() => 'Er is iets fout gegaan'));

  serviceToTest.getGebruiker("1").subscribe({
    next: gebruikerMessage => {
      expect(gebruikerMessage.error)
      .withContext('expected error')
      .toEqual(expectedGebruikerMessage);
      done();
    },
    error: () => done.fail('An error message should be returned, not an error raised')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getGebruiker.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('should return expected gebruikers (HttpClient called once)', (done: DoneFn) => {

  const expectedGebruikers: GebruikerDto[] =
    [
      {
        userId: "1",
        naam: 'A',
        achternaam: 'A',
        email: 'A',
        afdeling: 'B',
        rollen: [
          { id: 'A' }
        ],
        voornaam: 'A',
        inlognaam: 'A',
        tijdVanlaatsteInlog: 'A',
        employeeNr: '1',
        telefoonnummer: '020-1234567'
      },
      {
        userId: "2",
        naam: 'B',
        achternaam: 'B',
        email: 'B',
        afdeling: 'B',
        rollen: [
          {id: 'B'}
        ],
        voornaam: 'B',
        inlognaam: 'B',
        tijdVanlaatsteInlog: 'B',
        employeeNr: '2',
        telefoonnummer: '020-1234567'
      }
    ];

  const expectedGebruikerMessage = {
    data: expectedGebruikers
  }

  gebruikersAdministratieBackendServiceSpy.getGebruikers.and.returnValue(of(expectedGebruikers));

  serviceToTest.getGebruikers().subscribe({
    next: gebruiker => {
        expect(gebruiker)
      .withContext('expected gebruiker')
      .toEqual(expectedGebruikerMessage);
      done();
      },
      error: () => done.fail('Gebruikers should be returned, not an error')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getGebruikers.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('return error calling getGebruikers (HttpClient called once)', (done: DoneFn) => {

  const expectedGebruikersMessage = {
      errorMessage : 'Error: Er is iets fout gegaan'
  };

  gebruikersAdministratieBackendServiceSpy.getGebruikers.and.returnValue(throwError(() => 'Er is iets fout gegaan'));

  serviceToTest.getGebruikers().subscribe({
    next: gebruikerMessage => {
      expect(gebruikerMessage.error)
      .withContext('expected error')
      .toEqual(expectedGebruikersMessage);
      done();
    },
    error: () => done.fail('An error message should be returned, not an error raised')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getGebruikers.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('should return expected SystemUser', (done: DoneFn) => {

  const expectedSystemUser: SystemUser =
      {
        naam: 'UnitTestSystemUser'
      };

  gebruikersAdministratieBackendServiceSpy.getCurrentSystemUser.and.returnValue(of(expectedSystemUser));

  serviceToTest.getCurrentSystemUser().subscribe({
    next: systemuser => {
        expect(systemuser)
      .withContext('expected systemuser')
      .toEqual(expectedSystemUser);
      done();
      },
      error: () => done.fail('An system user should be returned, not an error raised')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getCurrentSystemUser.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('should be possible to change password', (done: DoneFn) =>{

  const expectedPasswordMessage = {
    info: {
      infoGenerateMessage: '', 
      infoMessage: 'Wachtwoord is bijgewerkt'
    }
  }

  gebruikersAdministratieBackendServiceSpy.postWijzigWachtwoord.withArgs("testNewPasswordUID", "newPassword").and.returnValue(of(void 0));

  serviceToTest.postWijzigWachtwoord("testNewPasswordUID", "newPassword").subscribe({
    next: info => {
        expect(info)
      .withContext('change password')
      .toEqual(expectedPasswordMessage);
      done();
    },
    error: () => done.fail('A success message is expected not an error')
  });

  expect(gebruikersAdministratieBackendServiceSpy.postWijzigWachtwoord.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('return error changing password (HttpClient called once)', (done: DoneFn) => {

  const expectedGebruikersMessage = {
      errorMessage : 'Error: Bij het opslaan van het wachtwoord is een fout opgetreden'
  };

  gebruikersAdministratieBackendServiceSpy.postWijzigWachtwoord.and.returnValue(throwError(() => 'Bij het opslaan van het wachtwoord is een fout opgetreden'));

  serviceToTest.postWijzigWachtwoord("testNewPasswordUID", "newPassword").subscribe({
    next: gebruikerMessage => {
      expect(gebruikerMessage.error)
      .withContext('expected error')
      .toEqual(expectedGebruikersMessage);
      done();
    },
    error: () => done.fail('An error message should be returned, not an error raised')
  });

  expect(gebruikersAdministratieBackendServiceSpy.postWijzigWachtwoord.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('should possible to generate a password', (done: DoneFn) => {

  const expectedPassword: PasswordDto =
      {
        password: 'unitTestGeneratedPasswordUID',
      };

  const expectedPasswordMessage = {
    passwordDto: expectedPassword,
    info: {
      infoGenerateMessage: 'Het genereerde wachtwoord is opgeslagen', 
      infoMessage: ''
    }
  }

  gebruikersAdministratieBackendServiceSpy.getGegenereerdWachtwoord.and.returnValue(of(expectedPassword));

  serviceToTest.getGegenereerdWachtwoord("unitTestGeneratedPasswordUID").subscribe({
    next: password => {
        expect(password)
      .withContext('expected password')
      .toEqual(expectedPasswordMessage);
      done();
    },
    error: () => done.fail('A password and success message is expected not an error')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getGegenereerdWachtwoord.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('return error generating new password (HttpClient called once)', (done: DoneFn) => {

  const expectedGebruikersMessage = {
      errorMessage : 'Error: Bij het genereren van het wachtwoord is een fout opgetreden'
  };

  gebruikersAdministratieBackendServiceSpy.getGegenereerdWachtwoord.and.returnValue(throwError( () => 'Bij het genereren van het wachtwoord is een fout opgetreden'));

  serviceToTest.getGegenereerdWachtwoord("unitTestGeneratedPasswordUID").subscribe({
    next: gebruikerMessage => {
      expect(gebruikerMessage.error)
      .withContext('expected error')
      .toEqual(expectedGebruikersMessage);
      done();
    },
    error: () => done.fail('An error message should be returned, not an error raised')
  });

  expect(gebruikersAdministratieBackendServiceSpy.getGegenereerdWachtwoord.calls.count())
    .withContext('one call')
    .toBe(1);
});