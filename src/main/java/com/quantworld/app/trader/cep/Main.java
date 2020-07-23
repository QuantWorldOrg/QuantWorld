package com.quantworld.app.trader.cep;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: Shawn
 * @Date: 10/18/2019
 * @Description:
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  private static final Lock lock = new ReentrantLock();

  private volatile String orderId = StringUtils.EMPTY;
  private static AtomicInteger atomicInteger = new AtomicInteger(0);

  public static void main(String[] args) {

  }

  public void processOrderId() {
    if (StringUtils.isNotBlank(getOrderId())) {
      System.out.println(System.currentTimeMillis() + ": Out");
      return;
    }
    System.out.println(System.currentTimeMillis() + ": In");
    setOrderId("newOrderId: " + 50);
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
