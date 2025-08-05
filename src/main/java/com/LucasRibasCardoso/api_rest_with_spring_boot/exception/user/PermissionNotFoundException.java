package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.user;

public class PermissionNotFoundException extends RuntimeException {
  public PermissionNotFoundException(String message) {
    super(message);
  }
}
