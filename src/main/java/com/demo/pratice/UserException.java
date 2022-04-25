package com.demo.pratice;


public class UserException extends RuntimeException {
  String message;

  public UserException(String message) {
    super(message);
    this.message = message;
  }

  public UserException(Throwable ex) {
    super(ex);
    this.message = ex.getMessage();
  }
}