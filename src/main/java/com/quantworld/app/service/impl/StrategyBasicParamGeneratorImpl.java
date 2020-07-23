package com.quantworld.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.service.StrategyBasicParamGenerator;
import com.quantworld.app.trader.oms.strategies.BasicStrategyGeneratorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("strategyBasicParamGenerator")
public class StrategyBasicParamGeneratorImpl implements StrategyBasicParamGenerator {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public JSONObject getBasicStrategyParam(String strategyTemplateName) {

    StrategyTemplateEnum strategyTemplateEnum = StrategyTemplateEnum.valueOf(strategyTemplateName.toUpperCase());
    String basicGeneratorParamClassName = strategyTemplateEnum.getBasicGeneratorParam();
    Object obj = null;
    try {
      obj = Class.forName(basicGeneratorParamClassName).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      logger.error("Can not get strategy {}", strategyTemplateName, e);
    }
    if (obj != null) {
      BasicStrategyGeneratorParam generatorParam = (BasicStrategyGeneratorParam) obj;
      return generatorParam.getBasicStrategyGeneratorParam();
    }

    return new JSONObject();
  }
}
