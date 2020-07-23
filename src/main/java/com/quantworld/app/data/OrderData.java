package com.quantworld.app.data;

import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OffsetEnum;
import com.quantworld.app.data.constants.OrderTypeEnum;
import com.quantworld.app.data.constants.StatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class OrderData extends BaseData {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String symbol = StringUtils.EMPTY;
  private ExchangeEnum exchange = ExchangeEnum.NONE;
  private String orderId = StringUtils.EMPTY;

  private OrderTypeEnum type = OrderTypeEnum.NONE;
  private DirectionEnum direction = DirectionEnum.NONE;
  private OffsetEnum offset = OffsetEnum.NONE;
  private StatusEnum status = StatusEnum.NONE;

  private float price;
  private float volume;
  private float traded;
  private float remainAmount;
  private Long time = 0L;
  private final Set<StatusEnum> activeStatus = new HashSet<StatusEnum>(){
    {
      add(StatusEnum.SUBMITTING);
      add(StatusEnum.NOTTRADED);
      add(StatusEnum.PARTTRADED);
      add(StatusEnum.PARTCANCELED);
      add(StatusEnum.CANCELLING);
    }
  };

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

  public OrderTypeEnum getType() {
    return type;
  }

  public void setType(OrderTypeEnum type) {
    this.type = type;
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

  public float getTraded() {
    return traded;
  }

  public void setTraded(float traded) {
    this.traded = traded;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
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
    return (exchange.name() + "." + orderId).toLowerCase();
  }

  public boolean isActive() {
    return activeStatus.contains(status);
  }

  public float getRemainAmount() {
    return remainAmount;
  }

  public void setRemainAmount(float remainAmount) {
    this.remainAmount = remainAmount;
  }

  public CancelRequest createCancelRequest() {
    return new CancelRequest(orderId, symbol, exchange);
  }

  public OrderData deepClone() {
    return (OrderData) super.deepClone();
  }

  @Override
  public String toString() {
    return "OrderData{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", orderId='" + orderId + '\'' +
        ", type=" + type +
        ", direction=" + direction +
        ", offset=" + offset +
        ", status=" + status +
        ", price=" + price +
        ", volume=" + volume +
        ", traded=" + traded +
        ", remainAmount=" + remainAmount +
        ", time=" + time +
        ", activeStatus=" + activeStatus +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
