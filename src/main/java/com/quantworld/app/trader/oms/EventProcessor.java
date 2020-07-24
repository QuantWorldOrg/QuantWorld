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

import com.quantworld.app.broker.exception.ServiceApplicationException;
import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.TickData;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.data.constants.OffsetEnum;
import com.quantworld.app.data.constants.OrderTypeEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.cep.OnEvent;
import com.quantworld.app.trader.domain.StrategyEntity;
import com.quantworld.app.trader.engines.AdminEngine;
import com.quantworld.app.trader.engines.BaseEngine;
import com.quantworld.app.trader.oms.strategies.BaseStrategy;
import com.quantworld.app.trader.oms.strategies.StrategyFactory;
import com.quantworld.app.trader.oms.strategies.StrategyParam;
import com.quantworld.app.trader.repository.StrategyEntityDAO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 11/6/2019
 * @Description:
 */
@Service
public class EventProcessor extends BaseEngine {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static final String NAME = "eventProcessor";

  private AdminEngine adminEngine;

  private EventDispatcher eventDispatcher;

  private Map<String, BaseStrategy> orderIdStrategyMap = new ConcurrentHashMap<>();
  private Map<String, ConcurrentHashMap<String, BaseStrategy>> qwSymbolStrategyMap = new ConcurrentHashMap<>();
  private Map<String, BaseStrategy> strategyNameAndStrategyMap = new ConcurrentHashMap<>();
  // different contracts have different strategies.
  private Map<String, StrategyParam> strategyNameAndStrategyParamBeanMap = new ConcurrentHashMap<>();

  private StrategyEntityDAO strategyEntityDAO;

  @Autowired
  public EventProcessor(EventDispatcher eventDispatcher, AdminEngine adminEngine, StrategyEntityDAO strategyEntityDAO) {
    this.eventDispatcher = eventDispatcher;
    this.adminEngine = adminEngine;
    this.strategyEntityDAO = strategyEntityDAO;
    registerEvent();
    init();
  }

  private void init() {
    writeLog("事件处理器启动");
    loadStrategySetting();
  }

  public void loadStrategySetting() {
    Map<? extends String, ? extends StrategyParam> settingMap = getStrategySetting();
    // pushing event to controller layer, loading all the existed strategy configuration
    Event event = new Event(EventTypeEnum.EVENT_STRATEGY_SETTING_LOADING, settingMap);
    eventDispatcher.putEvent(event);
    strategyNameAndStrategyParamBeanMap.putAll(settingMap);
    writeLog("算法配置载入成功");
  }

  /**
   * Save strategy related setting from controller layer
   *
   * @param strategyEntity
   */
  public void saveStrategySetting(StrategyEntity strategyEntity) {
    strategyNameAndStrategyParamBeanMap.put(strategyEntity.getStrategyName(), strategyEntity.getStrategyParam());
    Map<String, StrategyParam> settingMap = new ConcurrentHashMap<>();
    settingMap.put(strategyEntity.getStrategyName(), strategyEntity.getStrategyParam());
    Event event = new Event(EventTypeEnum.EVENT_STRATEGY_SETTING_SAVED, settingMap);
    eventDispatcher.putEvent(event);
    strategyEntityDAO.save(strategyEntity);
  }

  /**
   * Delete strategy related setting from controller layer
   *
   * @param strategyName
   * @param strategyParam
   */
  public void deleteStrategySetting(String strategyName, StrategyParam strategyParam, String userId) {
    strategyNameAndStrategyParamBeanMap.remove(strategyName);
    Map<String, StrategyParam> settingMap = new ConcurrentHashMap<>();
    settingMap.put(strategyName, strategyParam);
    Event event = new Event(EventTypeEnum.EVENT_STRATEGY_SETTING_DELETED, settingMap);
    eventDispatcher.putEvent(event);
    deleteSetting(strategyName, userId);
  }

  /**
   * Update strategy related setting from controller layer
   *
   * @param strategyEntity
   */
  public void updateStrategySetting(StrategyEntity strategyEntity) {
    strategyNameAndStrategyParamBeanMap.put(strategyEntity.getStrategyName(), strategyEntity.getStrategyParam());
    Map<String, StrategyParam> settingMap = new ConcurrentHashMap<>();
    BaseStrategy strategy = strategyNameAndStrategyMap.get(strategyEntity.getStrategyName());
    if (strategy != null) {
      strategy.updateParam(strategyEntity.getStrategyParam());
    } else {
      strategy = StrategyFactory.getStrategy(strategyEntity.getStrategyName(), strategyEntity.getStrategyParam());
      strategyNameAndStrategyMap.put(strategy.getName(), strategy);
    }
    settingMap.put(strategyEntity.getStrategyName(), strategyEntity.getStrategyParam());
    Event event = new Event(EventTypeEnum.EVENT_STRATEGY_SETTING_UPDATED, settingMap);
    eventDispatcher.putEvent(event);
    strategyEntityDAO.update(strategyEntity);
  }

