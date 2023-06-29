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

    @Test
    void testValidFlight() {
        var violations = validator.validate(createValidFlight());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidFlightNumber() {
        var flight1 = createValidFlight();
        var flight2 = createValidFlight();
        flight1.setFlightNumber("AA123");
        flight2.setFlightNumber("AA1234");
        var violations1 = validator.validate(flight1);
        var violations2 = validator.validate(flight2);
        assertEquals(0, violations1.size());
        assertEquals(0, violations2.size());
    }

    @ParameterizedTest
    @CsvSource({
            "AA12", "AA12345"
    })
    public void testInvalidFlightNumber(String flightNumber) {
        var flight = createValidFlight();
        flight.setFlightNumber(flightNumber);
        var violations = validator.validate(flight);
        assertEquals(1, violations.size());
    }

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

    @SneakyThrows
    @Test
    public void testChangeOrigin() {
        var flight = createValidFlight();
        flight.takeOff();
        assertThrows(ModelException.class, () -> flight.setOrigin("Canada"));
        flight.land();
        assertThrows(ModelException.class, () -> flight.setOrigin("Canada"));
    }

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

    @SneakyThrows
    @Test
    public void testTakeOffFromSomewhere() {
        var flight = createValidFlight();
        flight.setOrigin(null);
        assertThrows(ModelException.class, () -> flight.takeOff());
    }

    @SneakyThrows
    @Test
    public void testLandIntoSomewhere() {
        var flight = createValidFlight();
        flight.takeOff();
        flight.setDestination(null);
        assertThrows(ModelException.class, () -> flight.land());
    }

    @Test
    public void testNoLandBeforeTakeOff() {
        var flight = createValidFlight();
        assertThrows(ModelException.class, () -> flight.land());
    }
}
