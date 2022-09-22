import { ValidationErrors, AbstractControl } from '@angular/forms';

export class WachtwoordValidators {
  
  static wachtwoordValidator(control: AbstractControl): ValidationErrors | null {
    
    var wachtwoord!: string;
    const first: any = control.get('wachtwoord');
    if (first != null) {
        wachtwoord = first.value;
    }
   
    var bevestigWachtwoord! : string;
    const second: any = control.get('bevestigWachtwoord');
    if (second != null) {
      bevestigWachtwoord = second.value;
        if (wachtwoord !== bevestigWachtwoord) {
            second.setErrors({ mismatch: true });
            return ({ mismatch: true });
          } else {
            if (bevestigWachtwoord !== null && bevestigWachtwoord !== '' && wachtwoord !== null && wachtwoord !== '') {
              second.setErrors();
            }  
          }
    }
    return null;
  }
}
