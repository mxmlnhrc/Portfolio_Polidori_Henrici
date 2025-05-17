package datastructure;

import java.util.Iterator;

public interface EigeneListe<T> extends Iterable<T> {
    void add(T element); // Fügt ein Element am Ende der Liste hinzu
    boolean remove(T element); // Entfernt das erste Vorkommen eines Elements, gibt true bei Erfolg zurück
    T get(int index); // Gibt das Element am angegebenen Index zurück
    int size(); // Liefert die Anzahl der Elemente in der Liste
    boolean contains(T element);  // Prüft, ob ein Element in der Liste enthalten ist
    void clear(); // Entfernt alle Elemente aus der Liste

    boolean remove(T element, String credential);
}