package com.quantworld.app.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "exchange")
public class Exchange extends Entitys implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @Column(nullable = true, length = 50)
  private String userName;

  @Column(nullable = true, length = 50)
  private String exchangeName;

  @Column(nullable = true, length = 50)
  private String digitalCurrencyName;

  @Column(nullable = true, length = 10)
  private boolean buyOrSell;

  @Column(nullable = true, length = 10)
  private char dealStatus; // success, fail, in progress

  @Column(nullable = true, length = 50)
  private String strategyName;

  @Column(nullable = true, length = 50)
  private int currentBalance;

  @Column(nullable = true, length = 50)
  private int originalBalance;

  @Column(nullable = false, insertable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(nullable = false, insertable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModifyTime;

  @Id
  @GenericGenerator(name = "idGenerator", strategy = "uuid")
  @GeneratedValue(generator = "idGenerator")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  public String getDigitalCurrencyName() {
    return digitalCurrencyName;
  }

  public void setDigitalCurrencyName(String digitalCurrencyName) {
    this.digitalCurrencyName = digitalCurrencyName;
  }

  public boolean isBuyOrSell() {
    return buyOrSell;
  }

  public void setBuyOrSell(boolean buyOrSell) {
    this.buyOrSell = buyOrSell;
  }

  public char getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(char dealStatus) {
    this.dealStatus = dealStatus;
  }

  public String getStrategyName() {
    return strategyName;
  }

  public void setStrategyName(String strategyName) {
    this.strategyName = strategyName;
  }

  public int getCurrentBalance() {
    return currentBalance;
  }

  public void setCurrentBalance(int currentBalance) {
    this.currentBalance = currentBalance;
  }

  public int getOriginalBalance() {
    return originalBalance;
  }

  public void setOriginalBalance(int originalBalance) {
    this.originalBalance = originalBalance;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getLastModifyTime() {
    return lastModifyTime;
  }

  public void setLastModifyTime(Date lastModifyTime) {
    this.lastModifyTime = lastModifyTime;
  }
}
