package exception;

/** Wird geworfen bei ungültigem Datum */
public class InvalidDateException extends ValidationException {
    public InvalidDateException(String date) {
        super("Ungültiges Datum: " + date);
    }
}