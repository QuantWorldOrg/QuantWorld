package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description: Offset of order/trade.
 */
public enum OffsetEnum {
  NONE("NONE"),
  OPEN("开"),
  CLOSE("平"),
  CLOSETODAY("平今"),
  CLOSEYESTERDAY("平昨");
  String value;

  OffsetEnum(String value) {
    this.value = value;
  }
}
