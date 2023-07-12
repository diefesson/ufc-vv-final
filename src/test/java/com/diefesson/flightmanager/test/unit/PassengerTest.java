package com.diefesson.flightmanager.test.unit;

import static com.diefesson.flightmanager.test.util.ValidInstances.createValidPassenger;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.diefesson.flightmanager.test.util.ValidInstances;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PassengerTest {

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

    // UNIT-PASSENGER-00
    @Test
    public void testValidPassenger() {
        var passenger = createValidPassenger();
        var violations = validator.validate(passenger);
        assertTrue(violations.isEmpty());
    }

    // UNIT-PASSENGER-01
    @ParameterizedTest
    @CsvSource({
            "300-45-6789, US, 0",
            "900-45-6789, US, 1",
            "900-45-6789, GB, 0",
            "300-45-6789, GB, 1",
    })
    public void testPassengerId(String id, String countryCode, int expectedViolations) {
        var passenger = createValidPassenger();
        passenger.setId(id);
        passenger.setCountryCode(countryCode);
        var violations = validator.validate(passenger);
        assertEquals(expectedViolations, violations.size());
    }

    // UNIT-PASSENGER-02
    @ParameterizedTest
    @CsvSource({
            "GB, 0",
            "BR, 0",
            "FR, 0",
            "ZZ, 1",
            "YY, 1",
            "AA, 1"
    })
    public void testPassengerCountryCode(String countryCode, int expectedViolations) {
        var passenger = ValidInstances.createValidPassenger();
        passenger.setId("900-45-6789");
        passenger.setCountryCode(countryCode);
        var violations = validator.validate(passenger);
        assertEquals(expectedViolations, violations.size());
    }
}