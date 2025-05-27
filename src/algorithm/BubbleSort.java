package algorithm;

import java.util.Comparator;
import java.util.List;

/**
 * BubbleSort-Algorithmus (Kategorie: Sortieren durch Vertauschen)
 */
public class BubbleSort<T> implements SortAlgorithm<T> {
    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    // Tausche Elemente
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break; // Falls keine Vertauschung, abbrechen (Liste sortiert)
        }
    }
}
