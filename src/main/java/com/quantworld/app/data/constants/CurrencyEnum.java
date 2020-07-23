package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public enum CurrencyEnum {
  USD("USD"),
  HKD("HKD"),
  CNY("CNY");

  String value;

  CurrencyEnum(String value) {
    this.value = value;
  }
}
