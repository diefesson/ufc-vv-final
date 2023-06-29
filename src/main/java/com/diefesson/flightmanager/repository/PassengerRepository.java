package com.diefesson.flightmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diefesson.flightmanager.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
}
