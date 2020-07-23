package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/16/2019
 * @Description:
 */
public enum DirectionEnum {
  NONE("NONE"),
  LONG("buy"),
  SHORT("sell"),
  NET("净");

  String value;

  DirectionEnum(String value) {
    this.value = value;
  }
}
