package org.kashboikev.booklist.exceptions;

public class BookEntryAlreadyExistsException extends RuntimeException {
    public BookEntryAlreadyExistsException(String message) {
        super(message);
    }
}
