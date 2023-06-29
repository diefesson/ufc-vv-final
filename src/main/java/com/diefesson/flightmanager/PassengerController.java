package com.diefesson.flightmanager;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.dto.PassengerDto;
import com.diefesson.flightmanager.service.PassengerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("passenger")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    public PassengerDto post(@RequestBody @Valid PassengerDto passengerDto) {
        return passengerService.add(passengerDto);
    }

    @GetMapping("{id}")
    public Optional<PassengerDto> find(@PathVariable @Pattern(regexp = Regexes.PASSENGER_ID) String id) {
        return passengerService.find(id);
    }

    @GetMapping
    public List<PassengerDto> findAll() {
        return passengerService.list();
    }

}
