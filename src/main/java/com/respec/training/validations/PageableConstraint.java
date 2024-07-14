package com.respec.training.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageableValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PageableConstraint {
    String message() default "Invalid page parameters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int maxPerPage() default 50;
}