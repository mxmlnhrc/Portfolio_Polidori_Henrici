package util;

import exception.InvalidGradeException;
import exception.ValidationException;

public class GradeValidator implements Validator<Double> {
    @Override
    public void validate(Double input) throws ValidationException {
        if (input == null || input < 0.0 || input > 100.0) {  // Annahme von Noten zwischen 0 und 100 (Prozent) | Alt: 1.0 - 6.0 / Unistandard
            throw new InvalidGradeException(input);
        }
    }
}