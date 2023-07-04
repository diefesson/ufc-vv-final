package com.diefesson.flightmanager.dto;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.constraints.CountryCode;
import com.diefesson.flightmanager.constraints.IdCountryGet;
import com.diefesson.flightmanager.constraints.IdMatchesCountry;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdMatchesCountry
public class PassengerDto implements IdCountryGet {

    @NotNull
    @Pattern(regexp = Regexes.PASSENGER_ID)
    private String id;

    @NotNull
    private String name;

    @NotNull
    @CountryCode
    private String countryCode;

}
