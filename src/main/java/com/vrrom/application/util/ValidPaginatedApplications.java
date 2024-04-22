package com.vrrom.application.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PaginatedApplicationsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaginatedApplications {
    String message() default "Invalid pagination request parameters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
