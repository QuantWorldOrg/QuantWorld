package com.quantworld.app.broker.gateway;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Shawn
 * @Date: 3/11/2020
 * @Description:
 */
public class GatewayFactory {
  private static final Logger logger = LoggerFactory.getLogger(GatewayFactory.class);

  public static BaseGateway getGateway(String gatewayClass) {
    Object obj = null;
    try {
      if (StringUtils.isNotBlank(gatewayClass)) {
        obj = Class.forName(gatewayClass).getDeclaredConstructor().newInstance();
      }
    } catch (Exception e) {
      logger.error("Can not get gateway {}", gatewayClass, e);
    }
    return (BaseGateway) obj;
  }
}
