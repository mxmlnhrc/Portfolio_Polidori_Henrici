package util;

import exception.InvalidDateException;
import exception.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator implements Validator<String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public void validate(String input) throws ValidationException {
        try {
            LocalDate.parse(input, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(input);
        }
    }
}