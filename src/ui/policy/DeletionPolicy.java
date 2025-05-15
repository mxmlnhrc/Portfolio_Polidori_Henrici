package ui.policy;

/**
 * Schnittstelle für Löschregeln.
 * @param <T> Typ des zu löschenden Objekts
 */
public interface DeletionPolicy<T> {
    /**
     * Bestimmt, ob das gegebene Objekt gelöscht werden darf.
     * @param item das zu überprüfende Objekt
     * @param credential das vom Nutzer eingegebene Passwort oder Token
     * @return true, wenn Löschung erlaubt, sonst false
     */
    boolean canDelete(T item, String credential);
}
