package com.diefesson.flightmanager.constraints;

import java.util.regex.Pattern;

import com.diefesson.flightmanager.constants.Regexes;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdMatchesCountryValidator implements ConstraintValidator<IdMatchesCountry, IdCountryGet> {

    private static final Pattern US_ID = Pattern.compile(Regexes.US_ID);
    private static final Pattern NON_US_ID = Pattern.compile(Regexes.NON_US_ID);

    @Override
    public boolean isValid(IdCountryGet value, ConstraintValidatorContext context) {
        var countryCode = value.getCountryCode();
        var id = value.getId();
        var regex = countryCode.equals("US") ? US_ID : NON_US_ID;
        return regex.matcher(id).matches();
    }
}
