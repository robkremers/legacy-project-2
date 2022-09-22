import {GebruikerDto, PasswordDto} from "../../types/project.endpoint.model";

export type IGebruikersTableData = GebruikerDto[];
export type IGebruikerData = GebruikerDto;

interface IGebruikersTableError {
    errorMessage: string
}

interface IGebruikerInfo {
  infoMessage: string
  infoGenerateMessage: string
}

interface IGebruikerError {
  errorMessage: string
}

export interface IGebruikersTableMessage {
    data?: IGebruikersTableData
    error?: IGebruikersTableError
}

export interface IGebruikerMessage {
  data?: IGebruikerData
  error?: IGebruikerError
}

export interface IUpdatePasswordMessage {
    info?: IGebruikerInfo
    error?: IGebruikerError
}

export interface IUpdateGeneratePasswordMessage {
  passwordDto?: PasswordDto
  info?: IGebruikerInfo
  error?: IGebruikerError
}