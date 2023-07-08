package com.diefesson.flightmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@StandardException
public class PassengerNotFoundException extends FlightManagerException {
}
