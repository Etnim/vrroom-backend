package com.vrrom.dtos;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {
    private static Validator validator;

    public static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }
}