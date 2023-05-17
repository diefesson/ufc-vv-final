package com.diefesson.fm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@Import(FlightWithPassengersTest.FlightWithPassengerConfiguration.class)
public class FlightWithPassengersTest {

    @TestConfiguration
    public static class FlightWithPassengerConfiguration {
        @Bean()
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public Flight produceFlight() throws IOException {
            return FlightBuilderUtil.buildFlightFromCsv();
        }
    }

    @Autowired
    public Flight flight;

    @Test
    public void testNumberOfSeatsCannotBeExceeded() throws IOException {
        assertEquals(50, flight.getPassengersNumber());
        assertThrows(RuntimeException.class, () -> {
            flight.addPassenger(new Passenger("124-56-7890", "Michael Johnson", "US"));
        });

    }

    @Test
    public void testAddRemovePassengers() throws IOException {
        flight.setSeats(51);
        Passenger additionalPassenger = new Passenger("124-56-7890",
                "Michael Johnson", "US");
        flight.addPassenger(additionalPassenger);
        assertEquals(51, flight.getPassengersNumber());
        flight.removePassenger(additionalPassenger);
        assertEquals(50, flight.getPassengersNumber());
        assertEquals(51, flight.getSeats());
    }

    @Test
    public void testSetInvalidSeats() {
        assertEquals(50, flight.getPassengersNumber());
        assertThrows(RuntimeException.class, () -> {
            flight.setSeats(49);
        });
    }

    @Test
    public void testSetValidSeats() {
        assertEquals(50, flight.getPassengersNumber());
        flight.setSeats(52);
        assertEquals(52, flight.getSeats());
    }
}
