package com.diefesson.flightmanager.constraints;

import com.diefesson.flightmanager.model.Flight;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SeatingLimitValidator implements ConstraintValidator<SeatingLimit, Flight> {

    @Override
    public boolean isValid(Flight value, ConstraintValidatorContext context) {
        return value.getPassengersCount() <= value.getSeats();
    }

}
