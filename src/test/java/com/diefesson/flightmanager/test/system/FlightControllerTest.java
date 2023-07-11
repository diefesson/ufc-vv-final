package com.diefesson.flightmanager.test.system;

import static com.diefesson.flightmanager.test.util.ValidInstances.createValidFlightDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.diefesson.flightmanager.App;
import com.diefesson.flightmanager.control.FlightController;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(SpringExtension.class)
@Import(App.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FlightControllerTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Autowired
    public FlightController flightController;

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
    public void validFlightDto() {
        var violations = validator.validate(createValidFlightDto());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addGetFlight() {
        var flight = createValidFlightDto();
        var added = flightController.post(flight);
        assertEquals(flight, added);
        var found = flightController.find(flight.getFlightNumber());
        assertTrue(found.isPresent());
        assertEquals(flight, found.get());
    }

    @Test
    public void addGetInvalidFlight() {
        var flight = createValidFlightDto();
        flightController.post(flight);
        var found = flightController.find("AA7890");
        assertTrue(found.isEmpty());
    }

    @Test
    public void addListFlights() {
        var flights = IntStream.range(1, 100).mapToObj(suffix -> {
            var flight = createValidFlightDto();
            flight.setFlightNumber(("TS%04d".formatted(suffix)));
            return flight;
        }).toList();
        for (var p : flights) {
            var r = flightController.post(p);
            assertEquals(p, r);
        }
        var all = flightController.findAll();
        assertEquals(flights, all);
    }

}
