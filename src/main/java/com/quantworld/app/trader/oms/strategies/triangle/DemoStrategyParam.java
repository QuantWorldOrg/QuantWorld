package com.quantworld.app.trader.oms.strategies.triangle;

import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.trader.oms.strategies.StrategyParam;
import org.apache.commons.lang3.StringUtils;


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
