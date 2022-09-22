import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HomeComponent} from './home.component';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HttpClient} from "@angular/common/http";

describe('HomeComponent', () => {

  // Mock the httpClient used by the HelloWorldApiService the AppComponent depends upon
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
      ],
      declarations: [
        HomeComponent
      ]
    }).compileComponents();

    // Inject the http service and test controller for each test
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("should render Overzicht gebruikers", () => {
    expectElementWithIdToExistAndHaveExpectedTextContent('OverzichtGebruikersId', 'Gebruikersadministratie Lite')
  });

  function expectElementWithIdToExistAndHaveExpectedTextContent(elementId: string, expectedTextContent: string) {
    let element: HTMLElement = findElementById(elementId)
    expect(element).withContext('Element met id "' + elementId + '" niet gevonden.')
    expect(element.textContent).toEqual(expectedTextContent);
  }

  function findElementById(id: string) : HTMLElement {
      return fixture.debugElement.nativeElement.querySelector('#' + id)
  }

});
