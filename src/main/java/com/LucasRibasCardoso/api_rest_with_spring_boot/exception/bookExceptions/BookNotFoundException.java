package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(String message) {
    super(message);
  }
}
