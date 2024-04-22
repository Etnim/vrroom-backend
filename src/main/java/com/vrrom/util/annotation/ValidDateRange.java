package com.vrrom.util.annotation;


import com.vrrom.util.validator.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidDateRange {

    String message() default "The start date must be before the end date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
