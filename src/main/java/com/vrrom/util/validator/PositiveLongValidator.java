package com.vrrom.util.validator;

import com.vrrom.util.annotation.PositiveLong;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositiveLongValidator implements ConstraintValidator<PositiveLong, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value == null || value > 0;
    }
}
