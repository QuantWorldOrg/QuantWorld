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
