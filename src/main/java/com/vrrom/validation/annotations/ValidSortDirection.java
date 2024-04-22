package com.vrrom.validation.annotations;

import com.vrrom.validation.validators.SortDirectionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = SortDirectionValidator.class)
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ValidSortDirection {
    String message() default "Invalid sort direction value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}