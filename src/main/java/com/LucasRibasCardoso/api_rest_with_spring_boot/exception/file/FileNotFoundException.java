package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.file;

public class FileNotFoundException extends RuntimeException {
  public FileNotFoundException(String message) {
    super(message);
  }

  public FileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
