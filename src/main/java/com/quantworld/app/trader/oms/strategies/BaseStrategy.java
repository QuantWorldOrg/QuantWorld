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
package com.quantworld.app.trader.oms.strategies;

import com.quantworld.app.QWContext;
import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.TickData;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.OffsetEnum;
import com.quantworld.app.data.constants.OrderTypeEnum;
import com.quantworld.app.trader.oms.EventProcessor;
import com.quantworld.app.utils.MathUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 11/17/2019
 * @Description:
 */
public abstract class BaseStrategy {

  protected boolean isActive = false;
  protected Map<String, OrderData> activeOrders;
  protected Map<String, OrderData> inactiveOrders;
  protected EventProcessor eventProcessor;
  protected StrategyParam strategyParam;
  protected String strategyName;

  public BaseStrategy(String strategyName, StrategyParam strategyParam) {
    this.eventProcessor = (EventProcessor) QWContext.getBean(EventProcessor.NAME);
    this.strategyParam = strategyParam;
    this.activeOrders = new ConcurrentHashMap<>();
    this.strategyName = strategyName;
  }

  /**
   * 需要异步处理
   */
  public abstract void executeStrategy();


  public abstract String getName();

  public void updateTick(TickData tickData) {
    if (isActive) {
      onTick(tickData);
    }
  }

  public void updateOrder(OrderData orderData) {
    if (isActive) {
      if (orderData.isActive()) {
        activeOrders.put(orderData.getQwOrderId(), orderData);
      } else {
        activeOrders.remove(orderData.getQwOrderId());
      }
      onOrder(orderData);
    }
  }

  public void updateTrade(TradeData tradeData) {
    if (isActive) {
      onTrade(tradeData);
    }
  }

  public void updatePosition(PositionData positionData) {
    if (isActive) {
      onPosition(positionData);
    }
  }

  public void updateParam(StrategyParam strategyParam) {
    if (isActive) {
      onParam(strategyParam);
    }
  }

  public void updateTimer() {
    if (isActive) {
      onTimer();
    }
  }

  protected abstract void onTrade(TradeData tradeData);

  protected abstract void onOrder(OrderData orderData);

  protected abstract void onTick(TickData tickData);

  protected abstract void onParam(StrategyParam strategyParam);

  protected abstract void onPosition(PositionData positionData);

  protected abstract void onAccount(AccountData accountData);

  protected abstract void onTimer();

  protected abstract void onStart();

  protected abstract void onStop();

  public void start() {
    isActive = true;
    onStart();
    putVariablesEvent();
  }

  // TODO should unsubscribe the topics from exchanges?
  public void stop() {
    isActive = false;
    cancelAll();
    onStop();
    putVariablesEvent();
    writeLog("停止算法");
  }

  protected void putVariablesEvent() {
    // TODO
  }

  protected void putParametersEvent() {
    // TODO
  }

  public TickData getTick(String qwSymbol) {
    return eventProcessor.getTick(this, qwSymbol);
  }

  public AccountData getAccount(String qwSymbol) {
    return eventProcessor.getAccount(this, qwSymbol);
  }

  public ContractData getContract(String qwSymbol) {
    return eventProcessor.getContract(this, qwSymbol);
  }

  public void cancelOrder(String qwOrderId) {
    writeLog(String.format("取消订单: %s", qwOrderId));
    eventProcessor.cancelOrder(this, qwOrderId);
  }

  public void cancelAll() {
    if (activeOrders.isEmpty()) {
      return;
    }

    for (String qwSymbol : activeOrders.keySet()) {
      cancelOrder(qwSymbol);
    }
  }

  public void writeLog(String log) {
    eventProcessor.writeLog(this, log);
  }

  protected String buy(String qwSymbol, float price, float volume, OrderTypeEnum orderType, OffsetEnum offset) {
    String log = String.format("委托买入%s：%f@%f", qwSymbol, volume, price);
    writeLog(log);
    return eventProcessor.sendOrder(this, qwSymbol, DirectionEnum.LONG, MathUtil.getFloorByLastNumber(price * 0.98f, 0), volume, orderType, offset);
  }

  protected String sell(String qwSymbol, float price, float volume, OrderTypeEnum orderType, OffsetEnum offset) {
    String log = String.format("委托卖出%s：%f@%f", qwSymbol, volume, price);
    writeLog(log);
    return eventProcessor.sendOrder(this, qwSymbol, DirectionEnum.SHORT, MathUtil.getCeilByLastNumber(price * 1.02f, 0), volume, orderType, offset);
  }

  public void subscribe(String qwSymbol) {
    eventProcessor.subscribe(this, qwSymbol);
  }

  public void unSubscribe(String qwSymbol) {
    eventProcessor.unSubscribe(this, qwSymbol);
  }

  public void setStrategyParam(StrategyParam strategyParam) {
    this.strategyParam = strategyParam;
  }
}
