package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException(String message) {
    super(message);
  }
}
