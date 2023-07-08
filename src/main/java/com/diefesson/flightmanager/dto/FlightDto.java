package com.diefesson.flightmanager.dto;

import com.diefesson.flightmanager.constants.Regexes;
import com.diefesson.flightmanager.model.FlightStatus;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDto {

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

}
