package com.vrrom.customer.validator.personalId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonalIdValidator implements ConstraintValidator<PersonalId, Long> {
    private int numberOfDigits;
    private String regex;


    @Override
    public void initialize(PersonalId constraintAnnotation) {
        this.numberOfDigits = constraintAnnotation.value();
        this.regex = "^[3-6][0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])\\d{4}$";
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String id = value.toString();
        int digitCount = id.length();

        if (digitCount != numberOfDigits) {
            return false;
        }

        return id.matches(regex);
    }
}
