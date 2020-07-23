package com.quantworld.app.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;

@Entity
@Table(name = "historicalTradeRecords")
public class HistoricalTradeRecords extends Entitys {
  private static final long serialVersionUID = 1L;

  private String id;

  @Column(nullable = false, length = 50)
  private String symbol;

  @Column(nullable = false, length = 50)
  private String exchange;

  @Column(nullable = false, length = 50)
  private String orderId;

  @Column(nullable = false, length = 50)
  private String tradeId;

  @Column(nullable = false, length = 10)
  private String direction;

  @Column(nullable = false, length = 50)
  private String offset;

  @Column(nullable = false, length = 50)
  private float price;

  @Column(nullable = false, length = 50)
  private float volume;

  @Column(nullable = false, length = 50)
  private String strategyName;

  @Column(nullable = false, length = 50)
  private float profit;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp createdTime;

  @Id
  @GenericGenerator(name = "idGenerator", strategy = "uuid")
  @GeneratedValue(generator = "idGenerator")
  public String getId() {
    return id;
  }

  public void setId(String uuid) {
    this.id = uuid;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
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

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
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

  public Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Timestamp time) {
    this.createdTime = time;
  }

  public String getStrategyName() {
    return strategyName;
  }

  public void setStrategyName(String strategyName) {
    this.strategyName = strategyName;
  }

  public float getProfit() {
    return profit;
  }

  public void setProfit(float profit) {
    this.profit = profit;
  }
}
