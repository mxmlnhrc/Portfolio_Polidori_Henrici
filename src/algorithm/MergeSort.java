package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Mergesort-Implementierung (Divide-and-Conquer).
 * @param <T> Typ der zu sortierenden Elemente
 */
public class MergeSort<T> implements SortAlgorithm<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() < 2) {
            return;
        }
        List<T> sorted = mergeSort(list, comparator);
        for (int i = 0; i < sorted.size(); i++) {
            list.set(i, sorted.get(i));
        }
    }

    private List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        int size = list.size();
        if (size < 2) {
            return new ArrayList<>(list);
        }
        int mid = size / 2;
        List<T> left = mergeSort(list.subList(0, mid), comparator);
        List<T> right = mergeSort(list.subList(mid, size), comparator);
        return merge(left, right, comparator);
    }

    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }
}
