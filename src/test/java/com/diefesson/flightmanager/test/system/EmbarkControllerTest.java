package com.diefesson.flightmanager.test.system;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.diefesson.flightmanager.App;
import com.diefesson.flightmanager.control.EmbarkController;
import com.diefesson.flightmanager.control.FlightController;
import com.diefesson.flightmanager.control.PassengerController;
import com.diefesson.flightmanager.dto.EmbarkInfoDto;
import com.diefesson.flightmanager.dto.FlightDto;
import com.diefesson.flightmanager.dto.PassengerDto;
import com.diefesson.flightmanager.exception.FlightNotFoundException;
import com.diefesson.flightmanager.exception.PassengerNotFoundException;
import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.test.util.FlightBuilderUtil;

import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@Import(App.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class EmbarkControllerTest {

    private Flight flight;

    private Set<EmbarkInfoDto> regularEmbarkInfos;
    private Set<EmbarkInfoDto> vipEmbarkInfos;
    private Set<EmbarkInfoDto> embarkInfos;

    private Set<PassengerDto> passengerDtos;

    @Autowired
    public FlightController flightController;

    @Autowired
    public PassengerController passengerController;

    @Autowired
    public EmbarkController embarkController;

    @Autowired
    public ModelMapper mapper;

    private static final String UNKNOWN_FLIGHT_NUMBER = "AA7890";
    private static final String UNKNOWN_PASSENGER_ID = "321-654-9870";

    @BeforeEach
    @SneakyThrows
    public void setupFlight() {
        var mapper = new ModelMapper();
        flight = FlightBuilderUtil.loadFlight("AA1235");
        flightController.post(mapper.map(flight, FlightDto.class));
        for (var p : flight.getPassengers()) {
            var dto = mapper.map(p, PassengerDto.class);
            passengerController.post(dto);
        }
        regularEmbarkInfos = new HashSet<>();
        vipEmbarkInfos = new HashSet<>();
        embarkInfos = new HashSet<>();
        for (var p : flight.getPassengers()) {
            var embarkInfo = new EmbarkInfoDto(flight.getFlightNumber(), p.getId());
            if (p.isVip()) {
                vipEmbarkInfos.add(embarkInfo);
            } else {
                regularEmbarkInfos.add(embarkInfo);
            }
            embarkInfos.add(embarkInfo);
        }
        passengerDtos = flight
                .getPassengers()
                .stream()
                .map(p -> mapper.map(p, PassengerDto.class))
                .collect(Collectors.toSet());
    }

    // SYSTEM-EMBARK-00
    @Test
    @SneakyThrows
    public void embarkDisembarkTest() {
        // Embark passengers
        for (var ei : embarkInfos) {
            assertTrue(embarkController.embark(ei));
        }
        // Cannot embark a embarked passenger two times
        for (var ei : embarkInfos) {
            assertFalse(embarkController.embark(ei));
        }
        // Verify embarked passengers
        var persistedDtos = embarkController.listPassenger(flight.getFlightNumber());
        assertEquals(passengerDtos, new HashSet<>(persistedDtos));
        // Disembark regular passengers
        for (var ei : regularEmbarkInfos) {
            assertTrue(embarkController.disembark(ei));
        }
        // Cannot disembark a disembarked passenger
        for (var ei : regularEmbarkInfos) {
            assertFalse(embarkController.disembark(ei));
        }
        // Cannot disembark a vip passenger
        for (var ei : vipEmbarkInfos) {
            assertFalse(embarkController.disembark(ei));
        }
        // Checks all remaining passengers are vips
        assertTrue(embarkController.listPassenger(flight.getFlightNumber()).stream().allMatch(p -> p.isVip()));
    }

    // SYSTEM-EMBARK-01
    @Test
    public void invalidListPassengers() {
        assertNotEquals(UNKNOWN_FLIGHT_NUMBER, flight.getFlightNumber());
        assertThrows(FlightNotFoundException.class, () -> embarkController.listPassenger(UNKNOWN_FLIGHT_NUMBER));
    }

    // SYSTEM-EMBARK-02
    @Test
    public void invalidEmbarkDisembark() {
        assertNotEquals(UNKNOWN_FLIGHT_NUMBER, flight.getFlightNumber());
        assertTrue(flight.getPassengers().stream().allMatch(p -> !p.getId().equals(UNKNOWN_PASSENGER_ID)));
        var unknownFlightDto = new EmbarkInfoDto(UNKNOWN_FLIGHT_NUMBER, UNKNOWN_PASSENGER_ID);
        var unknownPassengerDto = new EmbarkInfoDto(flight.getFlightNumber(), UNKNOWN_PASSENGER_ID);
        assertThrows(FlightNotFoundException.class, () -> embarkController.embark(unknownFlightDto));
        assertThrows(PassengerNotFoundException.class, () -> embarkController.embark(unknownPassengerDto));
        assertThrows(FlightNotFoundException.class, () -> embarkController.disembark(unknownFlightDto));
        assertThrows(PassengerNotFoundException.class, () -> embarkController.disembark(unknownPassengerDto));
    }

}
