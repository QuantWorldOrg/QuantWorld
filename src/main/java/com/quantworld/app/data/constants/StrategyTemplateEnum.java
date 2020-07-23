package com.quantworld.app.data.constants;

import com.quantworld.app.trader.oms.strategies.teststrategy.TestParam;
import com.quantworld.app.trader.oms.strategies.teststrategy.TestStrategy;

/**
 * Here are the strategy template params, the enum element name should align with param property file name.
 * There are four params should be set.
 * 1. Strategy class path name
 * 2. Strategy runtime param setting.
 * 3. Strategy generator param, generator need to generate basic strategy param on generator page.
 * 4. Strategy template name, this name will display on page.
 *
 * @author: Shawn
 * @Date: 12/7/2019
 * @Description:
 */
public enum StrategyTemplateEnum {

  TESTSTRATEGY(
      TestStrategy.class.getName(),
      TestParam.class.getName(),
      "",
      "测试策略"
  );

  String strategy;
  String param;
  String displayLabel;
  String basicGeneratorParam;

  StrategyTemplateEnum(String strategy, String param, String basicGeneratorParam, String displayLabel) {
    this.strategy = strategy;
    this.param = param;
    this.basicGeneratorParam = basicGeneratorParam;
    this.displayLabel = displayLabel;
  }

  public String getStrategy() {
    return strategy;
  }

  public String getParam() {
    return param;
  }

  public String getDisplayLabel() {
    return displayLabel;
  }

  public String getBasicGeneratorParam() {
    return basicGeneratorParam;
  }
}
