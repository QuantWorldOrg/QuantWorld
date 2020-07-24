/*
 * Copyright 2019-2020 Shawn Peng
 * Email: shawnpeng@yeah.net
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
