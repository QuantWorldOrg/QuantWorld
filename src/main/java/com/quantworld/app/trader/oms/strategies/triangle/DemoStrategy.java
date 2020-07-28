package com.quantworld.app.trader.oms.strategies.triangle;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.QWContext;
import com.quantworld.app.data.*;
import com.quantworld.app.data.constants.*;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.oms.strategies.BaseStrategy;
import com.quantworld.app.trader.oms.strategies.StrategyParam;

public class DemoStrategy extends BaseStrategy {

  private final DemoStrategyVariable demoStrategyVariable = new DemoStrategyVariable();
  private DemoStrategyParam demoStrategyParam;

  public DemoStrategy(String strategyName, StrategyParam strategyParam) {
    super(strategyName, strategyParam);
    if (super.strategyParam != null) {
      demoStrategyParam = (DemoStrategyParam) super.strategyParam;
    } else {
      demoStrategyParam = new DemoStrategyParam();
    }
    demoStrategyParam = (DemoStrategyParam) strategyParam;
  }

  @Override
  protected void onStart() {
  }

  @Override
  public void executeStrategy() {
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
    demoStrategyParam = (DemoStrategyParam) strategyParam;
  }

  @Override
  protected void onPosition(PositionData positionData) {
    // Do nothing
  }

  @Override
  protected void onAccount(AccountData accountData) {
    // Do nothing
  }

  @Override
  protected void onTimer() {
  }

  @Override
  protected void onStop() {
  }

  @Override
  public String getName() {
    return strategyName;
  }

  protected void sendEmail(String subject, String content) {
    JSONObject data = new JSONObject();
    data.put("subject", subject);
    data.put("content", content);
    Event event = new Event(EventTypeEnum.EVENT_EMAIL, data);
    EventDispatcher eventDispatcher = (EventDispatcher) QWContext.getBean(EventDispatcher.NAME);
    eventDispatcher.putEvent(event);
  }
}
