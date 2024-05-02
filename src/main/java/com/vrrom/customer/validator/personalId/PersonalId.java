package com.vrrom.customer.validator.personalId;

import com.vrrom.customer.validator.minAge.MinimumAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PersonalIdValidator.class)
@Documented
public @interface PersonalId {
        String message() default "Number must have exactly 11 digits";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
        int value() default 11;
}
