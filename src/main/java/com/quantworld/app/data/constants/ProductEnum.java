package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public enum ProductEnum {
  NONE("NONE"),
  EQUITY("股票"),
  FUTURES("期货"),
  OPTION("期权"),
  INDEX("指数"),
  FOREX("外汇"),
  SPOT("现货"),
  ETF("ETF"),
  BOND("债券"),
  WARRANT("权证"),
  SPREAD("价差"),
  FUND("基金");

  String value;

  ProductEnum(String value) {
    this.value = value;
  }
}
