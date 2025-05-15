package algorithm;

import java.util.Comparator;
import java.util.List;

/**
 * Interface für Sortierstrategien.
 * @param <T> Typ der zu sortierenden Elemente
 */
public interface SortAlgorithm<T> {
    /**
     * Sortiert die gegebene Liste anhand des Comparators.
     * @param list zu sortierende Liste
     * @param comparator Vergleichsfunktion für Elemente
     */
    void sort(List<T> list, Comparator<T> comparator);
}
