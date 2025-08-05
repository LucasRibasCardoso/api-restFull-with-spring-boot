package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.mail;

public class SendFailedEmailException extends RuntimeException {
  public SendFailedEmailException(String message) {
    super(message);
  }
}
