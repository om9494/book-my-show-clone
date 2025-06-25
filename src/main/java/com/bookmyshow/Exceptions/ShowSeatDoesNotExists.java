package com.bookmyshow.Exceptions;

public class ShowSeatDoesNotExists extends RuntimeException {
    public ShowSeatDoesNotExists() {
        super("Show Seat does not exists");
    }
}