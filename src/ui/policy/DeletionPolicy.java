package ui.policy;

public interface DeletionPolicy<T> {
    /**
     * Bestimmt, ob ein Objekt gelöscht werden darf.
     */
    boolean canDelete(T item);
}