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
        var vip = Boolean.parseBoolean(split[3].trim());
        return Passenger
                .builder()
                .id(id)
                .name(name)
                .countryCode(countryCode)
                .vip(vip)
                .build();
    }

    public static Set<Passenger> loadPassengers(String flightNumber) throws IOException {
        var path = "/flights/%s.csv".formatted(flightNumber);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(FlightBuilderUtil.class.getResourceAsStream(path)))) {
            return reader
                    .lines()
                    .map(FlightBuilderUtil::lineToPassenger)
                    .collect(Collectors.toSet());
        }
    }

    public static Flight loadFlight(String flightNumber) throws IOException {
        var passengers = loadPassengers(flightNumber);
        return Flight
                .builder()
                .flightNumber(flightNumber)
                .seats(passengers.size())
                .passengers(passengers)
                .build();
    }

}
