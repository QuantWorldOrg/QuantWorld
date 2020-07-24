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
package com.quantworld.app.data;

import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.IntervalEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Candlestick bar data of a certain trading period.
 */
public class BarData extends BaseData {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String symbol = StringUtils.EMPTY;
  private ExchangeEnum exchange = ExchangeEnum.NONE;
  private Date datetime = new Date();
  private IntervalEnum interval;
  private float volume;
  private float openInterest;
  private float openPrice;
  private float highPrice;
  private float lowPrice;
  private float closePrice;
  private String qwSymbol = StringUtils.EMPTY;

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

  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }

  public IntervalEnum getInterval() {
    return interval;
  }

  public void setInterval(IntervalEnum interval) {
    this.interval = interval;
  }

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

  public float getOpenInterest() {
    return openInterest;
  }

  public void setOpenInterest(float openInterest) {
    this.openInterest = openInterest;
  }

  public float getOpenPrice() {
    return openPrice;
  }

  public void setOpenPrice(float openPrice) {
    this.openPrice = openPrice;
  }

  public float getHighPrice() {
    return highPrice;
  }

  public void setHighPrice(float highPrice) {
    this.highPrice = highPrice;
  }

  public float getLowPrice() {
    return lowPrice;
  }

  public void setLowPrice(float lowPrice) {
    this.lowPrice = lowPrice;
  }

  public float getClosePrice() {
    return closePrice;
  }

  public void setClosePrice(float closePrice) {
    this.closePrice = closePrice;
  }

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public BarData deepClone() {
    return (BarData) super.deepClone();
  }

  @Override
  public String toString() {
    return "BarData{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", datetime=" + datetime +
        ", interval=" + interval +
        ", volume=" + volume +
        ", openInterest=" + openInterest +
        ", openPrice=" + openPrice +
        ", highPrice=" + highPrice +
        ", lowPrice=" + lowPrice +
        ", closePrice=" + closePrice +
        ", qwSymbol='" + qwSymbol + '\'' +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
