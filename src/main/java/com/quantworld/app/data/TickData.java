package com.quantworld.app.data;

import com.quantworld.app.data.constants.ExchangeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickData extends BaseData {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Tick data contains information about:
   * last trade in market
   * orderbook snapshot
   * intraday market statistics.
   */

  private String symbol;
  private ExchangeEnum exchange;
  private long datetime;

  private String name = "";
  private float volume = 0;
  private float openInterest = 0;
  private float lastPrice = 0;
  private float lastVolume = 0;
  private float limitUp = 0;
  private float limitDown = 0;

  private float openPrice = 0;
  private float highPrice = 0;
  private float lowPrice = 0;
  private float preClose = 0;

  private float bidPrice1 = 0;
  private float bidPrice2 = 0;
  private float bidPrice3 = 0;
  private float bidPrice4 = 0;
  private float bidPrice5 = 0;

  private float askPrice1 = 0;
  private float askPrice2 = 0;
  private float askPrice3 = 0;
  private float askPrice4 = 0;
  private float askPrice5 = 0;

  private float bidVolume1 = 0;
  private float bidVolume2 = 0;
  private float bidVolume3 = 0;
  private float bidVolume4 = 0;
  private float bidVolume5 = 0;

  private float askVolume1 = 0;
  private float askVolume2 = 0;
  private float askVolume3 = 0;
  private float askVolume4 = 0;
  private float askVolume5 = 0;

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

  public long getDatetime() {
    return datetime;
  }

  public void setDatetime(long datetime) {
    this.datetime = datetime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public float getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(float lastPrice) {
    this.lastPrice = lastPrice;
  }

  public float getLastVolume() {
    return lastVolume;
  }

  public void setLastVolume(float lastVolume) {
    this.lastVolume = lastVolume;
  }

  public float getLimitUp() {
    return limitUp;
  }

  public void setLimitUp(float limitUp) {
    this.limitUp = limitUp;
  }

  public float getLimitDown() {
    return limitDown;
  }

  public void setLimitDown(float limitDown) {
    this.limitDown = limitDown;
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

  public float getPreClose() {
    return preClose;
  }

  public void setPreClose(float preClose) {
    this.preClose = preClose;
  }

  public float getBidPrice1() {
    return bidPrice1;
  }

  public void setBidPrice1(float bidPrice1) {
    this.bidPrice1 = bidPrice1;
  }

  public float getBidPrice2() {
    return bidPrice2;
  }

  public void setBidPrice2(float bidPrice2) {
    this.bidPrice2 = bidPrice2;
  }

  public float getBidPrice3() {
    return bidPrice3;
  }

  public void setBidPrice3(float bidPrice3) {
    this.bidPrice3 = bidPrice3;
  }

  public float getBidPrice4() {
    return bidPrice4;
  }

  public void setBidPrice4(float bidPrice4) {
    this.bidPrice4 = bidPrice4;
  }

  public float getBidPrice5() {
    return bidPrice5;
  }

  public void setBidPrice5(float bidPrice5) {
    this.bidPrice5 = bidPrice5;
  }

  public float getAskPrice1() {
    return askPrice1;
  }

  public void setAskPrice1(float askPrice1) {
    this.askPrice1 = askPrice1;
  }

  public float getAskPrice2() {
    return askPrice2;
  }

  public void setAskPrice2(float askPrice2) {
    this.askPrice2 = askPrice2;
  }

  public float getAskPrice3() {
    return askPrice3;
  }

  public void setAskPrice3(float askPrice3) {
    this.askPrice3 = askPrice3;
  }

  public float getAskPrice4() {
    return askPrice4;
  }

  public void setAskPrice4(float askPrice4) {
    this.askPrice4 = askPrice4;
  }

  public float getAskPrice5() {
    return askPrice5;
  }

  public void setAskPrice5(float askPrice5) {
    this.askPrice5 = askPrice5;
  }

  public float getBidVolume1() {
    return bidVolume1;
  }

  public void setBidVolume1(float bidVolume1) {
    this.bidVolume1 = bidVolume1;
  }

  public float getBidVolume2() {
    return bidVolume2;
  }

  public void setBidVolume2(float bidVolume2) {
    this.bidVolume2 = bidVolume2;
  }

  public float getBidVolume3() {
    return bidVolume3;
  }

  public void setBidVolume3(float bidVolume3) {
    this.bidVolume3 = bidVolume3;
  }

  public float getBidVolume4() {
    return bidVolume4;
  }

  public void setBidVolume4(float bidVolume4) {
    this.bidVolume4 = bidVolume4;
  }

  public float getBidVolume5() {
    return bidVolume5;
  }

  public void setBidVolume5(float bidVolume5) {
    this.bidVolume5 = bidVolume5;
  }

  public float getAskVolume1() {
    return askVolume1;
  }

  public void setAskVolume1(float askVolume1) {
    this.askVolume1 = askVolume1;
  }

  public float getAskVolume2() {
    return askVolume2;
  }

  public void setAskVolume2(float askVolume2) {
    this.askVolume2 = askVolume2;
  }

  public float getAskVolume3() {
    return askVolume3;
  }

  public void setAskVolume3(float askVolume3) {
    this.askVolume3 = askVolume3;
  }

  public float getAskVolume4() {
    return askVolume4;
  }

  public void setAskVolume4(float askVolume4) {
    this.askVolume4 = askVolume4;
  }

  public float getAskVolume5() {
    return askVolume5;
  }

  public void setAskVolume5(float askVolume5) {
    this.askVolume5 = askVolume5;
  }

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public TickData deepClone() {
    return (TickData) super.deepClone();
  }

  @Override
  public String toString() {
    return "TickData{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", datetime=" + datetime +
        ", name='" + name + '\'' +
        ", volume=" + volume +
        ", openInterest=" + openInterest +
        ", lastPrice=" + lastPrice +
        ", lastVolume=" + lastVolume +
        ", limitUp=" + limitUp +
        ", limitDown=" + limitDown +
        ", openPrice=" + openPrice +
        ", highPrice=" + highPrice +
        ", lowPrice=" + lowPrice +
        ", preClose=" + preClose +
        ", bidPrice1=" + bidPrice1 +
        ", bidPrice2=" + bidPrice2 +
        ", bidPrice3=" + bidPrice3 +
        ", bidPrice4=" + bidPrice4 +
        ", bidPrice5=" + bidPrice5 +
        ", askPrice1=" + askPrice1 +
        ", askPrice2=" + askPrice2 +
        ", askPrice3=" + askPrice3 +
        ", askPrice4=" + askPrice4 +
        ", askPrice5=" + askPrice5 +
        ", bidVolume1=" + bidVolume1 +
        ", bidVolume2=" + bidVolume2 +
        ", bidVolume3=" + bidVolume3 +
        ", bidVolume4=" + bidVolume4 +
        ", bidVolume5=" + bidVolume5 +
        ", askVolume1=" + askVolume1 +
        ", askVolume2=" + askVolume2 +
        ", askVolume3=" + askVolume3 +
        ", askVolume4=" + askVolume4 +
        ", askVolume5=" + askVolume5 +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
