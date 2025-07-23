package com.bookmyshow.Exceptions;

public class TicketDoesNotExist extends RuntimeException {
    public TicketDoesNotExist() {
        super("Ticket does not exist.");
    }
    public TicketDoesNotExist(String message) {
        super(message);
    }
}