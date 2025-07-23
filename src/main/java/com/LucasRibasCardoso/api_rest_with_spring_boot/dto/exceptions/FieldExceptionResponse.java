package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FieldExceptionResponse", description = "Response for field validation exceptions")
public record FieldExceptionResponse(

    @Schema(description = "Field that caused the validation error", example = "cpf")
    String field,

    @Schema(description = "Error message for the field", example = "CPF is invalid")
    String message) {}
