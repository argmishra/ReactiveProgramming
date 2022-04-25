package com.demo.pratice;

public class ReactorException extends Throwable {

  private final Throwable throwable;
  private final String message;

  public ReactorException(Throwable throwable, String message) {
    this.message = message;
    this.throwable = throwable;
  }
}
