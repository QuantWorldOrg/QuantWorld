package com.quantworld.app.trader.oms.strategies.teststrategy;

import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.trader.oms.strategies.StrategyParam;

/**
 * @author: Shawn
 * @Date: 12/7/2019
 * @Description:
 */
public class TestParam extends StrategyParam {

  private String testField;

  public String getTestField() {
    return testField;
  }

  public void setTestField(String testField) {
    this.testField = testField;
  }

  @Override
  public StrategyTemplateEnum getStrategyTemplateName() {
    return StrategyTemplateEnum.TESTSTRATEGY;
  }
}
