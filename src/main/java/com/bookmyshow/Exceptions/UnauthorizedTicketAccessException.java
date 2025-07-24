package com.bookmyshow.Exceptions;

public class UnauthorizedTicketAccessException extends RuntimeException {
    public UnauthorizedTicketAccessException() {
        super("You are not authorized to update this ticket.");
    }
    public UnauthorizedTicketAccessException(String message) {
        super(message);
    }
}