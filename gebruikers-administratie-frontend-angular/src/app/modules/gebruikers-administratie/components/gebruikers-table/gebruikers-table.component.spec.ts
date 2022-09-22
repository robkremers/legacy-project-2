import {ComponentFixture, fakeAsync, flush, flushMicrotasks, TestBed, tick} from '@angular/core/testing';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {GebruikersTableComponent} from './gebruikers-table.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GebruikersFrontendService} from "../../../../services/gebruikers-administratie-frontend.service";
import {observable, Observable, of, Subject} from "rxjs";
import {IGebruikersTableMessage} from "../../gebruikers-administratie.types";
import {GebruikerDto} from "../../../../types/project.endpoint.model";
import {asyncData} from "../../../../async-observable-helpers";
// import {asyncData} from "rxjs";

describe('GebruikersTableComponent', () => {
  let component: GebruikersTableComponent;
  let fixture: ComponentFixture<GebruikersTableComponent>;
  let gebruikersFrontendService: GebruikersFrontendService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatProgressBarModule],
      declarations: [GebruikersTableComponent],
      providers: [GebruikersFrontendService]
    })
      // Compile components with a templateUrl for the test's NgModule.
      // It is necessary to call this function as fetching urls is asynchronous.
      .compileComponents();

    // Create component and test fixture.
    // Configures and initializes environment for unit testing and provides methods for creating components and services in unit tests.
    // Declarations not present.
    fixture = TestBed.createComponent(GebruikersTableComponent);
    // Get test component from the fixture.
    component = fixture.componentInstance;
    // detect changes explicitly
    fixture.detectChanges();
    // gebruikersFrontendService provided to the TestBed.
    gebruikersFrontendService = TestBed.inject(GebruikersFrontendService);

  });

  it('should create component', () => {
    expect(component).toBeDefined();
  });

  it('should create gebruikersFrontendService', () => {
    expect(gebruikersFrontendService).toBeTruthy();
  });

  it('gebruikersFrontendService should be defined', () => {
    expect(gebruikersFrontendService).toBeDefined();
  });

  // Test whether the next test works partly.
  it('should use GebruikersFrontendService', () => {
    spyOn(gebruikersFrontendService, 'getGebruikers').and.returnValue(new Observable<IGebruikersTableMessage>);
    expect(gebruikersFrontendService.getGebruikers()).toEqual(new Observable<IGebruikersTableMessage>);
  });

  /**
   * I get an error message:
   * Error: NG0100: ExpressionChangedAfterItHasBeenCheckedError: Expression has changed after it was checked.
   * Previous value: 'true'. Current value: 'false'.. Find more at https://angular.io/errors/NG0100
   */
  it('animate should be turned off in case of a response', fakeAsync(() => {
    const gebruikersTableData = {
      data: [
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
        },
        {
          userId: "2",
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
        }
      ]
    };
      expect(component.animate).toBe(true);
      fixture.detectChanges();

      spyOn(gebruikersFrontendService, 'getGebruikers').and.returnValue(asyncData(gebruikersTableData));
      component.ngOnInit();
      // Execute all timers that are still pending.
      fixture.detectChanges();
    flush();
      // tick();

      expect(component.animate).toBe(false); // How to
    })
  );

});

/**
 * Creation of a stub.
 * - https://angular.io/guide/observables
 *  - Return an Observable of type Observable<IGebruikersTableMessage>.
 * To be used like in:
 * - https://stackoverflow.com/questions/60490532/how-to-test-map-and-tap-pipe-from-rxjs-in-angular
 *
 * See also:
 * - https://itnext.io/testing-observables-and-promises-in-angular-ac49575d27b4
 */
export class MockGebruikersFrontendService {

  private gebruikersTableData: IGebruikersTableMessage = {
    data: [
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
      }
    ]
  };

  constructor() {
  }

  getGebruikers(): Observable<IGebruikersTableMessage> {
    // return Observable<IGebruikersTableMessage>.(this.gebruikersTableData);
    return of(this.gebruikersTableData);
  }

}
