import {Injectable} from '@angular/core';
import {GebruikersAdministratieBackendService} from "./gebruikers-administratie-backend.service";
import {catchError, delay, map, Observable, of} from "rxjs";
import {
    IGebruikerMessage,
    IGebruikersTableMessage,
    IUpdatePasswordMessage,
    IUpdateGeneratePasswordMessage
} from "../modules/gebruikers-administratie/gebruikers-administratie.types";
import {SystemUser} from '../types/project.endpoint.model';

/**
 * De delay is toegevoegd om de <mat-progress-bar> in gebruikers-table.component.html wat tijd te geven.
 */
@Injectable({
    providedIn: 'root'
})

export class GebruikersFrontendService {

    constructor(private gebruikersAdministratieBackendService: GebruikersAdministratieBackendService) {
    }

    getGebruikers(): Observable<IGebruikersTableMessage> {
        return this.gebruikersAdministratieBackendService.getGebruikers().pipe(
            delay(777),
            map((responseData) => {
                return {
                    data: responseData
                }
            }),
            catchError((err) => {
                console.log('Error:', err)
                return of({
                    error: {
                        errorMessage: `Error: ${err.message || err.toString()}`,
                    }
                })
            })
        )
    }

    getGebruiker(userId: string): Observable<IGebruikerMessage> {
        return this.gebruikersAdministratieBackendService.getGebruiker(userId).pipe(
            map((responseData) => {
                return {
                    data: responseData
                }
            }),
            catchError((err) => {
                return of({
                    error: {
                        errorMessage: `Error: ${err.message || err.toString()}`,
                    }
                })
            })
        )
    }

    getCurrentSystemUser(): Observable<SystemUser> {
        return this.gebruikersAdministratieBackendService.getCurrentSystemUser();
    }


    postWijzigWachtwoord(uid: string, wachtwoord: string): Observable<IUpdatePasswordMessage> {
        /*
            De method gebruikersAdministratieBackendService.postWijzigWachtwoord geeft een Observable van type void
            terug. We willen nu een melding teruggeven naar de gebruiker zowel bij succesvol gewijzigd als bij een
            error. We maken daarom van de Observable<void> een Observable<IUpdatePasswordMessage>. We maken hierbij
            gebruik van de rxjs operator pipe, map en of.
        */
        return this.gebruikersAdministratieBackendService.postWijzigWachtwoord(uid, wachtwoord).pipe(
            map(() => {
                // Als er geen errors zijn, dan maken we een IUpdatePasswordMessage met het info veld gevuld
                return {
                    info: {
                        infoGenerateMessage: "",
                        infoMessage: "Wachtwoord is bijgewerkt"
                    }
                 };
             }),
            catchError((err) => {
                // Als er wel errors zijn, dan maken we een IUpdatePasswordMessage met het error veld gevuld
                return of({
                    error: {
                        errorMessage: "Error: Bij het opslaan van het wachtwoord is een fout opgetreden"
                    }
                })
            })
        )
    }

    getGegenereerdWachtwoord(uid: string): Observable<IUpdateGeneratePasswordMessage> {
        /*
            De method gebruikersAdministratieBackendService.getGegenereerdWachtwoord geeft een Observable van type Password
            terug. We willen een melding teruggeven naar de gebruiker zowel bij succesvol gewijzigd als bij een
            error. We maken daarom van de Observable<Password> een Observable<IUpdateGeneratePasswordMessage>. We maken hierbij
            gebruik van de rxjs operator pipe, map en of.
        */
        return this.gebruikersAdministratieBackendService.getGegenereerdWachtwoord(uid).pipe(
            map((responseData) => {
                // Als er geen errors zijn, dan maken we een IUpdateGeneratePasswordMessage met het info veld gevuld
                return {
                    passwordDto: responseData,
                    info: {
                        infoGenerateMessage: "Het genereerde wachtwoord is opgeslagen",
                        infoMessage: ""
                    }
                };
            }),
            catchError((err) => {
                // Als er wel errors zijn, dan maken we een IUpdateGeneratePasswordMessage met het error veld gevuld
                return of({
                    error: {
                        errorMessage: "Error: Bij het genereren van het wachtwoord is een fout opgetreden"
                    }
                })
            })
        )
    }
}
