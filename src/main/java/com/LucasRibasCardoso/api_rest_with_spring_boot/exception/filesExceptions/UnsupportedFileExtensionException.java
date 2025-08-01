package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions;

public class UnsupportedFileExtensionException extends RuntimeException {
  public UnsupportedFileExtensionException(String message) {
    super(message);
  }
}
