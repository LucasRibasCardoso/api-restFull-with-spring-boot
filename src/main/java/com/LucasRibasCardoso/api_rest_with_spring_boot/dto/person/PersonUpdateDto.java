package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record PersonUpdateDto(
    @Size(min = 1, message = "First name is required") String firstName,
    @Size(min = 1, message = "Last name is required") String lastName,
    @CPF(message = "Invalid CPF format") String cpf,
    Gender gender) {}
