package exception;

/** Wird geworfen bei doppelten Namen (Projekt oder Student) */

public class DuplicatedNameException extends ValidationException {
    public DuplicatedNameException(String name) {
        super("Name ist schon vorhanden: " + name);
    }
}