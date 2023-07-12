package com.diefesson.flightmanager.test.unit;

import static com.diefesson.flightmanager.test.util.ValidInstances.createValidFlight;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.diefesson.flightmanager.exception.ModelException;
import com.diefesson.flightmanager.model.FlightStatus;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.SneakyThrows;

public class FlightTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void setupValidation() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void tearDownValidation() {
        validatorFactory.close();
    }

    // UNIT-FLIGHT-00
    @Test
    void testValidFlight() {
        var violations = validator.validate(createValidFlight());
        assertTrue(violations.isEmpty());
    }

    // UNIT-FLIGHT-01
    @ParameterizedTest
    @CsvSource({
            "AA123, 0",
            "AA1234, 0",
            "AA12, 1",
            "AA12345, 1"
    })
    public void testFlightNumber(String flightNumber, int expectedViolations) {
        var flight = createValidFlight();
        flight.setFlightNumber(flightNumber);
        var violations1 = validator.validate(flight);
        assertEquals(expectedViolations, violations1.size());
    }

    // UNIT-FLIGHT-02
    @SneakyThrows
    @Test
    public void testFlyStatus() {
        var flight = createValidFlight();
        assertEquals(FlightStatus.AT_ORIGIN, flight.getStatus());
        flight.takeOff();
        assertEquals(FlightStatus.IN_FLIGHT, flight.getStatus());
        flight.land();
        assertEquals(FlightStatus.AT_DESTINATION, flight.getStatus());
    }

    // UNIT-FLIGHT-03
    @SneakyThrows
    @Test
    public void testFlyOrdering() {
        var flight = createValidFlight();
        flight.takeOff();
        assertThrows(ModelException.class, () -> flight.takeOff());
        flight.land();
        assertThrows(ModelException.class, () -> flight.takeOff());
        assertThrows(ModelException.class, () -> flight.land());
    }

    // UNIT-FLIGHT-04
    @SneakyThrows
    @Test
    public void testChangeOrigin() {
        var flight = createValidFlight();
        flight.takeOff();
        assertThrows(ModelException.class, () -> flight.setOrigin("Canada"));
        flight.land();
        assertThrows(ModelException.class, () -> flight.setOrigin("Canada"));
    }

    // UNIT-FLIGHT-05
    @SneakyThrows
    @Test
    public void testChangeDestination() {
        var flight = createValidFlight();
        flight.setDestination("France");
        flight.takeOff();
        flight.setDestination("Canada");
        flight.land();
        assertThrows(ModelException.class, () -> flight.setDestination("Canada"));
    }

    // UNIT-FLIGHT-06
    @SneakyThrows
    @Test
    public void testTakeOffFromSomewhere() {
        var flight = createValidFlight();
        flight.setOrigin(null);
        assertThrows(ModelException.class, () -> flight.takeOff());
    }

    // UNIT-FLIGHT-07
    @SneakyThrows
    @Test
    public void testLandIntoSomewhere() {
        var flight = createValidFlight();
        flight.takeOff();
        flight.setDestination(null);
        assertThrows(ModelException.class, () -> flight.land());
    }

    // UNIT-FLIGHT-08
    @Test
    public void testNoLandBeforeTakeOff() {
        var flight = createValidFlight();
        assertThrows(ModelException.class, () -> flight.land());
    }
}
