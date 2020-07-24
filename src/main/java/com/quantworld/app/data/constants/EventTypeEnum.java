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
