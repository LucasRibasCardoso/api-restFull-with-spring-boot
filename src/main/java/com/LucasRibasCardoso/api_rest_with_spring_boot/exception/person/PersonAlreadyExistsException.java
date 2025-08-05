package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.person;

public class PersonAlreadyExistsException extends RuntimeException {
  public PersonAlreadyExistsException(String message) {
    super(message);
  }
}
