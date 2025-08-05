package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
