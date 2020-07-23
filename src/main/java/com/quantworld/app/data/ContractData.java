package com.quantworld.app.data;

import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OptionTypeEnum;
import com.quantworld.app.data.constants.ProductEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class ContractData extends BaseData {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String symbol = StringUtils.EMPTY;
  private ExchangeEnum exchange = ExchangeEnum.NONE;
  private String name = StringUtils.EMPTY;
  private ProductEnum product = ProductEnum.NONE;
  private int size;
  private float pricePrecision;
  private float amountPrecision;

  private float minOrderAmount = 1.0f;           // minimum trading volume of the contract
  private float maxOrderAmount = Float.MAX_VALUE;
  private float minOrderValue = 1.0f;
  private boolean stopSupported = false;    //whether server supports stop order
  private boolean netPosition = false;      // whether gateway uses net position volume
  private boolean historyData = false;      // whether gateway provides bar history data

  private float optionStrike = 0;
  private String optionUnderlying = StringUtils.EMPTY;          // qw_symbol of underlying contract
  private OptionTypeEnum optionType;
  private Date optionExpiry = new Date();

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

  public float getPricePrecision() {
    return pricePrecision;
  }

  public void setPricePrecision(float pricePrecision) {
    this.pricePrecision = pricePrecision;
  }

  public float getMinOrderAmount() {
    return minOrderAmount;
  }

  public void setMinOrderAmount(float minOrderAmount) {
    this.minOrderAmount = minOrderAmount;
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

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public float getMaxOrderAmount() {
    return maxOrderAmount;
  }

  public void setMaxOrderAmount(float maxOrderAmount) {
    this.maxOrderAmount = maxOrderAmount;
  }

  public float getMinOrderValue() {
    return minOrderValue;
  }

  public void setMinOrderValue(float minOrderValue) {
    this.minOrderValue = minOrderValue;
  }

  public float getAmountPrecision() {
    return amountPrecision;
  }

  public void setAmountPrecision(float amountPrecision) {
    this.amountPrecision = amountPrecision;
  }

  public ContractData deepClone() {
    return (ContractData) super.deepClone();
  }

  @Override
  public String toString() {
    return "ContractData{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", name='" + name + '\'' +
        ", product=" + product +
        ", size=" + size +
        ", pricePrecision=" + pricePrecision +
        ", amountPrecision=" + amountPrecision +
        ", minOrderAmount=" + minOrderAmount +
        ", maxOrderAmount=" + maxOrderAmount +
        ", minOrderValue=" + minOrderValue +
        ", stopSupported=" + stopSupported +
        ", netPosition=" + netPosition +
        ", historyData=" + historyData +
        ", optionStrike=" + optionStrike +
        ", optionUnderlying='" + optionUnderlying + '\'' +
        ", optionType=" + optionType +
        ", optionExpiry=" + optionExpiry +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
