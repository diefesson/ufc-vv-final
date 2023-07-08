package com.diefesson.flightmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.diefesson.flightmanager.dto.FlightDto;
import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.repository.FlightRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    private final ModelMapper modelMapper;

    public FlightDto add(FlightDto flightDto) {
        var flight = modelMapper.map(flightDto, Flight.class);
        flight.setPassengers(Set.of());
        return modelMapper.map(flightRepository.save(flight), FlightDto.class);
    }

    public Optional<FlightDto> find(String id) {
        var flight = flightRepository.findById(id);
        if (flight.isPresent()) {
            return Optional.of(modelMapper.map(flight.get(), FlightDto.class));
        } else {
            return Optional.empty();
        }
    }

    public List<FlightDto> list() {
        return flightRepository
                .findAll()
                .stream()
                .map(p -> modelMapper.map(p, FlightDto.class))
                .toList();
    }

}
