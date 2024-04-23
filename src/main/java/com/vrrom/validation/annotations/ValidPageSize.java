package com.vrrom.validation.annotations;

import com.vrrom.validation.validators.PageSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PageSizeValidator.class)
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ValidPageSize {
    String message() default "The number must be positive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}