package com.quantworld.app.data;

import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OffsetEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class TradeData extends BaseData {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String symbol = StringUtils.EMPTY;
  private ExchangeEnum exchange = ExchangeEnum.NONE;
  private String orderId = StringUtils.EMPTY;
  private String tradeId = StringUtils.EMPTY;
  private DirectionEnum direction = DirectionEnum.NONE;

  private OffsetEnum offset;
  private float price;
  private float volume;
  private float fee;
  private Long time;

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

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getTradeId() {
    return tradeId;
  }

  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  public DirectionEnum getDirection() {
    return direction;
  }

  public void setDirection(DirectionEnum direction) {
    this.direction = direction;
  }

  public OffsetEnum getOffset() {
    return offset;
  }

  public void setOffset(OffsetEnum offset) {
    this.offset = offset;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public String getQwOrderId() {
    return gatewayName + "." + orderId;
  }

  public String getQwTradeId() {
    return gatewayName + "." + tradeId;
  }

  public float getFee() {
    return fee;
  }

  public void setFee(float fee) {
    this.fee = fee;
  }

  public TradeData deepClone() {
    return (TradeData) super.deepClone();
  }

  @Override
  public String toString() {
    return "TradeData{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", orderId='" + orderId + '\'' +
        ", tradeId='" + tradeId + '\'' +
        ", direction=" + direction +
        ", offset=" + offset +
        ", price=" + price +
        ", volume=" + volume +
        ", fee=" + fee +
        ", time=" + time +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
