package com.vrrom.validation.validators;

import com.vrrom.validation.annotations.ValidPageSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageSizeValidator implements ConstraintValidator<ValidPageSize, Integer> {
final static int MIN_PAGE_SIZE = 1;
final static int MAX_PAGE_SIZE = 20;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >=MIN_PAGE_SIZE && value <= MAX_PAGE_SIZE;
    }
}