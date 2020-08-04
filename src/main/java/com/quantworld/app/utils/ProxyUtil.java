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
package com.quantworld.app.utils;

import com.quantworld.app.QWContext;
import com.quantworld.app.domain.ProxyConfiguration;
import com.quantworld.app.repository.ProxyRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public class ProxyUtil {
  private static final Logger logger = LoggerFactory.getLogger(ProxyUtil.class);

  public static Proxy getProxy() {
    ProxyRepository repository = (ProxyRepository) QWContext.getBean(ProxyRepository.NAME);
    List<ProxyConfiguration> proxyConfigurations = repository.findAll();
    if (proxyConfigurations.isEmpty()) {
      return null;
    } else {
      ProxyConfiguration proxyConfiguration = proxyConfigurations.get(0);
      logger.info("Proxy Setting:{}", proxyConfiguration.toString());
      return new Proxy(Proxy.Type.valueOf(proxyConfiguration.getProtocol()),
          new InetSocketAddress(proxyConfiguration.getServer(), proxyConfiguration.getPort()));
    }
  }
}
