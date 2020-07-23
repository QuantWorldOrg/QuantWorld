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
