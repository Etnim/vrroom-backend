package com.vrrom.customer.validator.minAge;

import com.vrrom.customer.validator.minAge.MinimumAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinimumAgeValidator implements ConstraintValidator <MinimumAge, LocalDate>{
    private int minAge;

    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return false;
        return Period.between(date, LocalDate.now()).getYears() >= minAge;
    }
}
