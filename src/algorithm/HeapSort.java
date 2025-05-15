package algorithm;

import java.util.Comparator;
import java.util.List;

/**
     * Heapsort-Implementierung (Selection-Kategorie).
     * @param <T> Typ der zu sortierenden Elemente
 */

/**
 *  Ein Heap ist eine spezielle Baum\-Datenstruktur mit folgenden Eigenschaften:
 * \- Meist handelt es sich um einen binären Heap, also einen vollständigen Binärbaum.
 * \- Im Max\-Heap ist jeder Elternknoten größer oder gleich seinen Kindknoten (maximales Element an der Wurzel).
 * \- Im Min\-Heap ist jeder Elternknoten kleiner oder gleich seinen Kindknoten (minimales Element an der Wurzel).
 * \- Heaps werden häufig als Array implementiert, wobei Kind\- und Elternbeziehungen über Indizes berechnet werden.
 * \- Typische Anwendungen sind Prioritätswarteschlangen und Sortieralgorithmen wie Heapsort.
 */

public class HeapSort<T> implements SortAlgorithm<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        // Heap aufbauen (Max-Heap)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i, comparator);
        }
        // Elemente extrahieren
        for (int i = n - 1; i > 0; i--) {
            swap(list, 0, i);
            heapify(list, i, 0, comparator);
        }
    }

    private void heapify(List<T> list, int heapSize, int rootIndex, Comparator<T> comparator) {
        int largest = rootIndex;
        int left = 2 * rootIndex + 1;
        int right = 2 * rootIndex + 2;
        if (left < heapSize && comparator.compare(list.get(left), list.get(largest)) > 0) {
            largest = left;
        }
        if (right < heapSize && comparator.compare(list.get(right), list.get(largest)) > 0) {
            largest = right;
        }
        if (largest != rootIndex) {
            swap(list, rootIndex, largest);
            heapify(list, heapSize, largest, comparator);
        }
    }

    private void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
