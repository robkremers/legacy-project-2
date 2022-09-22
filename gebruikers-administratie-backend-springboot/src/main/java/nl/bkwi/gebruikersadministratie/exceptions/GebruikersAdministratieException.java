package nl.bkwi.gebruikersadministratie.exceptions;

public class GebruikersAdministratieException extends Exception {

    public GebruikersAdministratieException() {
        super();
    }

    public GebruikersAdministratieException(String message) {
        super(message);
    }

    public GebruikersAdministratieException(String message, Throwable cause) {
        super(message, cause);
    }
}
