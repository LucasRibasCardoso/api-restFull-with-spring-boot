package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.security;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
  public InvalidJwtAuthenticationException(String message) {
    super(message);
  }
}
