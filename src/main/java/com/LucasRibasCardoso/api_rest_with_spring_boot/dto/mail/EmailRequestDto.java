package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.mail;

import jakarta.validation.constraints.NotBlank;

public record EmailRequestDto(
    @NotBlank(message = "To address email is required") String to,
    @NotBlank(message = "Subject is required") String subject,
    String body) {}
