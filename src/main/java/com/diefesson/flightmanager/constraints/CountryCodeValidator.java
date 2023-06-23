package com.diefesson.flightmanager.constraints;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {

    private static final List<String> VALID_COUNTRIES = Arrays.asList(Locale.getISOCountries());

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        return VALID_COUNTRIES.contains(country);
    }

}
