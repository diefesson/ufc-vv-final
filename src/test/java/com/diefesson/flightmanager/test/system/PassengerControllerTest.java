package com.diefesson.flightmanager.test.system;

import static com.diefesson.flightmanager.test.util.ValidInstances.createValidPassengerDto;
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
import com.diefesson.flightmanager.control.PassengerController;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(SpringExtension.class)
@Import(App.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PassengerControllerTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Autowired
    public PassengerController passengerController;

    @BeforeAll
    public static void setupValidation() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void tearDownValidation() {
        validatorFactory.close();
    }

    // SYSTEM-PASSENGER-00
    @Test
    public void validPassengerDto() {
        var violations = validator.validate(createValidPassengerDto());
        assertTrue(violations.isEmpty());
    }

    // SYSTEM-PASSENGER-01
    @Test
    public void addGetPassenger() {
        var passenger = createValidPassengerDto();
        var added = passengerController.post(passenger);
        assertEquals(passenger, added);
        var found = passengerController.find(passenger.getId());
        assertTrue(found.isPresent());
        assertEquals(passenger, found.get());
    }

    // SYSTEM-PASSENGER-02
    @Test
    public void addListPassengers() {
        var passengers = IntStream.range(1, 100).mapToObj(suffix -> {
            var passenger = createValidPassengerDto();
            passenger.setId(("123-45-%04d".formatted(suffix)));
            return passenger;
        }).toList();
        for (var p : passengers) {
            var r = passengerController.post(p);
            assertEquals(p, r);
        }
        var all = passengerController.findAll();
        assertEquals(passengers, all);
    }

    // SYSTEM-PASSENGER-03
    @Test
    void unknownPassenger() {
        assertTrue(passengerController.find("123-456-7890").isEmpty());
    }

}
