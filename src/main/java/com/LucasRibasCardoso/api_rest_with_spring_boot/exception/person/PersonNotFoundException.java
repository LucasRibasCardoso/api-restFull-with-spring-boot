package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.person;

public class PersonNotFoundException extends RuntimeException {
  public PersonNotFoundException(String message) {
    super(message);
  }
}
