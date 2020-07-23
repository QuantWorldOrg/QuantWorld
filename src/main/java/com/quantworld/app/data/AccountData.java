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
