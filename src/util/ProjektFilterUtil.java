package util;

import datastructure.EigeneListe;
import model.Projekt;
import model.Student;
import datastructure.BinarySearchTree;
import java.util.Comparator;
import java.util.Collection;

public class ProjektFilterUtil {

    /**
     * Gibt alle Studenten aus allen Projekten als BinarySearchTree zurück.
     * Die Studenten werden nach Matrikelnummer sortiert eingefügt.
     * @param alleProjekte Projektliste mit allen Projekten
     * @return BinarySearchTree<Student> mit allen Studenten
     */
    public static BinarySearchTree<Student> getAllStudents(EigeneListe<Projekt> alleProjekte) {
        BinarySearchTree<Student> studenten = new BinarySearchTree<>(Comparator.comparing(Student::getMatrikelnummer));
        for (Projekt p : alleProjekte) {
            for (Student s : p.getTeilnehmer()) {
                studenten.add(s);
            }
        }
        return studenten;
    }

    /**
     * Gibt alle Projekte zurück, deren Titel (case-insensitive) den Suchbegriff enthält.
     */
    public static EigeneListe<Projekt> filterByTitel(EigeneListe<Projekt> alle, String suchbegriff) {
        BinarySearchTree<Projekt> gefiltert = new BinarySearchTree<>(Comparator.comparing(Projekt::getTitel, String.CASE_INSENSITIVE_ORDER));
        String suchbegriffLower = suchbegriff == null ? "" : suchbegriff.toLowerCase();
        for (Projekt p : alle) {
            String titel = p.getTitel() != null ? p.getTitel().toLowerCase() : "";
            if (titel.contains(suchbegriffLower)) {
                gefiltert.add(p);
            }
        }
        return gefiltert;
    }

    /**
     * Gibt alle Projekte mit exakt passender Note zurück.
     */
    public static EigeneListe<Projekt> filterByNoteExact(EigeneListe<Projekt> alle, double note) {
        BinarySearchTree<Projekt> gefiltert = new BinarySearchTree<>(Comparator.comparing(Projekt::getTitel, String.CASE_INSENSITIVE_ORDER));
        for (Projekt p : alle) {
            if (p.getNote() == note) {
                gefiltert.add(p);
            }
        }
        return gefiltert;
    }

    /**
     * Gibt alle Projekte mit einer Note >= min zurück.
     */
    public static EigeneListe<Projekt> filterByNoteMin(EigeneListe<Projekt> alle, double minNote) {
        BinarySearchTree<Projekt> gefiltert = new BinarySearchTree<>(Comparator.comparing(Projekt::getTitel, String.CASE_INSENSITIVE_ORDER));
        for (Projekt p : alle) {
            if (p.getNote() >= minNote) {
                gefiltert.add(p);
            }
        }
        return gefiltert;
    }

    /**
     * Gibt alle Projekte mit einer Note <= max zurück.
     */
    public static EigeneListe<Projekt> filterByNoteMax(EigeneListe<Projekt> alle, double maxNote) {
        BinarySearchTree<Projekt> gefiltert = new BinarySearchTree<>(Comparator.comparing(Projekt::getTitel, String.CASE_INSENSITIVE_ORDER));
        for (Projekt p : alle) {
            if (p.getNote() <= maxNote) {
                gefiltert.add(p);
            }
        }
        return gefiltert;
    }
}
