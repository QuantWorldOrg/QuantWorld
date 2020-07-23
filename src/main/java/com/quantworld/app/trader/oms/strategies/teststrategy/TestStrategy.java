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
