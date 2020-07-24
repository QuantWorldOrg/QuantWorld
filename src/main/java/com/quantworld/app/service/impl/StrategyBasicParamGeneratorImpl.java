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
