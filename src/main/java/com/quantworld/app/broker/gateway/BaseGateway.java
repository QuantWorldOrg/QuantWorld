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
package com.quantworld.app.broker.gateway;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.QWContext;
import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.HistoryRequest;
import com.quantworld.app.data.LogData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.TickData;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 11/7/2019
 * @Description:
 */
public abstract class BaseGateway {
  private List<ExchangeEnum> exchanges = new ArrayList<>();

  private String gatewayName;

  private JSONObject defaultSetting = new JSONObject();

  private EventDispatcher eventDispatcher;

  protected LocalOrderManager orderManager;

  private Map<String, String> symbolNameMap = new ConcurrentHashMap<>();

  public BaseGateway(String gatewayName) {
    this.gatewayName = gatewayName;
    this.eventDispatcher = (EventDispatcher) QWContext.getBean(EventDispatcher.NAME);
  }

  public void onEvent(EventTypeEnum eventType, Object data) {
    Event event = new Event(eventType, data);
    eventDispatcher.putEvent(event);
  }

  public void onTick(TickData tickData) {
    onEvent(EventTypeEnum.EVENT_TICK, tickData);
  }

  public void onTrade(TradeData tradeData) {
    onEvent(EventTypeEnum.EVENT_TRADE, tradeData);
  }

  public void onOrder(OrderData orderData) {
    onEvent(EventTypeEnum.EVENT_ORDER, orderData);
  }

  public void onPosition(PositionData positionData) {
    onEvent(EventTypeEnum.EVENT_POSITION, positionData);
  }

  public void onAccount(AccountData accountData) {
    onEvent(EventTypeEnum.EVENT_ACCOUNT, accountData);
  }

  public void onLog(LogData logData) {
    onEvent(EventTypeEnum.EVENT_LOG, logData);
  }

  public void onContract(ContractData contractData) {
    onEvent(EventTypeEnum.EVENT_CONTRACT, contractData);
  }

  public void writeLog(String msg) {
    onLog(new LogData(msg, gatewayName));
  }

  public List<ExchangeEnum> getExchanges() {
    return exchanges;
  }

  public void setExchanges(List<ExchangeEnum> exchanges) {
    this.exchanges = exchanges;
  }

  public String getGatewayName() {
    return gatewayName;
  }

  public void setGatewayName(String gatewayName) {
    this.gatewayName = gatewayName;
  }

  public JSONObject getDefaultSetting() {
    return defaultSetting;
  }

  public void setDefaultSetting(JSONObject defaultSetting) {
    this.defaultSetting = defaultSetting;
  }

  public abstract void connect(Map<String, String> setting);

  public abstract void subscribe(SubscribeRequest request);

  public abstract void unSubscribe(SubscribeRequest request);

  public abstract String sendOrder(OrderRequest request);

  public abstract void cancelOrder(CancelRequest cancelRequest);

  public List<String> sendOrders(Queue<OrderRequest> requests) {
    List<String> qwOrderIds = new LinkedList<>();
    for (OrderRequest request : requests) {
      String qwOrderId = this.sendOrder(request);
      qwOrderIds.add(qwOrderId);
    }
    return qwOrderIds;
  }

  public void cancelOrders(Queue<CancelRequest> requests) {
    for (CancelRequest request : requests) {
      this.cancelOrder(request);
    }
  }

  public abstract void queryAccount();

  public abstract void queryPosition();

  public abstract void queryContract();

  public abstract List<String> queryHistory(HistoryRequest request);

  public abstract void close();

  public LocalOrderManager getOrderManager() {
    return orderManager;
  }

  public void setOrderManager(LocalOrderManager orderManager) {
    this.orderManager = orderManager;
  }

  public Map<String, String> getSymbolNameMap() {
    return symbolNameMap;
  }

  public void updateSymbolNameMap(String key, String value) {
    symbolNameMap.put(key, value);
  }
}
