package model;

import exception.EmptyNameException;

import java.util.Objects;

/**
 * Repräsentiert einen Studenten mit Matrikelnummer.
 */
public class Student extends Mensch {
    private String matrikelnummer;

    /**
     * Konstruktor.
     * @param name           Name des Studenten (nicht leer)
     * @param birthDate      Geburtsdatum (nicht null)
     * @param matrikelnummer Matrikelnummer (nicht leer)
     * @throws EmptyNameException wenn der Name leer ist
     * @throws IllegalArgumentException wenn die Matrikelnummer ungültig ist
     */
    public Student(String name, String birthDate, String matrikelnummer) throws EmptyNameException {
        super(name, birthDate);
        setMatrikelnummer(matrikelnummer);
    }

    public String getMatrikelnummer() {
        return matrikelnummer;
    }

    /**
     * Setzt die Matrikelnummer.
     * @param matrikelnummer Matrikelnummer (nicht null oder leer)
     */
    public void setMatrikelnummer(String matrikelnummer) {
        if (matrikelnummer == null || matrikelnummer.trim().isEmpty()) {
            throw new IllegalArgumentException("Matrikelnummer darf nicht leer sein");
        }
        this.matrikelnummer = matrikelnummer.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(matrikelnummer, student.matrikelnummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matrikelnummer);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + getName() + '\'' +
                ", birthDate=" + getBirthDate() +
                ", matrikelnummer='" + matrikelnummer + '\'' +
                '}';
    }
}
