package com.quantworld.app.data;

import com.quantworld.app.data.constants.ExchangeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class SubscribeRequest extends BaseData {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String symbol;
  private ExchangeEnum exchange;

  public SubscribeRequest(String symbol, ExchangeEnum exchange) {
    this.symbol = symbol;
    this.exchange = exchange;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public ExchangeEnum getExchange() {
    return exchange;
  }

  public void setExchange(ExchangeEnum exchange) {
    this.exchange = exchange;
  }

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public SubscribeRequest deepClone() {
    return (SubscribeRequest) super.deepClone();
  }

  @Override
  public String toString() {
    return "SubscribeRequest{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
