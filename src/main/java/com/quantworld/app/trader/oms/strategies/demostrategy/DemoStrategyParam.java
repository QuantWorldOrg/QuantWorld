package com.quantworld.app.trader.oms.strategies.demostrategy;

import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.trader.oms.strategies.StrategyParam;


public class DemoStrategyParam extends StrategyParam {

  private int param = 0;
  @Override
  public StrategyTemplateEnum getStrategyTemplateName() {
    return StrategyTemplateEnum.DEMOSTRATEGY;
  }

  public int getParam() {
    return param;
  }

  public void setParam(int param) {
    this.param = param;
  }
}
