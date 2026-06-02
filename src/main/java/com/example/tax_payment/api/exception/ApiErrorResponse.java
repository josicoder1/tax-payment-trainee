package com.example.tax_payment.api.exception;

import java.time.Instant;

public record ApiErrorResponse(
        String message,
        String errorCode,
        Instant timestamp
) {}