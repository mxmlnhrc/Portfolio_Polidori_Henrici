package ui.policy;

import model.Projekt;

/**
 * Password-basierte Lösch-Policy für Projekte.
 */
public class PasswordDeletionPolicy implements DeletionPolicy<Projekt> {
    private final String password;

    /**
     * Standard-Konstruktor mit hartkodiertem Passwort.
     */
    public PasswordDeletionPolicy() {
        this.password = "secret"; // Default-Passwort, kann angepasst werden
    }

    /**
     * Konstruktor mit individuellem Passwort.
     * @param password das gültige Passwort
     */
    public PasswordDeletionPolicy(String password) {
        this.password = password;
    }

    @Override
    public boolean canDelete(Projekt item, String credential) {
        return password.equals(credential);
    }
}
