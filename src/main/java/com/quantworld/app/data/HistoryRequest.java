package com.quantworld.app.data;

import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.IntervalEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public class HistoryRequest extends BaseData {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String symbol = StringUtils.EMPTY;
  private ExchangeEnum exchange = ExchangeEnum.NONE;
  private Date start;
  private Date end;
  private IntervalEnum interval = IntervalEnum.NONE;

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

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public IntervalEnum getInterval() {
    return interval;
  }

  public void setInterval(IntervalEnum interval) {
    this.interval = interval;
  }

  public String getQwSymbol() {
    return (symbol + "." + exchange.name()).toLowerCase();
  }

  public HistoryRequest deepClone() {
    return (HistoryRequest) super.deepClone();
  }

  @Override
  public String toString() {
    return "HistoryRequest{" +
        "symbol='" + symbol + '\'' +
        ", exchange=" + exchange +
        ", start=" + start +
        ", end=" + end +
        ", interval=" + interval +
        ", gatewayName='" + gatewayName + '\'' +
        '}';
  }
}
