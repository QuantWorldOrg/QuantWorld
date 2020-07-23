package com.quantworld.app.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationBean {
  @Value("${uri.protocol:wss://}")
  private String protocol;

  @Value("${uri.host:api.huobi.pro}")
  private String host;

  @Value("${uri.port:443}")
  private String port;

  @Value("${uri.ao.path:/ws/v1}")
  private String aO;

  @Value("${uri.market.path:/ws}")
  private String market;

  @Value("${accessKey}")
  private String accessKey;

  @Value("${secretKey}")
  private String secretKey;

  public String getProtocol() {
    return protocol;
  }

  public String getHost() {
    return host;
  }

  public String getPort() {
    return port;
  }

  public String getaO() {
    return aO;
  }

  public String getMarket() {
    return market;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }
}
