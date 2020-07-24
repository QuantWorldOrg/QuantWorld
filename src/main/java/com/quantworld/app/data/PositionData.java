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

import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class PositionData extends BaseData implements Serializable {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String symbol = StringUtils.EMPTY;
  private ExchangeEnum exchange = ExchangeEnum.NONE;
  private DirectionEnum direction = DirectionEnum.NONE;

  private float volume;
  private float frozen;
  private float price;
  private float pnl;
  private float qwVolume;

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

  public DirectionEnum getDirection() {
    return direction;
  }

  public void setDirection(DirectionEnum direction) {
    this.direction = direction;
  }

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

  public float getFrozen() {
    return frozen;
  }

  public void setFrozen(float frozen) {
    this.frozen = frozen;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getPnl() {
    return pnl;
  }

  public void setPnl(float pnl) {
    this.pnl = pnl;
  }

  public float getQwVolume() {
    return qwVolume;
  }

  public void setQwVolume(float qwVolume) {
    this.qwVolume = qwVolume;
  }

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public String getQwPositionId() {
    return getQwSymbol() + "." + direction.name();
  }

  public PositionData deepClone() {
    return (PositionData) super.deepClone();
  }

  @Override
  public String toString() {
    return "PositionData{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", direction=" + direction +
        ", volume=" + volume +
        ", frozen=" + frozen +
        ", price=" + price +
        ", pnl=" + pnl +
        ", qwVolume=" + qwVolume +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
