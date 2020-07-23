package com.quantworld.app.broker.exception;

public class ServiceApplicationException extends RuntimeException{
  public ServiceApplicationException() {
  }

  public ServiceApplicationException(String message) {
    super(message);
  }

  public ServiceApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceApplicationException(Throwable cause) {
    super(cause);
  }

  public ServiceApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
