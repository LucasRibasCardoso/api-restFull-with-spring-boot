package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BookCreateDto(
    @NotBlank(message = "Title is required") String title,
    @NotBlank(message = "Author is required") String author,
    @NotNull(message = "Launch date is required") LocalDate launchDate,
    @NotNull(message = "Price is required") @Positive(message = "Price must be positive")
        BigDecimal price) {}
