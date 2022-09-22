import {Component, Input} from '@angular/core';
import {Observable} from "rxjs";
import {GebruikerDto} from "../../../../types/project.endpoint.model";

@Component({
  selector: 'app-gebruikers-administratie',
  templateUrl: './gebruikers-administratie-overzicht.component.html',
  styleUrls: ['./gebruikers-administratie-overzicht.component.scss']
})


export class GebruikersAdministratieOverzichtComponent {

  @Input() gebruikersData$!: Observable<GebruikerDto>;
  exportGebruikersURL: string = 'http://127.0.0.1:2020/backend/gebruikers/export';

}
