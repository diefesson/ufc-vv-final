package com.diefesson.flightmanager.test.util;

import java.util.Set;

import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.model.Passenger;

public class ValidInstances {

    public static Flight createValidFlight() {
        return Flight
                .builder()
                .flightNumber("AA1234")
                .origin("Brazil")
                .destination("United States")
                .passengers(Set.of())
                .build();
    }

    public static Passenger createValidPassenger() {
        return Passenger.builder()
                .id("321-45-6789")
                .name("John Smith")
                .countryCode("US")
                .build();
    }

}
