package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "DefaultResponseException", description = "Default response for exceptions")
public record DefaultResponseException(
    @Schema(description = "Timestamp of the exception", example = "2023-10-01T12:00:00Z")
    Instant timestamp,

    @Schema(description = "HTTP status code", example = "404")
    int status,

    @Schema(description = "Error message", example = "Message describing the error")
    String message,

    @Schema(description = "Path of the request that caused the exception", example = "/api/v1/resource/{id}")
    String path) {}
