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
package com.quantworld.app.data.constants;

import com.quantworld.app.trader.oms.strategies.teststrategy.TestParam;
import com.quantworld.app.trader.oms.strategies.teststrategy.TestStrategy;
import com.quantworld.app.trader.oms.strategies.triangle.DemoStrategy;
import com.quantworld.app.trader.oms.strategies.triangle.DemoBasicGeneratorParam;
import com.quantworld.app.trader.oms.strategies.triangle.DemoStrategyParam;

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
  DEMOSTRATEGY(
      DemoStrategy.class.getName(),
      DemoStrategyParam.class.getName(),
      DemoBasicGeneratorParam.class.getName(),
      "演示策略"
  ),

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
