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
package com.quantworld.app.trader.oms.strategies.teststrategy;

import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.TickData;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.trader.oms.strategies.BaseStrategy;
import com.quantworld.app.trader.oms.strategies.StrategyParam;

/**
 * @author: Shawn
 * @Date: 12/7/2019
 * @Description:
 */
public class TestStrategy extends BaseStrategy {

  public TestStrategy(String strategyName, StrategyParam strategyParam) {
    super(strategyName, strategyParam);
  }

  @Override
  public void executeStrategy() {

  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  protected void onTrade(TradeData tradeData) {

  }

  @Override
  protected void onOrder(OrderData orderData) {

  }

  @Override
  protected void onTick(TickData tickData) {

  }

  @Override
  protected void onParam(StrategyParam strategyParam) {

  }

  @Override
  protected void onPosition(PositionData positionData) {

  }

  @Override
  protected void onAccount(AccountData accountData) {

  }

  @Override
  protected void onTimer() {

  }

  @Override
  protected void onStart() {

  }

  @Override
  protected void onStop() {

  }

}
