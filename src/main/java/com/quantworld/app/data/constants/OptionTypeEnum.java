package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public enum OptionTypeEnum {
  NONE("NONE"),
  CALL("看涨期权"),
  PUT("看跌期权");
  String value;

  OptionTypeEnum(String value) {
    this.value = value;
  }
}
