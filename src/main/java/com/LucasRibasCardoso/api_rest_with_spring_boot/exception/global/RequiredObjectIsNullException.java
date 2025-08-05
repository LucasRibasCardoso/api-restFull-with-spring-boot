package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.global;

public class RequiredObjectIsNullException extends RuntimeException {

  public RequiredObjectIsNullException() {
    super("It is not allowed to persist a null object.");
  }
}
