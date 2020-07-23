package com.quantworld.app.broker.exception;

public class BrokerApiException extends RuntimeException {


  public static final String RUNTIME_ERROR = "RuntimeError";
  public static final String INPUT_ERROR = "InputError";
  public static final String KEY_MISSING = "KeyMissing";
  public static final String SYS_ERROR = "SysError";
  public static final String SUBSCRIPTION_ERROR = "SubscriptionError";
  public static final String ENV_ERROR = "EnvironmentError";
  public static final String EXEC_ERROR = "ExecuteError";
  private final String errCode;

  public BrokerApiException(String errType, String errMsg) {
    super(errMsg);
    this.errCode = errType;
  }

  public BrokerApiException(String errType, String errMsg, Throwable e) {
    super(errMsg, e);
    this.errCode = errType;
  }

  public String getErrType() {
    return this.errCode;
  }
}
