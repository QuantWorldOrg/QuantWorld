package com.quantworld.app.trader.oms;


import com.quantworld.app.data.LogData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.data.constants.StatusEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.cep.OnEvent;
import com.quantworld.app.trader.engines.AdminEngine;
import com.quantworld.app.trader.engines.BaseEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
// TODO, different strategy should has different risk management?
public class RiskManagement extends BaseEngine {

  public static final String NAME = "riskManagement";

  private EventDispatcher eventDispatcher;

  private AdminEngine adminEngine;

  private OrderManagementSystem orderManagementSystem;

  private boolean isActive;

  private int orderFlowCount;
  // 委托流每秒上限
  private int orderFlowLimit;

  private int orderFlowClear;
  private int orderFlowTimer;

  private int orderSizeLimit;

  private int tradeCount;
  private int tradeLimit;

  private int orderCancelLimit;
  private Map<String, Integer> orderCancelCounts;

  private int activeOrderLimit;

  @Autowired
  public RiskManagement(EventDispatcher eventDispatcher, AdminEngine adminEngine, OrderManagementSystem orderManagementSystem) {
    this.eventDispatcher = eventDispatcher;
    this.adminEngine = adminEngine;
    this.orderManagementSystem = orderManagementSystem;
  }

  private void init() {
    this.isActive = false;
    this.orderFlowCount = 0;
    this.orderFlowLimit = 50;
    this.orderFlowClear = 1;
    this.orderFlowTimer = 0;
    this.orderSizeLimit = 100;
    this.tradeCount = 0;
    this.tradeLimit = 1000;
    this.orderCancelLimit = 500;
    this.orderCancelCounts = new ConcurrentHashMap<>();
    this.activeOrderLimit = 50;
    loadSetting();
    subscribeEvent();
  }

  public String sendOrder(OrderRequest request, String gatewayName) {
    return adminEngine.sendOrderCheckRisk(request, gatewayName);
  }


  public void updateSetting(Map<String, Object> setting) {
    isActive = (boolean) setting.get("active");
    orderFlowLimit = (int) setting.get("orderFlowLimit");
    orderFlowClear = (int) setting.get("orderFlowClear");
    orderSizeLimit = (int) setting.get("orderSizeLimit");
    tradeLimit = (int) setting.get("tradeLimit");
    activeOrderLimit = (int) setting.get("activeOrderLimit");
    orderCancelLimit = (int) setting.get("orderCancelLimit");
    if (isActive) {
      writeLog("风控系统启动");
    } else {
      writeLog("风控系统停止");
    }
  }

  public Map<String, Object> getSetting() {
    Map<String, Object> setting = new ConcurrentHashMap<>();
    setting.put("active", isActive);
    setting.put("orderFlowLimit", orderFlowLimit);
    setting.put("orderFlowClear", orderFlowClear);
    setting.put("orderSizeLimit", orderSizeLimit);
    setting.put("tradeLimit", tradeLimit);
    setting.put("activeOrderLimit", activeOrderLimit);
    setting.put("orderCancelLimit", orderCancelLimit);
    return setting;
  }

  public void loadSetting() {
    // TODO 从数据库，或者缓存中读取配置
  }

  public void saveSetting() {
    // TODO 异步存入数据库中
  }

  public void subscribeEvent() {
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TRADE, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TIMER, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_ORDER, this);
  }

  public void writeLog(String message) {
    LogData logData = new LogData(message, NAME);
    eventDispatcher.putEvent(new Event(EventTypeEnum.EVENT_LOG, logData));
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ORDER)
  public void processOrderEvent(Event event) {
    if (isValidEvent(event)) {
      OrderData orderData = (OrderData) event.getData();
      if (orderData.getStatus() != StatusEnum.CANCELED) {
        return;
      }

      if (orderCancelCounts.get(orderData.getSymbol()) == null) {
        orderCancelCounts.put(orderData.getSymbol(), 0);
      } else {
        orderCancelCounts.put(orderData.getSymbol(), orderCancelCounts.get(orderData.getSymbol()) + 1);
      }
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TRADE)
  public void processTradeEvent(Event event) {
    if (isValidEvent(event)) {
      TradeData tradeData = (TradeData) event.getData();
      tradeCount += tradeData.getVolume();
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TIMER)
  public void processTimerEvent(Event event) {
    if (isValidEvent(event)) {
      orderFlowLimit += 1;
      if (orderFlowTimer >= orderFlowClear) {
        orderFlowCount = 0;
        orderFlowTimer = 0;
      }
    }
  }

  public boolean checkRisk(OrderRequest request, String gatewayName) {
    if (!isActive) {
      return true;
    }

    if (request.getVolume() <= 0) {
      writeLog("委托数量必须大于0");
      return false;
    }

    if (request.getVolume() > orderSizeLimit) {
      String log = String.format("单笔委托数量%f，超过限制%d", request.getVolume(), orderSizeLimit);
      writeLog(log);
      return false;
    }

    if (tradeCount > tradeLimit) {
      String log = String.format("今日总成交合约数量%d，超过限制%d", tradeCount, tradeLimit);
      writeLog(log);
      return false;
    }

    if (orderFlowCount >= orderFlowLimit) {
      String log = String.format("委托流数量%d，超过限制每%d秒%d次", orderFlowCount, orderFlowClear, orderFlowLimit);
      writeLog(log);
      return false;
    }

    int activeOrderCount = orderManagementSystem.getAllActiveOrders().size();
    if (activeOrderCount >= activeOrderLimit) {
      String log = String.format("当前活动委托次数%d，超过限制%d", activeOrderCount, activeOrderLimit);
      writeLog(log);
      return false;
    }

    if (orderCancelCounts.containsKey(request.getSymbol()) && orderCancelCounts.get(request.getSymbol()) >= orderCancelLimit) {
      String log = String.format("当日%s撤单次数%d，超过限制%d", request.getSymbol(), orderCancelCounts.get(request.getSymbol()), orderCancelLimit);
      writeLog(log);
      return false;
    }

    orderFlowCount++;
    return true;
  }
}



