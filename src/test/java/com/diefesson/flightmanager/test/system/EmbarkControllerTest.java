package com.diefesson.flightmanager.test.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
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
import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.test.util.FlightBuilderUtil;

import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@Import(App.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class EmbarkControllerTest {

    private Flight flight;

    @Autowired
    public FlightController flightController;

    @Autowired
    public PassengerController passengerController;

    @Autowired
    public EmbarkController embarkController;

    @Autowired
    public ModelMapper mapper;

    @BeforeEach
    @SneakyThrows
    public void setupFlight() {
        var mapper = new ModelMapper();
        flight = FlightBuilderUtil.readFlights("AA1235");
        flightController.post(mapper.map(flight, FlightDto.class));
        for (var p : flight.getPassengers()) {
            var dto = mapper.map(p, PassengerDto.class);
            passengerController.post(dto);
        }
    }

    @Test
    @SneakyThrows
    public void embarkDisembarkTest() {
        var embarkInfos = flight
                .getPassengers()
                .stream()
                .map(p -> new EmbarkInfoDto(flight.getFlightNumber(), p.getId()))
                .collect(Collectors.toSet());
        var passengerDtos = flight
                .getPassengers()
                .stream()
                .map(p -> mapper.map(p, PassengerDto.class))
                .collect(Collectors.toSet());
        for (var ei : embarkInfos) {
            assertTrue(embarkController.embark(ei));
        }
        for (var ei : embarkInfos) {
            assertFalse(embarkController.embark(ei));
        }
        var persistedDtos = embarkController.listPassenger(flight.getFlightNumber());
        assertEquals(passengerDtos, new HashSet<>(persistedDtos));
        for (var ei : embarkInfos) {
            assertTrue(embarkController.disembark(ei));
        }
        for (var ei : embarkInfos) {
            assertFalse(embarkController.disembark(ei));
        }
        assertTrue(embarkController.listPassenger(flight.getFlightNumber()).isEmpty());
    }

}
