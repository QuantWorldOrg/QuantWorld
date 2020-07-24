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

import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OptionTypeEnum;
import com.quantworld.app.data.constants.ProductEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "contract")
public class Contract extends Entitys {
  private static final long serialVersionUID = 1L;

  private String id;
  @Column(nullable = false, length = 50)
  private String symbol;

  @Column(nullable = false, length = 50)
  private ExchangeEnum exchange;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 50)
  private ProductEnum product;

  @Column(nullable = false, length = 40)
  private int size;

  @Column(nullable = false, length = 50)
  private float priceTick;

  @Column(nullable = false, length = 50)
  private float minVolume = 1.0f;           // minimum trading volume of the contract

  @Column(nullable = false, length = 50)
  private boolean stopSupported = false;    //whether server supports stop order

  @Column(nullable = false, length = 50)
  private boolean netPosition = false;      // whether gateway uses net position volume

  @Column(nullable = false, length = 50)
  private boolean historyData = false;      // whether gateway provides bar history data

  @Column(nullable = false, length = 50)
  private float optionStrike = 0;

  @Column(nullable = false, length = 50)
  private String optionUnderlying;          // qw_symbol of underlying contract

  @Column(nullable = false, length = 50)
  private OptionTypeEnum optionType;

  @Column(nullable = false, length = 50)
  private Date optionExpiry;

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

  public ExchangeEnum getExchange() {
    return exchange;
  }

  public void setExchange(ExchangeEnum exchange) {
    this.exchange = exchange;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductEnum getProduct() {
    return product;
  }

  public void setProduct(ProductEnum product) {
    this.product = product;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public float getPriceTick() {
    return priceTick;
  }

  public void setPriceTick(float priceTick) {
    this.priceTick = priceTick;
  }

  public float getMinVolume() {
    return minVolume;
  }

  public void setMinVolume(float minVolume) {
    this.minVolume = minVolume;
  }

  public boolean isStopSupported() {
    return stopSupported;
  }

  public void setStopSupported(boolean stopSupported) {
    this.stopSupported = stopSupported;
  }

  public boolean isNetPosition() {
    return netPosition;
  }

  public void setNetPosition(boolean netPosition) {
    this.netPosition = netPosition;
  }

  public boolean isHistoryData() {
    return historyData;
  }

  public void setHistoryData(boolean historyData) {
    this.historyData = historyData;
  }

  public float getOptionStrike() {
    return optionStrike;
  }

  public void setOptionStrike(float optionStrike) {
    this.optionStrike = optionStrike;
  }

  public String getOptionUnderlying() {
    return optionUnderlying;
  }

  public void setOptionUnderlying(String optionUnderlying) {
    this.optionUnderlying = optionUnderlying;
  }

  public OptionTypeEnum getOptionType() {
    return optionType;
  }

  public void setOptionType(OptionTypeEnum optionType) {
    this.optionType = optionType;
  }

  public Date getOptionExpiry() {
    return optionExpiry;
  }

  public void setOptionExpiry(Date optionExpiry) {
    this.optionExpiry = optionExpiry;
  }
}
