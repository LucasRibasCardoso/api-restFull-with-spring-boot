package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.global;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.ValidationExceptionResponse;

public class CustomValidationException extends RuntimeException {
  private final ValidationExceptionResponse validationResponse;

  public CustomValidationException(ValidationExceptionResponse validationResponse) {
    super(validationResponse.message());
    this.validationResponse = validationResponse;
  }

  public ValidationExceptionResponse getValidationResponse() {
    return this.validationResponse;
  }
}
