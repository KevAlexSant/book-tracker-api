package org.kashboikev.booklist.exceptions;

public class InvalidRatingValueException extends RuntimeException {
    public InvalidRatingValueException(String message) {
        super(message);
    }
}
