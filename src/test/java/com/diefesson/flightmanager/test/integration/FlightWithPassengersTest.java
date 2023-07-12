package com.diefesson.flightmanager.test.integration;

import static com.diefesson.flightmanager.test.util.ValidInstances.createValidPassenger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.diefesson.flightmanager.exception.ModelException;
import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.test.util.FlightBuilderUtil;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.SneakyThrows;

public class FlightWithPassengersTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private Flight flight1;
    private Flight flight2;
    private Flight flight3;

    @BeforeAll
    public static void setupValidation() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void tearDownValidation() {
        validatorFactory.close();
    }

    @BeforeEach
    public void setupFlights() throws IOException {
        flight1 = FlightBuilderUtil.loadFlight("AA1234");
        flight2 = FlightBuilderUtil.loadFlight("AA1235");
        flight3 = FlightBuilderUtil.loadFlight("AA1236");
    }

    // INTEGRATION-FLIGHT-00
    @Test
    public void testFlightsPassengerCount() {
        assertEquals(flight1.getPassengersCount(), flight1.getSeats());
        assertEquals(flight2.getPassengersCount(), flight2.getSeats());
        assertEquals(flight3.getPassengersCount(), flight3.getSeats());
        assertEquals(50, flight1.getSeats());
        assertEquals(36, flight2.getSeats());
        assertEquals(24, flight3.getSeats());
    }

    // INTEGRATION-FLIGHT-01
    @Test
    public void testNumberOfSeatsCannotBeExceeded() {
        flight1.setSeats(49);
        var violations = validator.validate(flight1);
        assertEquals(1, violations.size());
    }

    // INTEGRATION-FLIGHT-02
    @SneakyThrows
    @Test
    public void testAddRemovePassengers() {
        flight1.setSeats(51);
        var additional = createValidPassenger();
        assertTrue(flight1.addPassenger(additional));
        assertEquals(51, flight1.getPassengersCount());
        assertTrue(flight1.removePassenger(additional));
        assertFalse(flight1.removePassenger(additional));
        assertEquals(50, flight1.getPassengersCount());
        assertEquals(51, flight1.getSeats());
    }

    // INTEGRATION-FLIGHT-03
    @SneakyThrows
    @Test
    public void testNotChangePassengersAfterTakeOff() {
        flight1.setOrigin("Brazil");
        flight1.setDestination("France");
        flight1.setPassengers(Set.of());
        flight1.addPassenger(createValidPassenger());
        flight1.takeOff();
        assertThrows(ModelException.class, () -> flight1.addPassenger(createValidPassenger()));
        assertThrows(ModelException.class, () -> flight1.removePassenger(createValidPassenger()));
        flight1.land();
        assertThrows(ModelException.class, () -> flight1.addPassenger(createValidPassenger()));
        assertThrows(ModelException.class, () -> flight1.removePassenger(createValidPassenger()));
    }
}
