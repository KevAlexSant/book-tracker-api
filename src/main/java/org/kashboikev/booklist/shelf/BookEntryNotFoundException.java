package org.kashboikev.booklist.shelf;

public class BookEntryNotFoundException extends RuntimeException {
    public BookEntryNotFoundException(String message) {
        super(message);
    }
}
