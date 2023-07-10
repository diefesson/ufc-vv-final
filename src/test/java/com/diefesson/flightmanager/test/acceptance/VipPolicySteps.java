package com.diefesson.flightmanager.test.acceptance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.diefesson.flightmanager.model.Flight;
import com.diefesson.flightmanager.model.Passenger;
import com.diefesson.flightmanager.test.util.FlightBuilderUtil;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;

public class VipPolicySteps {

    private Flight flight;

    private List<Passenger> regularPassengers;
    private List<Passenger> vipPassengers;

    private Flight anotherFlight = Flight
            .builder()
            .flightNumber("AA7890")
            .seats(48)
            .build();

    @SneakyThrows
    @Given("there is a flight having number {string} with passengers defined in the corresponding file")
    public void there_is_a_flight_having_number_with_passengers_defined_in_the_corresponding_file(String flightNumber) {
        flight = FlightBuilderUtil.loadFlight(flightNumber);
    }

    @When("we have regular passengers")
    public void we_have_regular_passengers() {
        regularPassengers = flight.getPassengers().stream().filter(p -> !p.isVip()).toList();
    }

    @SneakyThrows
    @Then("you can remove them from the flight")
    public void you_can_remove_them_from_the_flight() {
        for (var p : regularPassengers) {
            flight.removePassenger(p);
        }
    }

    @SneakyThrows
    @Then("add them to another flight")
    public void add_them_to_another_flight() {
        for (var p : regularPassengers) {
            assertTrue(anotherFlight.addPassenger(p));
        }
    }

    @When("we have vip passengers")
    public void we_have_vip_passengers() {
        vipPassengers = flight.getPassengers().stream().filter(p -> p.isVip()).toList();
    }

    @SneakyThrows
    @Then("you cannot remove them from the flight")
    public void you_cannot_remove_them_from_the_flight() {
        for (Passenger passenger : vipPassengers) {
            assertFalse(flight.removePassenger(passenger));
        }
    }

}
