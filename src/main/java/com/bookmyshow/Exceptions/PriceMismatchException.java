package com.bookmyshow.Exceptions;

public class PriceMismatchException extends RuntimeException {
    public PriceMismatchException() {
        super("The price of the new ticket does not match the original ticket price.");
    }
    public PriceMismatchException(String message) {
        super(message);
    }
}