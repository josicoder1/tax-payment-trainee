package com.example.tax_payment.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(Exception ex) {

        return new ApiErrorResponse(
                ex.getMessage(),
                "NOT_FOUND",
                Instant.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleGeneric(Exception ex) {

        return new ApiErrorResponse(
                ex.getMessage(),
                "BAD_REQUEST",
                Instant.now()
        );
    }
}