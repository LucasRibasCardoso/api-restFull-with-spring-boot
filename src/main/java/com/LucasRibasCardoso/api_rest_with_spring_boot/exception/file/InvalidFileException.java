package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.file;

public class InvalidFileException extends RuntimeException {
  public InvalidFileException(String message) {
    super(message);
  }

  public InvalidFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
