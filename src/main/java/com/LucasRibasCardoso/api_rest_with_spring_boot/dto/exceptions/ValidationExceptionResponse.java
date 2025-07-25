package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Schema(name = "ValidationExceptionResponse", description = "Response for validation exceptions")
public record ValidationExceptionResponse(
    @Schema(description = "Timestamp of the exception occurrence", example = "2023-10-01T12:00:00Z")
        Instant timestamp,
    @Schema(description = "HTTP status code", example = "400") int status,
    @Schema(description = "Error message", example = "Validation failed") String message,
    @Schema(
            description = "Path of the request that caused the exception",
            example = "/api/v1/resource")
        String path,
    @Schema(description = "List of field errors") List<FieldExceptionResponse> errors) {}
