package com.diefesson.flightmanager.constants;

public class Regexes {

    // Passenger related
    public static final String SSN = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
    public static final String NON_US_IDENTIFIER = "^(?!000|666)[9][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";

    // Flight related
    public static final String FLIGHT_NUMBER = "^[A-Z]{2}\\d{3,4}$";

    private Regexes() {
    }

}
