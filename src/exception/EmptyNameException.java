package exception;

/** Wird geworfen, wenn ein Name leer ist */

public class EmptyNameException extends ValidationException {
    public EmptyNameException() {
        super("Name darf nicht leer sein");
    }
}