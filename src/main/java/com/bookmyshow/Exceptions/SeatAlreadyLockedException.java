package com.bookmyshow.Exceptions;

// This exception is thrown when a user tries to lock a seat that is already locked by another user.
public class SeatAlreadyLockedException extends RuntimeException {
    public SeatAlreadyLockedException(String message) {
        super(message);
    }
}