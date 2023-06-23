package com.diefesson.flightmanager.model;

import com.diefesson.flightmanager.constraints.IdMatchesCountry;
import com.diefesson.flightmanager.constraints.CountryCode;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IdMatchesCountry
public class Passenger {

    @Id
    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    @CountryCode
    private String countryCode;

}
