package com.diefesson.flightmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diefesson.flightmanager.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, String> {

}
