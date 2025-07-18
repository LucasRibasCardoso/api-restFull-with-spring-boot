package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions;

import java.time.Instant;
import java.util.List;

public record ValidationExceptionResponse(
    Instant timestamp,
    int status,
    String message,
    String path,
    List<FieldExceptionResponse> errors) {}
