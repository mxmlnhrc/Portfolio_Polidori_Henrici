package model;

import exception.DuplicatedNameException;
import exception.EmptyNameException;
import exception.ValidationException;
import util.DateValidator;
import util.GradeValidator;
import util.Validator;
import datastructure.EigeneListe;
import datastructure.SimpleLinkedList;

/**
 * Repräsentiert ein Projekt mit Titel, Teilnehmern, Note und Abgabedatum.
 */
public class Projekt {
    private String titel;
    private final EigeneListe<Student> teilnehmer;
    private double note;
    private String abgabeDatum; // Format "yyyyMMdd"

    private final Validator<String> dateValidator = new DateValidator();
    private final Validator<Double> gradeValidator = new GradeValidator();

    /**
     * Konstruktor.
     * @param titel        Projekttitel (nicht leer)
     * @param note         Note (0.0 bis 100.0)
     * @param abgabeDatum  Abgabedatum im Format yyyyMMdd
     * @throws EmptyNameException       wenn Titel leer ist
     * @throws ValidationException      bei ungültiger Note oder Datum
     */
    public Projekt(String titel, double note, String abgabeDatum) throws EmptyNameException, ValidationException {
        setTitel(titel);
        setNote(note);
        setAbgabeDatum(abgabeDatum);
        this.teilnehmer = new SimpleLinkedList<>();
    }

    public String getTitel() {
        return titel;
    }

    /**
     * Setzt den Projekttitel.
     * @param titel neuer Titel (nicht null oder leer)
     * @throws EmptyNameException wenn Titel leer ist
     */
    public void setTitel(String titel) throws EmptyNameException {
        if (titel == null || titel.trim().isEmpty()) {
            throw new EmptyNameException();
        }
        this.titel = titel.trim();
    }

    public double getNote() {
        return note;
    }

    /**
     * Setzt die Note nach Validierung.
     * @param note Note (0.0 bis 100.0)
     * @throws ValidationException bei ungültiger Note
     */
    public void setNote(double note) throws ValidationException {
        gradeValidator.validate(note);
        this.note = note;
    }

    public String getAbgabeDatum() {
        return abgabeDatum;
    }

    /**
     * Setzt das Abgabedatum nach Validierung.
     * @param abgabeDatum Datum im Format yyyyMMdd
     * @throws ValidationException bei ungültigem Datum
     */
    public void setAbgabeDatum(String abgabeDatum) throws ValidationException {
        dateValidator.validate(abgabeDatum);
        this.abgabeDatum = abgabeDatum;
    }

    /**
     * Fügt einen Studenten hinzu; wirft bei Duplikaten.
     * @param student Student
     * @throws DuplicatedNameException wenn Student bereits vorhanden ist
     */
    public void addStudent(Student student) throws DuplicatedNameException {
        for (Student s : teilnehmer) {
            if (s.equals(student)) {
                throw new DuplicatedNameException(student.getName());
            }
        }
        teilnehmer.add(student);
    }

    /**
     * Entfernt einen Studenten.
     * @param student Student zum Entfernen
     * @return true, wenn entfernt wurde
     */
    public boolean removeStudent(Student student) {
        return teilnehmer.remove(student);
    }

    public EigeneListe<Student> getTeilnehmer() {
        return teilnehmer;
    }

    @Override
    public String toString() {
        return "Projekt{" +
                "titel='" + titel + '\'' +
                ", note=" + note +
                ", abgabeDatum='" + abgabeDatum + '\'' +
                ", teilnehmer=" + teilnehmer +
                '}';
    }
}