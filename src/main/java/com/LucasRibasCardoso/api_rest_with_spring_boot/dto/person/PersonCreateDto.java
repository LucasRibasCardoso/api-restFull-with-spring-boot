package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record PersonCreateDto(
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @CPF(message = "Invalid CPF format! Use the format 000.000.000-00")
        @NotBlank(message = "CPF is required")
        String cpf,
    @NotNull(message = "Gender is required") Gender gender) {}
