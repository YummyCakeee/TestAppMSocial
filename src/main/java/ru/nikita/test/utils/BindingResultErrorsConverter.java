package ru.nikita.test.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class BindingResultErrorsConverter {

    public static String convertToString(BindingResult bindingResult) {
        List<FieldError> errors =  bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder("");
        for (FieldError error: errors) {
            sb.append(error.getField()).append(" - ")
                    .append(error.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }
}
