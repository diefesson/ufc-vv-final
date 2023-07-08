package com.diefesson.flightmanager.dto;

import com.diefesson.flightmanager.constants.Regexes;

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
public class EmbarkInfoDto {

    @NotNull
    @Pattern(regexp = Regexes.FLIGHT_NUMBER)
    private String flightNumber;

    @NotNull
    @Pattern(regexp = Regexes.PASSENGER_ID)
    private String passengerId;

}
