import {Component, OnInit} from '@angular/core';
import {GebruikersFrontendService} from "../../../../services/gebruikers-administratie-frontend.service";
import {Observable, of, tap} from "rxjs";
import {IGebruikerMessage,
    IUpdatePasswordMessage,
    IUpdateGeneratePasswordMessage} from "../../gebruikers-administratie.types";
import {ActivatedRoute, Params} from "@angular/router";
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {WachtwoordValidators} from './wachtwoord-validators';

@Component({
    selector: 'app-gebruikers-detail',
    templateUrl: './gebruikers-detail.component.html',
    styleUrls: ['./gebruikers-detail.component.scss']
})
export class GebruikersDetailComponent implements OnInit {
    private userId: string = "";
    public animate = true;
    public templateData$!: Observable<IGebruikerMessage>;
    public updatePasswordMessage$: Observable<IUpdatePasswordMessage>;
    public updateGeneratePasswordMessage$: Observable<IUpdateGeneratePasswordMessage>;
    public frmWijzigWachtwoord: FormGroup;
    public frmGenereerWachtwoord: FormGroup;
    public laatsteActieWasWachtwoordGeneratie: boolean = false;
 
    constructor(
        private route: ActivatedRoute,
        private gebruikersFrontendService: GebruikersFrontendService,
        private fb: FormBuilder) {

        this.frmWijzigWachtwoord = this.wijzigWachtwoordForm();
        this.frmGenereerWachtwoord = this.wijzigWachtwoordForm();

        this.updatePasswordMessage$ = of({}); // No info or error
        this.updateGeneratePasswordMessage$ = of({}); // No info or error
    }

    ngOnInit(): void {
        this.route.params.subscribe(
            (params: Params) => {
                this.userId = params['userId'];
                this.templateData$ = this.gebruikersFrontendService.getGebruiker(this.userId).pipe(
                    tap(() => {
                        this.animate = false
                    })
                );
            }
        )
    }

    wijzigWachtwoordForm(): FormGroup {
        return this.fb.group(
            {
                wachtwoord: [null, Validators.compose([Validators.required])],
                bevestigWachtwoord: [null, Validators.compose([Validators.required])]
            }
            ,
            {
                validators: [WachtwoordValidators.wachtwoordValidator]
            }
        );
    }

    submit() {
        this.animate = true;
        this.updatePasswordMessage$ = this.gebruikersFrontendService.postWijzigWachtwoord(this.userId, this.frmWijzigWachtwoord.value.wachtwoord).pipe(
                 tap(() => {
                this.animate = false;
            })
        );
        this.laatsteActieWasWachtwoordGeneratie = false;
    }

    generatePassword(): void {
        this.animate = true;
        this.frmWijzigWachtwoord.setValue({
            wachtwoord: "",
            bevestigWachtwoord: ""
        })
        this.laatsteActieWasWachtwoordGeneratie = true;
        this.updateGeneratePasswordMessage$ = this.gebruikersFrontendService.getGegenereerdWachtwoord(this.userId).pipe(
                tap(() => {
                this.animate = false;
            })
        );
    }
}
