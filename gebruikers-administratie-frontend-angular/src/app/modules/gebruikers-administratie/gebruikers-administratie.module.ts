import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {GebruikersAdministratieRoutingModule} from './gebruikers-administratie-routing.module';
import {
    GebruikersAdministratieOverzichtComponent
} from './components/gebruikers-overzicht/gebruikers-administratie-overzicht.component';
import {MatTableModule} from "@angular/material/table";
import {GebruikersTableComponent} from "./components/gebruikers-table/gebruikers-table.component";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import { BkwiPanelComponent } from './components/bkwi-panel/bkwi-panel.component';
import {MatIconModule} from "@angular/material/icon";
import { GebruikersDetailComponent } from './components/gebruikers-detail/gebruikers-detail.component';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';



@NgModule({
    declarations: [
        GebruikersAdministratieOverzichtComponent,
        GebruikersTableComponent,
        BkwiPanelComponent,
        GebruikersDetailComponent
    ],
    exports: [
        GebruikersAdministratieOverzichtComponent,
        GebruikersTableComponent,
    ],
    imports: [
        FormsModule,
        ReactiveFormsModule,
        CommonModule,
        GebruikersAdministratieRoutingModule,
        MatTableModule,
        MatProgressBarModule,
        MatIconModule,
    ]
})
export class GebruikersAdministratieModule {
}
