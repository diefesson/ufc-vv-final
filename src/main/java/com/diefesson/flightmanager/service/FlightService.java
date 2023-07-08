package com.diefesson.flightmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.diefesson.flightmanager.dto.EmbarkInfoDto;
import com.diefesson.flightmanager.dto.FlightDto;
import com.diefesson.flightmanager.dto.PassengerDto;
import com.diefesson.flightmanager.exception.FlightNotFoundException;
import com.diefesson.flightmanager.exception.ModelException;
import com.diefesson.flightmanager.exception.PassengerNotFoundException;
import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.repository.FlightRepository;
import com.diefesson.flightmanager.repository.PassengerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

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

    @Transactional
    public boolean embark(EmbarkInfoDto embarkDto)
            throws FlightNotFoundException, PassengerNotFoundException, ModelException {
        var flightOptional = flightRepository.findById(embarkDto.getFlightNumber());
        var passengerOptional = passengerRepository.findById(embarkDto.getPassengerId());
        if (flightOptional.isEmpty()) {
            throw new FlightNotFoundException();
        }
        if (passengerOptional.isEmpty()) {
            throw new PassengerNotFoundException();
        }
        var flight = flightOptional.get();
        var passenger = passengerOptional.get();

        var result = flight.addPassenger(passenger);
        flightRepository.save(flight);
        return result;
    }

    @Transactional
    public boolean disembark(EmbarkInfoDto embarkDto)
            throws PassengerNotFoundException, FlightNotFoundException, ModelException {
        var flightOptional = flightRepository.findById(embarkDto.getFlightNumber());
        var passengerOptional = passengerRepository.findById(embarkDto.getPassengerId());
        if (flightOptional.isEmpty()) {
            throw new FlightNotFoundException();
        }
        if (passengerOptional.isEmpty()) {
            throw new PassengerNotFoundException();
        }
        var flight = flightOptional.get();
        var passenger = passengerOptional.get();
        var result = flight.removePassenger(passenger);
        flightRepository.save(flight);
        return result;
    }

    @Transactional
    public List<PassengerDto> listPassengers(String id) throws FlightNotFoundException {
        var flight = flightRepository.findById(id);
        if (flight.isEmpty()) {
            throw new FlightNotFoundException();
        }
        return flight
                .get()
                .getPassengers()
                .stream()
                .map(p -> modelMapper.map(p, PassengerDto.class))
                .collect(Collectors.toList());
    }

}
