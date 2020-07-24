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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class AccountData extends BaseData {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String accountId = StringUtils.EMPTY;
  private String asset = StringUtils.EMPTY;
  private float balance;
  private float available;
  private float frozen;
  private ExchangeEnum exchange = ExchangeEnum.NONE;

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public float getBalance() {
    return balance;
  }

  public void setBalance(float balance) {
    this.balance = balance;
  }

  public float getFrozen() {
    return frozen;
  }

  public void setFrozen(float frozen) {
    this.frozen = frozen;
  }

  public float getAvailable() {
    return available;
  }

  public String getQwAccountId() {
    return gatewayName + "." + accountId;
  }

  public String getAsset() {
    return asset;
  }

  public void setAsset(String asset) {
    this.asset = asset;
  }

  public ExchangeEnum getExchange() {
    return exchange;
  }

  public void setExchange(ExchangeEnum exchange) {
    this.exchange = exchange;
  }

  public String getQwSymbol() {
    return (asset + "." + exchange.name()).toLowerCase();
  }

  public void setAvailable(float available) {
    this.available = available;
  }

  public AccountData deepClone() {
    return (AccountData) super.deepClone();
  }

  @Override
  public String toString() {
    return "AccountData{" +
        "accountId='" + accountId + '\'' +
        ", asset='" + asset + '\'' +
        ", balance=" + balance +
        ", available=" + available +
        ", frozen=" + frozen +
        ", exchange=" + exchange +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
