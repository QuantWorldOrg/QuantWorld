package com.quantworld.app.data;

import com.quantworld.app.data.constants.ExchangeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class CancelRequest extends BaseData {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String orderId;
  private String symbol;
  private ExchangeEnum exchange;

  public CancelRequest(String orderId, String symbol, ExchangeEnum exchange) {
    this.orderId = orderId;
    this.symbol = symbol;
    this.exchange = exchange;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
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

  public CancelRequest deepClone() {
    return (CancelRequest) super.deepClone();
  }

  @Override
  public String toString() {
    return "CancelRequest{" +
        "orderId='" + orderId + '\'' +
        ", symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
