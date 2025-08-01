package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions;

public class ExportFileException extends RuntimeException {

  public ExportFileException(String message) {
    super(message);
  }
}
