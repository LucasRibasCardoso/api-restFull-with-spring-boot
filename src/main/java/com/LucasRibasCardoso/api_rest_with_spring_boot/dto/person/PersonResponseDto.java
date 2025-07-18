package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;

public record PersonResponseDto(
    Long id, String firstName, String lastName, String cpf, Gender gender) {}
