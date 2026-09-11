package org.kashboikev.booklist.exceptions;

import org.kashboikev.booklist.account.UserNotFoundException;
import org.kashboikev.booklist.shelf.BookEntryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookEntryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookEntryNotFoundException(BookEntryNotFoundException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
