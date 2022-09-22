import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { BkwiPanelComponent } from '../bkwi-panel/bkwi-panel.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GebruikersAdministratieOverzichtComponent } from './gebruikers-administratie-overzicht.component';

describe('GebruikersAdministratieComponent', () => {
  let component: GebruikersAdministratieOverzichtComponent;
  let fixture: ComponentFixture<GebruikersAdministratieOverzichtComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GebruikersAdministratieOverzichtComponent, BkwiPanelComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GebruikersAdministratieOverzichtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should created fully', () => {
    expect(component).toBeTruthy();
    expect(component.exportGebruikersURL).toBeTruthy();
    const debug = fixture.debugElement as DebugElement;
    const button = debug.query(By.css('#ExportGebruikersButtonId'));
    expect(button).toBeTruthy();
  });
});
