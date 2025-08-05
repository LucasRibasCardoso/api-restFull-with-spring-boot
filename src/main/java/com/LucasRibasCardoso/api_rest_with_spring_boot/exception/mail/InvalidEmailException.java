package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.mail;

public class InvalidEmailException extends RuntimeException {
  public InvalidEmailException(String message) {
    super(message);
  }
}
