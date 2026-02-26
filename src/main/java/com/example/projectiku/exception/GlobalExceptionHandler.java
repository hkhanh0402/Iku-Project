package com.example.projectiku.exception;

import com.example.projectiku.dto.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ================= NOT FOUND =================
    @ExceptionHandler(CustomResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(CustomResourceNotFoundException ex) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // ================= DUPLICATE =================
    @ExceptionHandler(CustomDuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(CustomDuplicateResourceException ex) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // ================= BAD REQUEST =================
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(CustomBadRequestException ex) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    // ================= VALIDATION =================
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .data(errors)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ================= JSON FORMAT ERROR =================
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Request body is invalid or wrong format")
                        .data(null)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ================= GLOBAL =================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                null
        );
    }

    // ================= COMMON BUILDER =================
    private ResponseEntity<ApiResponse<Object>> buildResponse(
            HttpStatus status,
            String message,
            Object data) {

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build(),
                status
        );
    }
}