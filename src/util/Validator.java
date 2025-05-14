package util;

import exception.ValidationException;

/** Allgemeine Schnittstelle für Validierung */
public interface Validator<T> {
    void validate(T input) throws ValidationException;
}