package datastructure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

/**
 * Binärer Suchbaum für generische Typen, implementiert EigeneListe<T>.
 * Fügt Elemente gemäß Comparator ein, bietet In-Order-Iteration.
 */
public class BinarySearchTree<T> implements EigeneListe<T> {
    private class Node {
        T data;
        Node left, right;
        Node(T data) { this.data = data; }
    }

    private Node root;
    private int size;
    private final Comparator<T> comparator;

    /**
     * Konstruktor mit Comparator für die Ordnung.
     * @param comparator Vergleichsfunktion für Elemente
     */
    public BinarySearchTree(Comparator<T> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator darf nicht null sein");
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    @Override
    public void add(T element) {
        if (element == null) throw new IllegalArgumentException("Element darf nicht null sein");
        root = insert(root, element);
        size++;
    }

    private Node insert(Node node, T element) {
        if (node == null) {
            return new Node(element);
        }
        int cmp = comparator.compare(element, node.data);
        if (cmp < 0) {
            node.left = insert(node.left, element);
        } else {
            node.right = insert(node.right, element);
        }
        return node;
    }

    @Override
    public boolean remove(T element) {
        if (element == null) return false;
        int oldSize = size;
        root = delete(root, element);
        return size < oldSize;
    }

    private Node delete(Node node, T element) {
        if (node == null) return null;
        int cmp = comparator.compare(element, node.data);
        if (cmp < 0) {
            node.left = delete(node.left, element);
        } else if (cmp > 0) {
            node.right = delete(node.right, element);
        } else {
            // gefunden
            size--;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // zwei Kinder: Nachfolger finden
            Node successor = findMin(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data);
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        List<T> list = new ArrayList<>(size);
        // In-Order traversal sammeln
        inorderCollect(root, list);
        return list.get(index);
    }

    private void inorderCollect(Node node, List<T> list) {
        if (node == null) return;
        inorderCollect(node.left, list);
        list.add(node.data);
        inorderCollect(node.right, list);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T element) {
        Node curr = root;
        while (curr != null) {
            int cmp = comparator.compare(element, curr.data);
            if (cmp == 0) return true;
            curr = (cmp < 0) ? curr.left : curr.right;
        }
        return false;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Stack<Node> stack = new Stack<>();
            private Node current = root;

            @Override
            public boolean hasNext() {
                return (!stack.isEmpty() || current != null);
            }

            @Override
            public T next() {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }
                if (stack.isEmpty()) {
                    throw new NoSuchElementException();
                }
                Node node = stack.pop();
                T result = node.data;
                current = node.right;
                return result;
            }
        };
    }

    @Override
    public boolean remove(T element, String credential) {
        throw new UnsupportedOperationException("Benutze remove(T element) anstelle");
    }
}
