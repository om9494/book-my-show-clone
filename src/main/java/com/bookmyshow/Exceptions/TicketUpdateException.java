package com.bookmyshow.Exceptions;

/**
 * Custom exception for handling errors related to the ticket update process.
 */
public class TicketUpdateException extends RuntimeException {
    public TicketUpdateException(String message) {
        super(message);
    }
}