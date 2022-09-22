import {Component, OnInit} from '@angular/core';
import {Observable, tap} from "rxjs";
import {GebruikersFrontendService} from "../../../../services/gebruikers-administratie-frontend.service";
import {IGebruikersTableMessage} from "../../gebruikers-administratie.types";


@Component({
    selector: 'app-gebruikers-table',
    templateUrl: './gebruikers-table.component.html',
    styleUrls: ['./gebruikers-table.component.scss']
})
export class GebruikersTableComponent implements OnInit {

    public templateData$!: Observable<IGebruikersTableMessage>
    public displayedColumns = ['afdeling', 'naam', 'achternaam', 'voornaam', 'email', 'actions'];

    public animate = true;

    constructor(private gebruikersFrontendService: GebruikersFrontendService) {
    }

    ngOnInit(): void {
        this.templateData$ = this.gebruikersFrontendService.getGebruikers().pipe(
            tap(() => {
                this.animate = false
            })
        );
    }

}
