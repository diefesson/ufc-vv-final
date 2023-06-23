package com.diefesson.flightmanager.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.model.Passenger;

public class FlightBuilderUtil {

    private static Passenger lineToPassenger(String line) {
        var split = line.split(";");
        var id = split[0].trim();
        var name = split[1].trim();
        var countryCode = split[2].trim();
        return Passenger
                .builder()
                .id(id)
                .name(name)
                .countryCode(countryCode)
                .build();
    }

    public static Set<Passenger> readPassengers(String flightNumber) throws IOException {
        var path = "/flights/%s.csv".formatted(flightNumber);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(FlightBuilderUtil.class.getResourceAsStream(path)))) {
            return reader
                    .lines()
                    .map(FlightBuilderUtil::lineToPassenger)
                    .collect(Collectors.toSet());
        }
    }

    public static Flight readFlights(String flightNumber) throws IOException {
        var passengers = readPassengers(flightNumber);
        return Flight
                .builder()
                .flightNumber(flightNumber)
                .seats(passengers.size())
                .passengers(passengers)
                .build();
    }

}
