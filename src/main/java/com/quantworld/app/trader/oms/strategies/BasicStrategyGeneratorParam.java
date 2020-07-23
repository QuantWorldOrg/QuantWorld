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
