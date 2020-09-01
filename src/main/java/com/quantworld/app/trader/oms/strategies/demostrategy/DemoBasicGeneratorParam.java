package com.quantworld.app.trader.oms.strategies.demostrategy;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.data.constants.digitalcurrency.BaseCurrencyEnum;
import com.quantworld.app.data.constants.digitalcurrency.MidCurrencyEnum;
import com.quantworld.app.data.constants.digitalcurrency.QuoteCurrencyEnum;
import com.quantworld.app.trader.oms.strategies.BasicStrategyGeneratorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DemoBasicGeneratorParam extends BasicStrategyGeneratorParam {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public JSONObject getBasicStrategyGeneratorParam() {
    JSONObject jsonObject = new JSONObject();
    String strategyTemplateName = StrategyTemplateEnum.DEMOSTRATEGY.toString().toLowerCase();
    InputStream inputStream = getClass().getResourceAsStream("/i18n/" + strategyTemplateName.toLowerCase() + ".properties");
    Properties properties = new Properties();
    if (inputStream != null) {
      BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
      try {
        properties.load(bf);
        inputStream.close();
      } catch (Exception e) {
        logger.error("读取" + strategyTemplateName + ".properties 失败！", e);
      }
    }
    jsonObject.put("baseCur", getBaseCurArray(properties));
    jsonObject.put("midCur", getMidCurArray(properties));
    jsonObject.put("quoteCur", getQuoteCurArray(properties));
    jsonObject.put("exchangeEnum", getExchanges(properties));

    return jsonObject;
  }

  private JSONObject getMidCurArray(Properties properties) {
    List<String> nameList = new ArrayList<>();
    for (MidCurrencyEnum currencyEnum : MidCurrencyEnum.values()) {
      nameList.add(currencyEnum.name());
    }
    Collections.sort(nameList);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("key", properties.get("midCur"));
    jsonObject.put("value", nameList);
    return jsonObject;
  }

  private JSONObject getBaseCurArray(Properties properties) {
    List<String> nameList = new ArrayList<>();
    for (BaseCurrencyEnum currencyEnum : BaseCurrencyEnum.values()) {
      nameList.add(currencyEnum.name());
    }
    Collections.sort(nameList);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("key", properties.get("baseCur"));
    jsonObject.put("value", nameList);
    return jsonObject;
  }

  private JSONObject getQuoteCurArray(Properties properties) {
    List<String> nameList = new ArrayList<>();
    for (QuoteCurrencyEnum currencyEnum : QuoteCurrencyEnum.values()) {
      nameList.add(currencyEnum.name());
    }
    Collections.sort(nameList);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("key", properties.get("quoteCur"));
    jsonObject.put("value", nameList);
    return jsonObject;
  }


}
