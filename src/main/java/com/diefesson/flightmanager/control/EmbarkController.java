package com.diefesson.flightmanager.control;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.dto.EmbarkInfoDto;
import com.diefesson.flightmanager.dto.PassengerDto;
import com.diefesson.flightmanager.exception.FlightNotFoundException;
import com.diefesson.flightmanager.exception.ModelException;
import com.diefesson.flightmanager.exception.PassengerNotFoundException;
import com.diefesson.flightmanager.service.FlightService;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class EmbarkController {

    private final FlightService flightService;

    @PostMapping("embark")
    public boolean embark(EmbarkInfoDto embarkDto)
            throws FlightNotFoundException, PassengerNotFoundException, ModelException {
        return flightService.embark(embarkDto);
    }

    @PostMapping("disembark")
    public boolean disembark(EmbarkInfoDto embarkDto)
            throws PassengerNotFoundException, FlightNotFoundException, ModelException {
        return flightService.disembark(embarkDto);
    }

    @GetMapping("flight/{id}/passengers")
    public List<PassengerDto> listPassenger(@PathVariable @Pattern(regexp = Regexes.FLIGHT_NUMBER) String id)
            throws FlightNotFoundException {
        return flightService.listPassengers(id);
    }

}
