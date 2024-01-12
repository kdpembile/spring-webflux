package com.kentisthebest.exceptions;

public class ReviewNotFoundException extends RuntimeException {

  public ReviewNotFoundException(String message) {
    super(message);
  }

  public ReviewNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
