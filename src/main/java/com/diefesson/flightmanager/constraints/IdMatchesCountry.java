package com.diefesson.flightmanager.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = IdMatchesCountryValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdMatchesCountry {

    String message() default "invalid country and id combination";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}