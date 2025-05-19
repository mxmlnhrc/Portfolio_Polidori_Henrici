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
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(list, n, i, comparator);
        for (int i = n - 1; i >= 0; i--) {
            T temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);
            heapify(list, i, 0, comparator);
        }
    }

    /**
     * Hilfsmethode, um den Heap zu erstellen.
     * @param list Liste, die in einen Heap umgewandelt werden soll
     * @param n Größe des Heaps
     * @param i Index des aktuellen Knotens
     * @param comparator Vergleichsfunktion für die Elemente
     */
    private void heapify(List<T> list, int n, int i, Comparator<T> comparator) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n && comparator.compare(list.get(l), list.get(largest)) > 0)
            largest = l;
        if (r < n && comparator.compare(list.get(r), list.get(largest)) > 0)
            largest = r;
        if (largest != i) {
            T swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);
            heapify(list, n, largest, comparator);
        }
    }
}
