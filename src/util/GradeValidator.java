package util;

import exception.InvalidGradeException;
import exception.ValidationException;

public class GradeValidator implements Validator<Double> {
    @Override
    public void validate(Double input) throws ValidationException {
        if (input == null || input < 0.0 || input > 100.0) {
            throw new InvalidGradeException(input);
        }
    }
}