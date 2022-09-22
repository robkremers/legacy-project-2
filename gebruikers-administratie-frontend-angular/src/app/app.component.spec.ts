import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HttpClient} from "@angular/common/http";

describe('AppComponent', () => {

  // Mock the httpClient used by the HelloWorldApiService the AppComponent depends upon
  let httpClientSpy: jasmine.SpyObj<HttpClient>;


  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(async () => {

    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get']);


    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
      ],
      declarations: [
        AppComponent
      ]
    }).compileComponents();

    // Inject the http service and test controller for each test
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);

  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'Gebruikers Administratie'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('Gebruikers Administratie');
  });

  it('No text in footer', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelector('footer')?.textContent).toEqual('');
  });
});
