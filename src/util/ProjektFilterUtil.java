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
}
