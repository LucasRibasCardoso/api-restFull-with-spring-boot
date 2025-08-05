package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.file;

public class UnsupportedFileExtensionException extends RuntimeException {
  public UnsupportedFileExtensionException(String message) {
    super(message);
  }
}
