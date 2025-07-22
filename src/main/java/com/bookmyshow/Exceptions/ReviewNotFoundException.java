package com.bookmyshow.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested review is not found in the system.
 * Results in an HTTP 404 (Not Found) response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {

    /**
     * Constructs a new ReviewNotFoundException with the specified detail message.
     * @param message The detail message, which is saved for later retrieval by the getMessage() method.
     */
    public ReviewNotFoundException(String message) {
        super(message);
    }
}