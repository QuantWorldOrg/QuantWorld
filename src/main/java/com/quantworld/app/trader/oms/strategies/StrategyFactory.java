package com.quantworld.app.trader.oms.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Shawn
 * @Date: 12/7/2019
 * @Description:
 */
public class StrategyFactory {
  private static final Logger logger = LoggerFactory.getLogger(StrategyFactory.class);

  public static BaseStrategy getStrategy(String strategyName, StrategyParam strategyParam) {
    Object obj = null;
    try {
      obj = Class.forName(strategyParam.getStrategyTemplateName().getStrategy()).getDeclaredConstructor(String.class, StrategyParam.class)
          .newInstance(strategyName, strategyParam);
    } catch (Exception e) {
      logger.error("Can not get strategy {}", strategyParam.getStrategyTemplateName(), e);
    }
    return (BaseStrategy) obj;
  }
}
