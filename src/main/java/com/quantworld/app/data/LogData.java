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
package com.quantworld.app.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class LogData extends BaseData {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String msg;

  public String getMsg() {
    return msg;
  }

  public LogData(String message, String gatewayName) {
    this.msg = message;
    this.gatewayName = gatewayName;
  }


  //TODO log level

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Date getTime() {
    return new Date();
  }

  public LogData deepClone() {
    return (LogData) super.deepClone();
  }

  @Override
  public String toString() {
    return "LogData{" +
        "msg='" + msg + '\'' +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
