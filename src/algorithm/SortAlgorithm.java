package algorithm;

import java.util.Comparator;
import java.util.List;

public interface SortAlgorithm<T> {
    /**
     * Sortiert die gegebene Liste anhand des Comparators.
     */
    void sort(List<T> list, Comparator<T> comparator);
}