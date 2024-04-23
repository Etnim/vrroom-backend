package com.vrrom.validation.validators;

import com.vrrom.validation.annotations.ValidSortDirection;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortDirectionValidator implements ConstraintValidator<ValidSortDirection, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.equalsIgnoreCase("asc") || value.equalsIgnoreCase("desc");
    }
}