package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
