package com.vrrom.util.annotation;


import com.vrrom.util.validator.PositiveLongValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PositiveLongValidator.class)
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface PositiveLong {
    String message() default "The number must be positive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
