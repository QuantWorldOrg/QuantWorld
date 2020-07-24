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
package com.quantworld.app.trader.oms;

import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.TickData;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.cep.OnEvent;
import com.quantworld.app.trader.engines.BaseEngine;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 11/6/2019
 * @Description:
 */
@Component
public class OrderManagementSystem extends BaseEngine {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static final String NAME = "orderManagementSystem";

  private EventDispatcher eventDispatcher;

  private Map<String, TickData> tickDataMap;

  private Map<String, OrderData> orderDataMap;

  private Map<String, TradeData> tradeDataMap;

  private Map<String, PositionData> positionDataMap;

  private Map<String, AccountData> accountDataMap;

  private Map<String, ContractData> contractDataMap;

  private Map<String, OrderData> activeOrderDataMap;

  @Autowired
  public OrderManagementSystem(EventDispatcher eventDispatcher) {
    this.eventDispatcher = eventDispatcher;
    init();
  }

  private void init() {
    tickDataMap = new ConcurrentHashMap<>();
    orderDataMap = new ConcurrentHashMap<>();
    tradeDataMap = new ConcurrentHashMap<>();
    positionDataMap = new ConcurrentHashMap<>();
    accountDataMap = new ConcurrentHashMap<>();
    contractDataMap = new ConcurrentHashMap<>();
    activeOrderDataMap = new ConcurrentHashMap<>();
    subscribeEvent();
  }

  private void subscribeEvent() {
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TICK, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_ORDER, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TRADE, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_POSITION, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_ACCOUNT, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_CONTRACT, this);
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TICK)
  private void processTickEvent(Event event) {
    if (isValidEvent(event)) {
      TickData tickData = (TickData) event.getData();
      tickDataMap.put(tickData.getQwSymbol(), tickData);
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ORDER)
  private void processOrderEvent(Event event) {
    if (isValidEvent(event)) {
      OrderData orderData = (OrderData) event.getData();
      logger.info("OMS收到订单号:{}", orderData.getQwOrderId());
      orderDataMap.put(orderData.getQwOrderId(), orderData);

      if (orderData.isActive()) {
        activeOrderDataMap.put(orderData.getQwOrderId(), orderData);
      } else {
        activeOrderDataMap.remove(orderData.getQwOrderId());
      }
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TRADE)
  private void processTradeEvent(Event event) {
    if (isValidEvent(event)) {
      TradeData tradeData = (TradeData) event.getData();
      tradeDataMap.put(tradeData.getQwTradeId(), tradeData);
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_POSITION)
  private void processPositionEvent(Event event) {
    if (isValidEvent(event)) {
      PositionData positionData = (PositionData) event.getData();
      positionDataMap.put(positionData.getQwPositionId(), positionData);
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ACCOUNT)
  private void processAccountEvent(Event event) {
    if (isValidEvent(event)) {
      AccountData accountData = (AccountData) event.getData();
      accountDataMap.put(accountData.getQwSymbol(), accountData);
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_CONTRACT)
  private void processContractEvent(Event event) {
    if (isValidEvent(event)) {
      ContractData contractData = (ContractData) event.getData();
      if (contractData != null) {
        contractDataMap.put(contractData.getQwSymbol().toLowerCase(), contractData);
      }
    }
  }

  public TickData getTick(String qwSymbol) {
    return tickDataMap.get(qwSymbol);
  }

  public OrderData getOrderData(String qwOrderId) {
    return orderDataMap.get(qwOrderId);
  }

  public TradeData getTradeData(String qwTradeId) {
    return tradeDataMap.get(qwTradeId);
  }

  public PositionData getPositionData(String qwPositionId) {
    return positionDataMap.get(qwPositionId);
  }

  public AccountData getAccountData(String qwSymbol) {
    return accountDataMap.get(qwSymbol);
  }

  public ContractData getContractData(String qwSymbol) {
    return contractDataMap.get(qwSymbol);
  }

  public List<TickData> getAllTickData() {
    if (tickDataMap.isEmpty()) {
      return new LinkedList<>();
    } else {
      return new LinkedList<>(tickDataMap.values());
    }
  }

  public List<OrderData> getAllOrderData() {
    if (orderDataMap.isEmpty()) {
      return new LinkedList<>();
    } else {
      return new LinkedList<>(orderDataMap.values());
    }
  }

  public List<TradeData> getAllTradeData() {
    if (tradeDataMap.isEmpty()) {
      return new LinkedList<>();
    } else {
      return new LinkedList<>(tradeDataMap.values());
    }
  }

  public List<PositionData> getAllPositionData() {
    if (positionDataMap.isEmpty()) {
      return new LinkedList<>();
    } else {
      return new LinkedList<>(positionDataMap.values());
    }
  }

  public List<AccountData> getAllAccountsData() {
    if (accountDataMap.isEmpty()) {
      return new LinkedList<>();
    } else {
      return new LinkedList<>(accountDataMap.values());
    }
  }

  public List<ContractData> getAllContractsData() {
    if (contractDataMap == null || contractDataMap.isEmpty()) {
      return new LinkedList<>();
    } else {
      return new LinkedList<>(contractDataMap.values());
    }
  }

  public List<OrderData> getAllActiveOrders(String qWSymbol) {
    if (Strings.isBlank(qWSymbol)) {
      return new LinkedList<>(activeOrderDataMap.values());
    } else {
      List<OrderData> activeOrders = new LinkedList<>();
      for (Map.Entry<String, OrderData> entry : activeOrderDataMap.entrySet()) {
        if (qWSymbol.equals(entry.getKey())) {
          activeOrders.add(entry.getValue());
        }
      }
      return activeOrders;
    }
  }

  public List<OrderData> getAllActiveOrders() {
    return getAllActiveOrders("");
  }
}
