package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions;

import java.time.Instant;

public record DefaultResponseException(
    Instant timestamp, int status, String message, String path) {}
