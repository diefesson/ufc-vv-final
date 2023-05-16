package com.diefesson.fm;

import javax.enterprise.inject.Produces;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class FlightProducer {
    @Produces
    public Flight createFlight() throws IOException {
        return FlightBuilderUtil.buildFlightFromCsv();
    }
}
