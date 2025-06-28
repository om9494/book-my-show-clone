package com.bookmyshow.Exceptions;

public class UserDoesNotExist extends RuntimeException {
    
    public UserDoesNotExist() {
        super("User does not exist with the provided ID.");
    }

    public UserDoesNotExist(String message) {
        super(message);
    }
}
