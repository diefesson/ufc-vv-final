package com.diefesson.flightmanager.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.diefesson.flightmanager.dto.PassengerDto;
import com.diefesson.flightmanager.model.Passenger;
import com.diefesson.flightmanager.repository.PassengerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public PassengerDto add(PassengerDto passengerDto) {
        var passenger = modelMapper.map(passengerDto, Passenger.class);
        return modelMapper.map(passengerRepository.save(passenger), PassengerDto.class);
    }

    @Transactional
    public Optional<PassengerDto> find(String id) {
        var passenger = passengerRepository.findById(id);
        if (passenger.isPresent()) {
            return Optional.of(modelMapper.map(passenger.get(), PassengerDto.class));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public List<PassengerDto> list() {
        return passengerRepository
                .findAll()
                .stream()
                .map(p -> modelMapper.map(p, PassengerDto.class))
                .toList();
    }

}
