package com.quantworld.app.data.constants;


public enum OrderTypeEnum {

  NONE("NONE"),
  LIMIT("限价"),
  MARKET("市价"),
  STOP("STOP"),
  FAK("FAK"),
  FOK("FOK"),
  BLP("BLP"),
  VWAP("VWAP"),
  TWAP("TWAP"),
  SOR("SOR");
  String value;

  OrderTypeEnum(String value) {
    this.value = value;
  }
}
