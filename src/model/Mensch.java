package model;

import exception.EmptyNameException;
import exception.InvalidDateException;
import exception.ValidationException;
import util.DateValidator;
import util.Validator;


import java.time.LocalDate;
import java.util.Objects;

/**
 * Basisklasse für Personen mit Name und Geburtsdatum.
 */
public class Mensch {
    private String name;
    private final String birthDate;
    private final Validator<String> dateValidator = new DateValidator();

    /**
     * Konstruktor.
     * @param name        Name der Person (nicht leer)
     * @param birthDate   Geburtsdatum (nicht null)
     * @throws EmptyNameException wenn der Name leer ist
     */
    public Mensch(String name, String birthDate) throws EmptyNameException {
        setName(name);
        if (birthDate == null) {
            throw new IllegalArgumentException("Geburtsdatum darf nicht null sein");
        }
        try {
            dateValidator.validate(birthDate);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Person.
     * @param name neuer Name (nicht null oder leer)
     * @throws EmptyNameException wenn der Name leer ist
     */
    public void setName(String name) throws EmptyNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyNameException();
        }

        this.name = name.trim();
    }

    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mensch)) return false;
        Mensch mensch = (Mensch) o;
        return Objects.equals(name, mensch.name) &&
                Objects.equals(birthDate, mensch.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthDate);
    }

    @Override
    public String toString() {
        return "Mensch{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
