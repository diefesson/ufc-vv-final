package com.diefesson.flightmanager.constraints;

import java.util.regex.Pattern;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.model.Passenger;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdMatchesCountryValidator implements ConstraintValidator<IdMatchesCountry, Passenger> {

    private static final Pattern SSN = Pattern.compile(Regexes.SSN);
    private static final Pattern NON_US_IDENTIFIER = Pattern.compile(Regexes.NON_US_IDENTIFIER);

    @Override
    public boolean isValid(Passenger passenger, ConstraintValidatorContext context) {
        var countryCode = passenger.getCountryCode();
        var id = passenger.getId();
        var regex = countryCode.equals("US") ? SSN : NON_US_IDENTIFIER;
        return regex.matcher(id).matches();
    }
}
