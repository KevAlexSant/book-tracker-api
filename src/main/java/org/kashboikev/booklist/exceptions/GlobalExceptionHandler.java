package org.kashboikev.booklist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("User not found");
        return problem;
    }

    @ExceptionHandler(BookEntryNotFoundException.class)
    public ProblemDetail handleBookEntryNotFoundException(BookEntryNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("Book entry not found");
        return problem;
    }

    @ExceptionHandler(UsernameNotAvailable.class)
    public ProblemDetail handleUsernameNotAvailable(UsernameNotAvailable e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, e.getMessage());
        problem.setTitle("Username not available");
        return problem;
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ProblemDetail handlePasswordsDoNotMatchException(PasswordsDoNotMatchException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        problem.setTitle("Passwords do not match");
        return problem;
    }

    @ExceptionHandler(BookEntryAlreadyExistsException.class)
    public ProblemDetail handleBookEntryAlreadyExists(BookEntryAlreadyExistsException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, e.getMessage());
        problem.setTitle("Book entry already exists");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more fields failed validation"
        );

        problem.setTitle("Validation failed");
        problem.setProperty("errors", errors);

        return problem;
    }

}
