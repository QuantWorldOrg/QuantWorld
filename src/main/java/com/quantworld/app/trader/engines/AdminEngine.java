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
package com.quantworld.app.trader.engines;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.QWContext;
import com.quantworld.app.service.BaseApp;
import com.quantworld.app.broker.gateway.BaseGateway;
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
import com.quantworld.app.trader.oms.OrderManagementSystem;
import com.quantworld.app.trader.oms.RiskManagement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 11/6/2019
 * @Description:
 */
@Component
public class AdminEngine {

  public static final String NAME = "adminEngine";

  private Logger logger = LoggerFactory.getLogger(getClass());

  private EventDispatcher eventDispatcher;

  private LogEngine logEngine;

  private EmailEngine emailEngine;

  private OrderManagementSystem orderManagementSystem;

  private Map<String, BaseGateway> gatewayMap = new ConcurrentHashMap<>();

  private Map<String, BaseEngine> engineMap = new ConcurrentHashMap<>();

  private Map<String, BaseApp> appMap = new ConcurrentHashMap<>();

  private List<ExchangeEnum> exchanges = new ArrayList<>();

  @Autowired
  public AdminEngine(EventDispatcher eventDispatcher, LogEngine logEngine, EmailEngine emailEngine, OrderManagementSystem orderManagementSystem) {
    this.eventDispatcher = eventDispatcher;
    this.logEngine = logEngine;
    this.emailEngine = emailEngine;
    this.orderManagementSystem = orderManagementSystem;
    init();
  }

  public void startEngine() {
    initEngine();
  }

  public AdminEngine(){}

  private void init() {
    eventDispatcher.start();
  }

  public Map<String, BaseEngine> addEngine(BaseEngine engine) {
    engineMap.put(engine.getEngineName(), engine);
    return engineMap;
  }

  public Map<String, BaseGateway> addGateway(BaseGateway gateway) {
    gatewayMap.put(gateway.getGatewayName(), gateway);
    return gatewayMap;
  }

  public Map<String, BaseEngine> addApp(BaseApp app) {
    appMap.put(app.getAppName(), app);
    Map<String, BaseEngine> engine = addEngine(app.getBaseEngine());
    return engine;
  }

  public void initEngine() {
    addEngine(logEngine);
    addEngine(orderManagementSystem);
    addEngine(emailEngine);
  }

  public void writeLog(String message, String source) {
    LogData logData = new LogData(message, source);
    eventDispatcher.putEvent(new Event(EventTypeEnum.EVENT_LOG, logData));
  }

  public BaseGateway getGateway(String gatewayName) {
    BaseGateway gateway = gatewayMap.get(gatewayName);
    if (gateway == null) {
      writeLog(String.format("找不到底层接口：%s", gatewayName), StringUtils.EMPTY);
    }
    return gateway;
  }

  public BaseEngine getEngine(String engineName) {
    BaseEngine engine = engineMap.get(engineName);
    if (engine == null) {
      writeLog(String.format("找不到引擎：%s", engineName), StringUtils.EMPTY);
    }
    return engine;
  }

  public JSONObject getDefaultSetting(String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      return gateway.getDefaultSetting();
    }
    return new JSONObject();
  }

  public Set<String> getAllGatewayNames() {
    return gatewayMap.keySet();
  }

  public Set<String> getAllApps() {
    return appMap.keySet();
  }


  public List<ExchangeEnum> getAllExchanges() {
    return exchanges;
  }

  public void connect(Map<String, String> setting, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      gateway.connect(setting);
    }
  }

  public void subscribe(SubscribeRequest request, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      gateway.subscribe(request);
    }
  }

  public void unSubscribe(SubscribeRequest request, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      gateway.unSubscribe(request);
    }
  }

  public String sendOrder(OrderRequest request, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      return gateway.sendOrder(request);
    } else {
      return StringUtils.EMPTY;
    }
  }

  public String sendOrderCheckRisk(OrderRequest request, String gatewayName) {
    // 避免循环依赖
    RiskManagement riskManagerEngine = (RiskManagement) QWContext.getBean(RiskManagement.NAME);
    if (!riskManagerEngine.checkRisk(request, gatewayName)) {
      return StringUtils.EMPTY;
    }
    return sendOrder(request, gatewayName);
  }

  public void cancelOrder(CancelRequest request, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      gateway.cancelOrder(request);
    }
  }

  /**
   * 需要保证订单的顺序，所以这里用Queue和Linkedlist
   *
   * @param requests
   * @param gatewayName
   * @return
   */
  public List<String> sendOrders(Queue<OrderRequest> requests, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      return gateway.sendOrders(requests);
    } else {
      return new LinkedList<>();
    }
  }


  public void cancelOrders(Queue<CancelRequest> requests, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      gateway.cancelOrders(requests);
    }
  }

  public List<String> queryHistory(HistoryRequest request, String gatewayName) {
    BaseGateway gateway = getGateway(gatewayName);
    if (gateway != null) {
      return gateway.queryHistory(request);
    } else {
      return new LinkedList<>();
    }
  }

  public void close() {
    eventDispatcher.stop();

    for (Map.Entry<String, BaseEngine> engineEntry : engineMap.entrySet()) {
      engineEntry.getValue().close();
    }

    for (Map.Entry<String, BaseGateway> gatewayEntry : gatewayMap.entrySet()) {
      gatewayEntry.getValue().close();
    }
  }
  
  public TickData getTick(String qwSymbol) {
    return orderManagementSystem.getTick(qwSymbol);
  }

  public OrderData getOrderData(String qwSymbol) {
    return orderManagementSystem.getOrderData(qwSymbol);
  }

  public TradeData getTradeData(String qwTradeId) {
    return orderManagementSystem.getTradeData(qwTradeId);
  }

  public PositionData getPositionData(String qwPositionId) {
    return orderManagementSystem.getPositionData(qwPositionId);
  }

  public AccountData getAccountData(String qwSymbol) {
    return orderManagementSystem.getAccountData(qwSymbol);
  }

  public ContractData getContractData(String qwSymbol) {
    return orderManagementSystem.getContractData(qwSymbol);
  }

  public List<TickData> getAllTickData() {
    return orderManagementSystem.getAllTickData();
  }

  public List<OrderData> getAllOrderData() {
    return orderManagementSystem.getAllOrderData();
  }

  public List<TradeData> getAllTradeData() {
    return orderManagementSystem.getAllTradeData();
  }

  public List<PositionData> getAllPositionData() {
    return orderManagementSystem.getAllPositionData();
  }

  public List<AccountData> getAllAccountsData() {
    return orderManagementSystem.getAllAccountsData();
  }

  public List<ContractData> getAllContractsData() {
    return orderManagementSystem.getAllContractsData();
  }

  public List<OrderData> getAllActiveOrders(String qWSymbol) {
    return orderManagementSystem.getAllActiveOrders(qWSymbol);
  }

  public List<OrderData> getAllActiveOrders() {
    return orderManagementSystem.getAllActiveOrders();
  }
}
