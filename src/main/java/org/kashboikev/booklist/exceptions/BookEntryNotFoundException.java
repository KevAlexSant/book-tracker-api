package org.kashboikev.booklist.exceptions;

public class BookEntryNotFoundException extends RuntimeException {
    public BookEntryNotFoundException(String message) {
        super(message);
    }
}
