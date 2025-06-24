package com.bookmyshow.Exceptions;

public class RequestedSeatAreNotAvailable extends RuntimeException{
    public RequestedSeatAreNotAvailable() {
        super("Requested Seats Are Not Available");
    }
}
