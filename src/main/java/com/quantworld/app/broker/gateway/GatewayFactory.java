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
