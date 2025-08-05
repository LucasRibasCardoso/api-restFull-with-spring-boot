package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security;

import java.time.LocalDateTime;

public record TokenResponseDto(
    String username,
    Boolean authenticated,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    String accessToken,
    String refreshToken) {}
