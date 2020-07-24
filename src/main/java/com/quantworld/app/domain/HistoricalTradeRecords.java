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
