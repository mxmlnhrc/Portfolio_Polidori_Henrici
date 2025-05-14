package exception;

/** Wird geworfen bei ungültiger Note */

public class InvalidGradeException extends ValidationException {
    public InvalidGradeException(double grade) {
        super("Ungültige Note: " + grade);
    }
}