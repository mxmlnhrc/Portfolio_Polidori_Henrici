package util;

import exception.ValidationException;

public interface Validator<T> {
    /**
     * Validiert den Input oder wirft eine ValidationException.
     */
    void validate(T input) throws ValidationException;
}