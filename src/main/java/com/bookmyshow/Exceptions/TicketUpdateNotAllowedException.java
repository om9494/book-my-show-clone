package com.bookmyshow.Exceptions;

public class TicketUpdateNotAllowedException extends RuntimeException {
    public TicketUpdateNotAllowedException() {
        super("Ticket update is not allowed at this time.");
    }
    public TicketUpdateNotAllowedException(String message) {
        super(message);
    }
}