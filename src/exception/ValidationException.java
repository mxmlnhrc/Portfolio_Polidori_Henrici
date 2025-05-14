package exception;

/** Basis-Exception für alle Validierungsfehler */

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}