package com.quantworld.app.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyUtil {
  private static final Logger logger = LoggerFactory.getLogger(ProxyUtil.class);

  @NotNull
  public static Proxy getProxy() {
    String osName = System.getProperties().getProperty("os.name");
    String hostname;
    if (osName.contains("Windows")) {
      hostname = "127.0.0.1";
    } else {
      hostname = "192.168.1.117";
    }
    logger.info("hostname:{}, OS System:{}", hostname, osName);
    return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostname, 10808));
  }
}
