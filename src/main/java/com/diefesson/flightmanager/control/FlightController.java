package com.diefesson.flightmanager.control;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.dto.FlightDto;
import com.diefesson.flightmanager.service.FlightService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("flight")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public FlightDto post(@RequestBody @Valid FlightDto flightDto) {
        return flightService.add(flightDto);
    }

    @GetMapping("{id}")
    public Optional<FlightDto> find(@PathVariable @Pattern(regexp = Regexes.PASSENGER_ID) String id) {
        return flightService.find(id);
    }

    @GetMapping
    public List<FlightDto> findAll() {
        return flightService.list();
    }

}
