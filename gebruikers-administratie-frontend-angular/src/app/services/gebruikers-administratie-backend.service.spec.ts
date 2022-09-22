import {waitForAsync} from '@angular/core/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { asyncData, asyncError } from '../async-observable-helpers';
import { GebruikerDto, PasswordDto } from "../types/project.endpoint.model";
import { GebruikersAdministratieBackendService } from './gebruikers-administratie-backend.service';

let httpClientSpy: jasmine.SpyObj<HttpClient>;
let serviceToTest: GebruikersAdministratieBackendService;

beforeEach(() => {
  httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'put']);
  serviceToTest = new GebruikersAdministratieBackendService(httpClientSpy);
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

  httpClientSpy.get.and.returnValue(asyncData(expectedGebruikers));

  serviceToTest.getGebruikers().subscribe({
    next: gebruikers => {
      expect(gebruikers)
        .withContext('expected gebruikers')
        .toEqual(expectedGebruikers);
      done();
    },
    error: done.fail
  });
  expect(httpClientSpy.get.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('getGebruikers should raise error (HttpClient called once) when admin user is unauthorized', (done: DoneFn) => {
  const errorResponse = new HttpErrorResponse({
       status: 401,
       statusText: 'Unauthorized',
    });

  httpClientSpy.get.and.returnValue(asyncError(errorResponse));

  serviceToTest.getGebruikers().subscribe({
    next: () => done.fail('expected an error, not gebruiker'),
    error: error => {
      expect(error.message)
      .withContext("unauthorized")
      .toContain("401 Unauthorized");
      done();
    }
  });
  expect(httpClientSpy.get.calls.count())
    .withContext('one call')
    .toBe(1);
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

  httpClientSpy.get.and.returnValue(asyncData(expectedGebruiker));

  serviceToTest.getGebruiker("1").subscribe({
    next: gebruiker => {
      expect(gebruiker)
        .withContext('expected gebruiker')
        .toEqual(expectedGebruiker);
      done();
    },
    error: done.fail
  });
  expect(httpClientSpy.get.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('getGebruiker should raise error (HttpClient called once) when admin user is unauthorized', (done: DoneFn) => {
  const errorResponse = new HttpErrorResponse({
       status: 401,
       statusText: 'Unauthorized',
    });

  httpClientSpy.get.and.returnValue(asyncError(errorResponse));

  serviceToTest.getGebruiker("1").subscribe({
    next: gebruiker => done.fail('expected an error, not gebruiker'),
    error: error => {
      expect(error.message)
      .withContext("unauthorized")
      .toContain("401 Unauthorized");
      done();
    }
  });
  expect(httpClientSpy.get.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('store a manually made password', waitForAsync(() => {

  httpClientSpy.put.and.returnValue(asyncData({value: []}));

  serviceToTest.postWijzigWachtwoord("testNewPasswordUID", "newPassword").subscribe({});
  
  expect(httpClientSpy.put.calls.count())
    .withContext('one call')
    .toBe(1);
}));

it('should return expected password', (done: DoneFn) => {
  const expectedPassword: PasswordDto =
    {
      password: "geheim",
    };

  httpClientSpy.get.and.returnValue(asyncData(expectedPassword));

  serviceToTest.getGegenereerdWachtwoord("unittestuser").subscribe({
    next: password => {
      expect(password)
        .withContext('expected password')
        .toEqual(expectedPassword);
      done();
    },
    error: done.fail
  });
  expect(httpClientSpy.get.calls.count())
    .withContext('one call')
    .toBe(1);
});

it('should raise error during password generation when admin user is unauthorized', (done: DoneFn) => {
  const errorResponse = new HttpErrorResponse({
       status: 401,
       statusText: 'Unauthorized',
    });

  httpClientSpy.get.and.returnValue(asyncError(errorResponse));

  serviceToTest.getGegenereerdWachtwoord("unittestuser").subscribe({
    next: () => done.fail('expected an error, not a password'),
    error: error => {
      expect(error.message)
      .withContext("unauthorized")
      .toContain("401 Unauthorized");
      done();
    }
  });
  expect(httpClientSpy.get.calls.count())
    .withContext('one call')
    .toBe(1);
});
