package com.quantworld.app.data.constants;

import com.quantworld.app.broker.gateway.bigone.BigOneGateway;
import com.quantworld.app.broker.gateway.huobi.HuobiGateway;

public enum ExchangeEnum {
  NONE("NONE"),
  // Chinese
  CFFEX(""), // China Financial Futures Exchange
  SHFE(""), //Shanghai Futures Exchange
  CZCE(""), //Zhengzhou Commodity Exchange
  DCE(""), //Dalian Commodity Exchange
  INE(""), //Shanghai International Energy Exchange
  SSE(""), //Shanghai Stock Exchange
  SZSE(""), //Shenzhen Stock Exchange
  SGE(""), //Shanghai Gold Exchange
  WXE(""), //Wuxi Steel Exchange

  // Global
  SMART(""), //Smart Router for US stocks
  NYMEX(""), //New York Mercantile Exchange
  COMEX(""), //a division of theNew York Mercantile Exchange
  GLOBEX(""), //Globex of CME
  IDEALPRO(""), //Forex ECN of Interactive Brokers
  CME(""), //Chicago Mercantile Exchange
  ICE(""), //Intercontinental Exchange
  SEHK(""), //Stock Exchange of Hong Kong
  HKFE(""), //Hong Kong Futures Exchange
  SGX(""), //Singapore Global Exchange
  CBOT(""), //Chicago Board of Trade
  CBOE(""), //Chicago Board Options Exchange
  CFE(""), //CBOE Futures Exchange
  DME(""), //Dubai Mercantile Exchange
  EUREX(""), //Eurex Exchange
  APEX(""), //Asia Pacific Exchange
  LME(""), //London Metal Exchange
  BMD(""), //Bursa Malaysia Derivatives
  TOCOM(""), //Tokyo Commodity Exchange
  EUNX(""), //Euronext Exchange
  KRX(""), //Korean Exchange

  // CryptoCurrency
  BITME(""),
  OKEX(""),
  HUOBI(HuobiGateway.class.getName()),
  BITFINEX(""),
  BINANCE(""),
  COINBASE(""),
  BIGONE(BigOneGateway.class.getName()),

  // Special Function
  LOCAL("")//For local generated data
  ;

  private String gatewayName;
  ExchangeEnum(String gatewayName) {
    this.gatewayName = gatewayName;
  }

  public String getGatewayName() {
    return gatewayName;
  }
}
