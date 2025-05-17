package algorithm;

import java.util.Comparator;
import java.util.List;

/**
 * Mergesort-Implementierung (Divide-and-Conquer).
 * @param <T> Typ der zu sortierenden Elemente
 */
public class MergeSort<T> implements SortAlgorithm<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return;
        mergeSort(list, 0, list.size() - 1, comparator);
    }

    private void mergeSort(List<T> list, int left, int right, Comparator<T> comp) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid, comp);
            mergeSort(list, mid + 1, right, comp);
            merge(list, left, mid, right, comp);
        }
    }

    private void merge(List<T> list, int left, int mid, int right, Comparator<T> comp) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        Object[] L = new Object[n1];
        Object[] R = new Object[n2];
        for (int i = 0; i < n1; i++) L[i] = list.get(left + i);
        for (int j = 0; j < n2; j++) R[j] = list.get(mid + 1 + j);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comp.compare((T)L[i], (T)R[j]) <= 0) {
                list.set(k++, (T)L[i++]);
            } else {
                list.set(k++, (T)R[j++]);
            }
        }
        while (i < n1) list.set(k++, (T)L[i++]);
        while (j < n2) list.set(k++, (T)R[j++]);
    }
}
