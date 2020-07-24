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
package com.quantworld.app.trader.oms.strategies;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.StrategyTemplateEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public abstract class BasicStrategyGeneratorParam {
  protected StrategyTemplateEnum strategyTemplateEnum;

  protected ExchangeEnum exchangeEnum;

  public StrategyTemplateEnum getStrategyTemplateEnum() {
    return strategyTemplateEnum;
  }

  public void setStrategyTemplateEnum(StrategyTemplateEnum strategyTemplateEnum) {
    this.strategyTemplateEnum = strategyTemplateEnum;
  }

  public ExchangeEnum getExchangeEnum() {
    return exchangeEnum;
  }

  public void setExchangeEnum(ExchangeEnum exchangeEnum) {
    this.exchangeEnum = exchangeEnum;
  }

  public abstract JSONObject getBasicStrategyGeneratorParam();

  protected JSONObject getExchanges(Properties properties) {
    List<String> nameList = new ArrayList<>();
    ExchangeEnum[] enums = ExchangeEnum.values();
    for (ExchangeEnum exchangeEnum : enums) {
      if (ExchangeEnum.NONE.equals(exchangeEnum)) {
        continue;
      }
      nameList.add(exchangeEnum.name());
    }
    Collections.sort(nameList);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("key", properties.get("exchangeEnum"));
    jsonObject.put("value", nameList);
    return jsonObject;
  }

}
