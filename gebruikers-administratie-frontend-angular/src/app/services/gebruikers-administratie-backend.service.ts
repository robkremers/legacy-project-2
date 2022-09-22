import {Injectable} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {GebruikerDto, PasswordDto, SystemUser} from "../types/project.endpoint.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";


@Injectable({
    providedIn: 'root'
})
export class GebruikersAdministratieBackendService {
  readonly url: string = environment.backend_url;

  /**
   * @param httpClient {HttpClient}
   */
  constructor(private httpClient: HttpClient) {
  }

  /**
   * @return {Observable<GebruikerDto[]>}
   */
  getGebruikers(): Observable<GebruikerDto[]> {
    const url = this.url + '/gebruikers';
    return this.httpClient.get<GebruikerDto[]>(`${url}`)
        .pipe(
          catchError((err) => this.catchHttpError(err))
        );
  }

  getGebruiker(uid : string): Observable<GebruikerDto> {
    const url = this.url + '/gebruikers/' + uid;
    return this.httpClient.get<GebruikerDto>(`${url}`)
      .pipe(
        catchError((err) => this.catchHttpError(err))
      );
  }

  getCurrentSystemUser(): Observable<SystemUser> {
    const url = this.url + '/system-user';
    return this.httpClient.get<SystemUser>(`${url}`)
        .pipe(
          catchError((err) => this.catchHttpError(err))
        )
  }

  catchHttpError(error: any): Observable<any> {
    console.log('error caught in service')
    console.error(error);
    return throwError(() => error);
  }

  postWijzigWachtwoord(uid: string, wachtwoord: string): Observable<void> {
    const url = this.url + '/gebruikers/' + uid + '/password';
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    };

    const password: PasswordDto = {password: wachtwoord};

    return this.httpClient.put<void>(url, password, httpOptions)
      .pipe(
          catchError((err) => this.catchHttpError(err))
      );
  }

  getGegenereerdWachtwoord(uid: string): Observable<PasswordDto> {
    const url = this.url + '/gebruikers/' + uid + '/generated_password';
    const httpOptions = {
      headers: new HttpHeaders({
          'Content-Type': 'application/json',
      })
    };

    return this.httpClient.get<PasswordDto>(url, httpOptions)
    .pipe(
          catchError((err) => this.catchHttpError(err))
      );
  }
}
