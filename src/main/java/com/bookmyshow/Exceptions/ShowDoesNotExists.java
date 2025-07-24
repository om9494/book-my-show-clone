package com.bookmyshow.Exceptions;

public class ShowDoesNotExists extends RuntimeException {
    public ShowDoesNotExists() {
        super("Show does not exist.");
    }
    public ShowDoesNotExists(String message) {
        super(message);
    }
}