  private void saveSetting(String strategyName, StrategyParam strategyParam) {
    strategyEntityDAO.save(new StrategyEntity(strategyName, strategyParam));
  }

  private void deleteSetting(String strategyName, String userId) {
    strategyEntityDAO.deleteByStrategyName(strategyName, userId);
  }

  private void updateSetting(String strategyName, StrategyParam strategyParam, String userId) {
    StrategyEntity strategyEntity = new StrategyEntity(strategyName, strategyParam);
    strategyEntity.setUserId(userId);
    strategyEntityDAO.update(strategyEntity);
  }

  private Map<? extends String, ? extends StrategyParam> getStrategySetting() {
    List<StrategyEntity> strategyEntityList = strategyEntityDAO.findAll();
    Map<String, StrategyParam> strategyParamBeanMap = new ConcurrentHashMap<>();
    for (StrategyEntity strategyEntity : strategyEntityList) {
      strategyParamBeanMap.put(strategyEntity.getStrategyName(), strategyEntity.getStrategyParam());
    }
    return strategyParamBeanMap;
  }

  public void registerEvent() {
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TICK, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TIMER, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TRADE, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_ORDER, this);
  }


  @OnEvent(eventType = EventTypeEnum.EVENT_TICK)
  public void processTickEvent(Event event) {
    if (isValidEvent(event)) {
      TickData tickData = (TickData) event.getData();
      ConcurrentHashMap<String, BaseStrategy> strategies = qwSymbolStrategyMap.get(tickData.getQwSymbol());
      if (strategies != null && !strategies.isEmpty()) {
//        strategies.values().parallelStream().forEach(s->s.updateTick(tickData));
        for (BaseStrategy strategy : strategies.values()) {
          strategy.updateTick(tickData);
        }
      }
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TIMER)
  public void processTimerEvent(Event event) {
    if (isValidEvent(event)) {
      if (!strategyNameAndStrategyMap.isEmpty()) {
        //strategyNameAndStrategyMap.values().parallelStream().forEach(BaseStrategy::updateTimer);
        for (BaseStrategy strategy : strategyNameAndStrategyMap.values()) {
          strategy.updateTimer();
        }
      }
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TRADE)
  public void processTradeEvent(Event event) {
    if (isValidEvent(event)) {
      TradeData tradeData = (TradeData) event.getData();
      BaseStrategy strategy = orderIdStrategyMap.get(tradeData.getQwOrderId());
      if (strategy != null) {
        strategy.updateTrade(tradeData);
      }
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ORDER)
  public void processOrderEvent(Event event) {
    if (isValidEvent(event)) {
      OrderData orderData = (OrderData) event.getData();
      BaseStrategy strategy = orderIdStrategyMap.get(orderData.getQwOrderId());
      if (strategy != null) {
        strategy.updateOrder(orderData);
      }
    }
  }

  // start strategy from controller layer, new configuration from UI
  public String startStrategy(String strategyName, StrategyParam strategyParam) throws ServiceApplicationException {
    BaseStrategy strategy = strategyNameAndStrategyMap.get(strategyName);
    if (strategy == null || strategyNameAndStrategyMap.isEmpty()) {
      strategy = StrategyFactory.getStrategy(strategyName, strategyParam);
      strategyNameAndStrategyMap.put(strategy.getName(), strategy);
    } else {
      strategy.setStrategyParam(strategyParam);
    }
    strategy.start();
    return strategy.getName();
  }

  // stop strategy from controller layer
  public void stopStrategy(String strategyName) {
    BaseStrategy strategy = strategyNameAndStrategyMap.get(strategyName);
    if (strategy != null) {
      strategy.stop();
      strategyNameAndStrategyMap.remove(strategy.getName());
    }
  }

  public void stopAll() {
    for (String strategyName : strategyNameAndStrategyMap.keySet()) {
      stopStrategy(strategyName);
    }
  }

  public void subscribe(BaseStrategy strategy, String qwSymbol) {
    // 合约订阅的时候，一些其他信息也应该被订阅，比如仓位，即订阅该合约的所有信息
    ContractData contractData = adminEngine.getContractData(qwSymbol);
    if (contractData == null) {
      String msg = String.format("订阅行情失败，找不到合约：%s", qwSymbol);
      writeLog(strategy, msg);
      throw new ServiceApplicationException(msg);
    }

    ConcurrentHashMap<String, BaseStrategy> strategies = qwSymbolStrategyMap.get(qwSymbol);
    if (strategies == null) {
      strategies = new ConcurrentHashMap<>();
    }
    if (strategies.isEmpty()) {
      SubscribeRequest request = new SubscribeRequest(contractData.getSymbol(), contractData.getExchange());
      adminEngine.subscribe(request, contractData.getGatewayName());
      strategies.put(strategy.getName(), strategy);
      qwSymbolStrategyMap.put(qwSymbol, strategies);
    } else {
      SubscribeRequest request = new SubscribeRequest(contractData.getSymbol(), contractData.getExchange());
      adminEngine.subscribe(request, contractData.getGatewayName());
      // TODO test this part code
      qwSymbolStrategyMap.get(qwSymbol).put(strategy.getName(), strategy);
    }
  }

  /**
   * unsubscribe topic
   *
   * @param strategy
   * @param qwSymbol
   */
  public void unSubscribe(BaseStrategy strategy, String qwSymbol) {
    ContractData contractData = adminEngine.getContractData(qwSymbol);
    if (contractData == null) {
      writeLog(strategy, String.format("取消订阅行情失败，找不到合约：%s", qwSymbol));
      return;
    }

    ConcurrentHashMap<String, BaseStrategy> strategies = qwSymbolStrategyMap.get(qwSymbol);
    if (strategies == null || strategies.isEmpty()) {
      SubscribeRequest request = new SubscribeRequest(contractData.getSymbol(), contractData.getExchange());
      adminEngine.unSubscribe(request, contractData.getGatewayName());
      return;
    }

    qwSymbolStrategyMap.get(qwSymbol).remove(strategy.getName());
  }

  public String sendOrder(BaseStrategy strategy, String qwSymbol, DirectionEnum direction, float price, float volume,
                          OrderTypeEnum orderType, OffsetEnum offset) {
    ContractData contract = adminEngine.getContractData(qwSymbol);
    if (contract == null) {
      writeLog(strategy, "委托下单失败，找不到合约：" + qwSymbol);
      return StringUtils.EMPTY;
    }
    OrderRequest request = new OrderRequest();
    request.setSymbol(contract.getSymbol());
    request.setExchange(contract.getExchange());
    request.setDirection(direction);
    request.setType(orderType);
    request.setVolume(volume);
    request.setPrice(price);
    request.setOffset(offset);

    String qwOrderId = adminEngine.sendOrderCheckRisk(request, contract.getGatewayName());
    orderIdStrategyMap.put(qwOrderId, strategy);
    logger.info("发送订单:{}", qwOrderId);
    return qwOrderId;
  }

  public void cancelOrder(BaseStrategy strategy, String qwOrderId) {
    OrderData orderData = adminEngine.getOrderData(qwOrderId);
    if (orderData == null) {
      writeLog(strategy, String.format("委托撤单失败，找不到委托：%s", qwOrderId));
      return;
    }

    CancelRequest request = orderData.createCancelRequest();
    adminEngine.cancelOrder(request, orderData.getGatewayName());
  }

  public TickData getTick(BaseStrategy strategy, String qwSymbol) {
    TickData tickData = adminEngine.getTick(qwSymbol);
    if (tickData == null) {
//      writeLog(strategy, String.format("查询行情失败，找不到行情: %s", qwSymbol));
    }
    return tickData;
  }

  public AccountData getAccount(BaseStrategy strategy, String qwSymbol) {
    AccountData accountData = adminEngine.getAccountData(qwSymbol);
    if (accountData == null) {
      writeLog(strategy, String.format("查询账户失败: %s", qwSymbol));
    }
    return accountData;
  }

  public ContractData getContract(BaseStrategy strategy, String qwSymbol) {
    ContractData contractData = adminEngine.getContractData(qwSymbol);
    if (contractData == null) {
      writeLog(strategy, String.format("查询合约失败，找不到合约: %s", qwSymbol));
    }
    return contractData;
  }

  public List<ContractData> getAllContracts(BaseStrategy strategy) {
    List<ContractData> contractDataList = adminEngine.getAllContractsData();
    if (contractDataList == null || contractDataList.isEmpty()) {
      writeLog(strategy, String.format("没有合约，请更新。"));
    }
    return contractDataList;
  }

  public void writeLog(BaseStrategy strategy, String log) {
    if (strategy != null) {
      String message = String.format("%s:%s", strategy.getName(), log);
      logger.info(message);
      Event event = new Event(EventTypeEnum.EVENT_STRATEGY_LOG, message);
      eventDispatcher.putEvent(event);
    }
  }

  public void writeLog(String log) {
    writeLog(null, log);
  }
}
