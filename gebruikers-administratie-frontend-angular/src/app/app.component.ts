import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {GebruikersFrontendService} from './services/gebruikers-administratie-frontend.service';
import {SystemUser} from './types/project.endpoint.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {
  title = 'Gebruikers Administratie';

  public currentSystemUser$!: Observable<SystemUser>;

  constructor(private gebruikersService: GebruikersFrontendService) {

  }

  ngOnInit(): void {
    this.currentSystemUser$ = this.gebruikersService.getCurrentSystemUser()
  }


}
