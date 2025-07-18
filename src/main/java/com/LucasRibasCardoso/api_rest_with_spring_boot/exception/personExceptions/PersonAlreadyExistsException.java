package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions;

public class PersonAlreadyExistsException extends RuntimeException {
  public PersonAlreadyExistsException(String message) {
    super(message);
  }
}
