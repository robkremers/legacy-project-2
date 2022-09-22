import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  GebruikersAdministratieOverzichtComponent
} from './components/gebruikers-overzicht/gebruikers-administratie-overzicht.component';
import {GebruikersDetailComponent} from "./components/gebruikers-detail/gebruikers-detail.component";

const routes: Routes = [
  {path: '', component: GebruikersAdministratieOverzichtComponent},
  {path: 'view/:userId', component: GebruikersDetailComponent}
 ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GebruikersAdministratieRoutingModule {
}
