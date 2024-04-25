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
    String message() default "Page size must be between 1 and 20";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}