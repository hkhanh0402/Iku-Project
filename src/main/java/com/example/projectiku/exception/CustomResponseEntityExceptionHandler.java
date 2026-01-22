package com.example.projectiku.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDetails> handleNotFoundException(
            CustomResourceNotFoundException ex,
            WebRequest request) {

        CustomErrorDetails errorDetails = new CustomErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        CustomErrorDetails errorDetails = new CustomErrorDetails(
                LocalDateTime.now(),
                "Validation failed",
                errors
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDetails> handleAllExceptions(
            Exception ex,
            WebRequest request) {

        CustomErrorDetails errorDetails = new CustomErrorDetails(
                LocalDateTime.now(),
                "Internal server error",
                ex.getMessage()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomDuplicateResourceException.class)
    public ResponseEntity<CustomErrorDetails> handleDuplicateResourceException(
            CustomDuplicateResourceException ex,
            WebRequest request) {

        CustomErrorDetails errorDetails = new CustomErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}

