package com.quantworld.app.data.constants;

/**
 * IntervalEnum of bar data.
 */

public enum IntervalEnum {
  NONE("NONE"),
  MINUTE("1m"),
  HOUR("1h"),
  DAILY("d"),
  WEEKLY("w");

  String value;

  IntervalEnum(String value) {
    this.value = value;
  }
}
