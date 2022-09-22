/** @format */

export interface Rollen extends Array<Rol> {}
export interface GebruikerDto {
	userId: string;
	naam: string;
	achternaam: string;
	voornaam: string;
	email: string;
	afdeling: string;
	inlognaam: string;
	tijdVanlaatsteInlog: string;
	rollen: Rollen;
    employeeNr: string;
    telefoonnummer: string;
}

export interface PasswordDto {
	password: string;
}

export interface Rol {
	id: string;
}
export interface SystemUser {
	naam: string;
}
