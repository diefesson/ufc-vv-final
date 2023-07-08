package com.diefesson.flightmanager.model;

import java.util.HashSet;
import java.util.Set;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.constraints.SeatingLimit;
import com.diefesson.flightmanager.exception.ModelException;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log
@SeatingLimit
@Entity
public class Flight {

    @Id
    @NotNull
    @Pattern(regexp = Regexes.FLIGHT_NUMBER)
    private String flightNumber;

    @Positive
    @Builder.Default
    private int seats = 1;

    private String origin;

    private String destination;

    @NotNull
    @Builder.Default
    private FlightStatus status = FlightStatus.AT_ORIGIN;

    @NotNull
    @ToString.Exclude
    @ManyToMany
    private Set<Passenger> passengers;

    public int getPassengersCount() {
        return passengers.size();
    }

    public boolean addPassenger(Passenger passenger) throws ModelException {
        if (status != FlightStatus.AT_ORIGIN) {
            throw new ModelException("cannot add passengers any longer");
        }
        passengers = new HashSet<>(getPassengers());
        var result = passengers.add(passenger);
        setPassengers(passengers);
        return result;
    }

    public boolean removePassenger(Passenger passenger) throws ModelException {
        if (status != FlightStatus.AT_ORIGIN) {
            throw new ModelException("cannot remove passengers any longer");
        }
        passengers = new HashSet<>(passengers);
        var result = passengers.remove(passenger);
        setPassengers(passengers);
        return result;
    }

    public void setOrigin(String origin) throws ModelException {
        if (status != FlightStatus.AT_ORIGIN) {
            throw new ModelException("flight cannot change its origin any longer");
        }
        this.origin = origin;
    }

    public void setDestination(String destination) throws ModelException {
        if (status == FlightStatus.AT_DESTINATION) {
            throw new ModelException("flight cannot change it's destination any longer");
        }
        this.destination = destination;
    }

    public void takeOff() throws ModelException {
        if (origin == null) {
            throw new ModelException("flight cannot take off without origin");
        }
        if (status != FlightStatus.AT_ORIGIN) {
            throw new ModelException("flight must be at origin to take off");
        }
        log.info(this + " is taking off");
        status = FlightStatus.IN_FLIGHT;
    }

    public void land() throws ModelException {
        if (destination == null) {
            throw new ModelException("flight cannot land without destination");
        }
        if (status != FlightStatus.IN_FLIGHT) {
            throw new ModelException("flight must be in flight to land");
        }
        log.info(this + " is landing");
        status = FlightStatus.AT_DESTINATION;
    }
}
