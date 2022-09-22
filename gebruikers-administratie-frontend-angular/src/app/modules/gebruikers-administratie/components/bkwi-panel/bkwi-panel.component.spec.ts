import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BkwiPanelComponent } from './bkwi-panel.component';

describe('BkwiPanelComponent', () => {
  let component: BkwiPanelComponent;
  let fixture: ComponentFixture<BkwiPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BkwiPanelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BkwiPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
