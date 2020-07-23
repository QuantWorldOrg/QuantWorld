package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/24/2019
 * @Description:
 */
public enum EventTypeEnum {
  EVENT_NONE("noneEvent"),
  EVENT_TICK("eTick."),
  EVENT_TRADE("eTrade."),
  EVENT_ORDER("eOrder."),
  EVENT_POSITION("ePosition."),
  EVENT_ACCOUNT("eAccount."),
  EVENT_CONTRACT("eContract."),
  EVENT_LOG("eLog."),
  EVENT_EMAIL("eEmail."),
  EVENT_TIMER("eTimer."),

  // strategy
  EVENT_STRATEGY_LOG("eStrategyLog."),
  EVENT_STRATEGY_PARAM("eStrategyParam."),
  EVENT_STRATEGY_SETTING_UPDATED("eStrategySetting."),
  EVENT_STRATEGY_SETTING_DELETED("eStrategyVariables."),
  EVENT_STRATEGY_SETTING_SAVED("eStrategyParameters."),
  EVENT_STRATEGY_SETTING_LOADING("eStrategyParameters.");

  String value;

  EventTypeEnum(String value) {
    this.value = value;
  }
}
