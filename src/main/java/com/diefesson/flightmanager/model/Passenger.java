package com.diefesson.flightmanager.model;

import com.diefesson.flightmanager.constraints.IdMatchesCountry;

import java.util.Set;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.constraints.CountryCode;
import com.diefesson.flightmanager.constraints.IdCountryGet;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdMatchesCountry
@Entity
public class Passenger implements IdCountryGet {

    @Id
    @NotNull
    @Pattern(regexp = Regexes.PASSENGER_ID)
    private String id;

    @NotNull
    private String name;

    @NotNull
    @CountryCode
    private String countryCode;

    @ManyToMany
    private Set<Flight> flights;

    private boolean vip;

}
