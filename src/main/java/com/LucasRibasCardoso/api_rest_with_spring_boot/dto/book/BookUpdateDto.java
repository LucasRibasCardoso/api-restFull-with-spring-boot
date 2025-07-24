package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookUpdateDto(

    @Size(min = 1, message = "Title is required")
    String title,

    @Size(min = 1, message = "Author is required")
    String author,

    LocalDate launchDate,

    @Positive(message = "Price must be greater than zero")
    BigDecimal price

) {}
