package com.vrrom.customer.validator.personalId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonalIdValidator implements ConstraintValidator<PersonalId, Long> {
    private int numberOfDigits;

    @Override
    public void initialize(PersonalId constraintAnnotation) {
        this.numberOfDigits = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        int digitCount = String.valueOf(Math.abs(value)).length();
        return digitCount == numberOfDigits;
    }
}
