package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions;

public class BookAlreadyExistsException extends RuntimeException {
  public BookAlreadyExistsException(String message) {
    super(message);
  }
}
