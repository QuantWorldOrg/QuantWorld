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
