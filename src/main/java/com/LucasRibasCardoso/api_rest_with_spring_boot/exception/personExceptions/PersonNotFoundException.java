package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions;

public class PersonNotFoundException extends RuntimeException {
  public PersonNotFoundException(String message) {
    super(message);
  }
}
