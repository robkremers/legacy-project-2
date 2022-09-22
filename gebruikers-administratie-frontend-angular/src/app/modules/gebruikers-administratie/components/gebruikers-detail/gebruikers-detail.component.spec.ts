import { ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import {GebruikersDetailComponent} from './gebruikers-detail.component';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import { BkwiPanelComponent } from '../bkwi-panel/bkwi-panel.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';

describe('GebruikersDetailComponent', () => {
  let component: GebruikersDetailComponent;
  let fixture: ComponentFixture<GebruikersDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      declarations: [ GebruikersDetailComponent, BkwiPanelComponent ],
       imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatProgressBarModule
      ]
    })
    .compileComponents();
  });

  it(`should have a button 'Genereer wachtwoord'`, () => {
    fixture = TestBed.createComponent(GebruikersDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const addToFavoriteBtnEl = fixture.debugElement.query(By.css('#sla-wachtwoord-op'));
    expect(addToFavoriteBtnEl).toBeDefined();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Genereer wachtwoord');
    
  });

  it(`should be possible to click 'Genereer wachtwoord'`, waitForAsync(() => {
    fixture = TestBed.createComponent(GebruikersDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const addToFavoriteBtnEl = fixture.debugElement.query(By.css('#genereer-wachtwoord'));
    expect(addToFavoriteBtnEl).toBeDefined();
    addToFavoriteBtnEl.triggerEventHandler('click', null);
  }))
 
  it(`There should be 2 password fields zijn and button 'Sla wachtwoord op'`, async() => {
    fixture = TestBed.createComponent(GebruikersDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    const debug = fixture.debugElement as DebugElement;
    const ww = debug.query(By.css('#wachtwoord-id'));
    expect(ww).toBeTruthy();
    const bww = debug.query(By.css('#bevestig-wachtwoord-id'));
    expect(bww).toBeTruthy();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Sla wachtwoord op');

    const compiledDebugNative = fixture.debugElement.nativeElement;
    expect(compiledDebugNative.querySelector('#sla-wachtwoord-op').disable).toBeFalsy();

    let password1 = fixture.debugElement.query(By.css('#wachtwoord-id'));
    password1.nativeElement.value = 'a';
    password1.nativeElement.dispatchEvent(new Event('input'));
    expect(password1.nativeElement.value).toBe('a');

    let password2 = fixture.debugElement.query(By.css('#bevestig-wachtwoord-id'));
    password2.nativeElement.value = 'a';
    password2.nativeElement.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    expect(password2.nativeElement.value).toBe('a');

    const addToFavoriteBtnEl = fixture.debugElement.query(By.css('#sla-wachtwoord-op'));
    expect(addToFavoriteBtnEl).toBeDefined();
    addToFavoriteBtnEl.triggerEventHandler('click', null);
  });

  it(`The entered password should be saved`, waitForAsync(() => {
    fixture = TestBed.createComponent(GebruikersDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();

    let password1 = fixture.debugElement.query(By.css('#wachtwoord-id'));
    password1.nativeElement.value = 'a';
    password1.nativeElement.dispatchEvent(new Event('input'));
    expect(password1.nativeElement.value).toBe('a');

    let password2 = fixture.debugElement.query(By.css('#bevestig-wachtwoord-id'));
    password2.nativeElement.value = 'a';
    password2.nativeElement.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    expect(password2.nativeElement.value).toBe('a');

    const addToFavoriteBtnEl = fixture.debugElement.query(By.css('#sla-wachtwoord-op'));
    expect(addToFavoriteBtnEl).toBeDefined();
    addToFavoriteBtnEl.triggerEventHandler('click', null);
  }));
  
});
