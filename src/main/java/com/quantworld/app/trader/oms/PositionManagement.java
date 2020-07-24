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
package com.quantworld.app.trader.oms;

import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OffsetEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionManagement {

  private String qwSymbol;
  private ExchangeEnum exchange;
  private Map<String, OrderData> activeOrders = new ConcurrentHashMap();

  private float longPos = 0;
  private float longYd = 0;
  private float longTd = 0;

  private float shortPos = 0;
  private float shortYd = 0;
  private float shortTd = 0;

  private float longPosFrozen = 0;
  private float longYdFrozen = 0;
  private float longTdFrozen = 0;

  private float shortPosFrozen = 0;
  private float shortYdFrozen = 0;
  private float shortTdFrozen = 0;

  public PositionManagement(ContractData contractData) {
    this.qwSymbol = contractData.getQwSymbol();
    this.exchange = contractData.getExchange();
  }

  public void updatePosition(PositionData positionData) {
    if (DirectionEnum.LONG.equals(positionData.getDirection())) {
      longPos = positionData.getVolume();
      longYd = positionData.getQwVolume();
      longTd = longPos - longYd;
    } else {
      shortPos = positionData.getVolume();
      shortYd = positionData.getQwVolume();
      shortTd = shortPos - shortYd;
    }
  }

  public void updateOrder(OrderData orderData) {
    if (orderData.isActive()) {
      activeOrders.put(orderData.getOrderId(), orderData);
    } else {
      if (activeOrders.containsKey(orderData.getOrderId())) {
        activeOrders.remove(orderData.getOrderId());
      }
    }
  }

  public void updateTrade(TradeData tradeData) {
    if (DirectionEnum.LONG.equals(tradeData.getDirection())) {
      if (OffsetEnum.OPEN.equals(tradeData.getOffset())) {
        longTd += tradeData.getVolume();
      } else if (OffsetEnum.CLOSETODAY.equals(tradeData.getOffset())) {
        shortTd -= tradeData.getVolume();
      } else if (OffsetEnum.CLOSEYESTERDAY.equals(tradeData.getOffset())) {
        shortYd -= tradeData.getVolume();
      } else if (OffsetEnum.CLOSE.equals(tradeData.getOffset())) {
        if (ExchangeEnum.SHFE.equals(tradeData.getExchange())) {
          shortYd -= tradeData.getVolume();
        } else {
          shortTd -= tradeData.getVolume();
          if (shortTd < 0) {
            shortYd += shortTd;
            shortTd = 0;
          }
        }
      }
    } else {
      if (OffsetEnum.OPEN.equals(tradeData.getOffset())) {
        shortTd += tradeData.getVolume();
      } else if (OffsetEnum.CLOSETODAY.equals(tradeData.getOffset())) {
        longTd -= tradeData.getVolume();
      } else if (OffsetEnum.CLOSEYESTERDAY.equals(tradeData.getOffset())) {
        longYd -= tradeData.getVolume();
      } else if (OffsetEnum.CLOSE.equals(tradeData.getOffset())) {
        if (ExchangeEnum.SHFE.equals(tradeData.getExchange())) {
          longYd -= tradeData.getVolume();
        } else {
          longTd -= tradeData.getVolume();
          if (longTd < 0) {
            longYd += longTd;
            longTd = 0;
          }
        }
      }
    }
  }

  public void calculateFrozen() {
    longPosFrozen = 0;
    longYdFrozen = 0;
    longTdFrozen = 0;

    shortPosFrozen = 0;
    shortYdFrozen = 0;
    shortTdFrozen = 0;

    for (Map.Entry<String, OrderData> entry : activeOrders.entrySet()) {
      OrderData orderData = entry.getValue();
      if (OffsetEnum.OPEN.equals(orderData.getOffset())) {
        continue;
      }

      float frozen = orderData.getVolume() - orderData.getTraded();
      if (DirectionEnum.LONG.equals(orderData.getDirection())) {
        if (OffsetEnum.CLOSETODAY.equals(orderData.getOffset())) {
          shortTdFrozen += frozen;
        } else if (OffsetEnum.CLOSEYESTERDAY.equals(orderData.getOffset())) {
          shortYdFrozen += frozen;
        } else if (OffsetEnum.CLOSE.equals(orderData.getOffset())) {
          shortTdFrozen += frozen;

          if (shortTdFrozen > shortTd) {
            shortYdFrozen += (shortTdFrozen - shortTd);
            shortTdFrozen = shortTd;
          }
        }
      } else if (DirectionEnum.SHORT.equals(orderData.getDirection())) {
        if (OffsetEnum.CLOSETODAY.equals(orderData.getOffset())) {
          longTdFrozen += frozen;
        } else if (OffsetEnum.CLOSEYESTERDAY.equals(orderData.getOffset())) {
          longYdFrozen += frozen;
        } else if (OffsetEnum.CLOSE.equals(orderData.getOffset())) {
          longTdFrozen += frozen;
          if (longTdFrozen > longTd) {
            longYdFrozen += (longTdFrozen - longTd);
            longTdFrozen = longTd;
          }
        }
      }

      longPosFrozen = longTdFrozen + longYdFrozen;
      shortPosFrozen = shortTdFrozen + shortYdFrozen;
    }
  }

  public List<OrderRequest> convertOrderRequestShfe(OrderRequest request) {
    if (OffsetEnum.OPEN.equals(request.getOffset())) {
      List<OrderRequest> requestList = new ArrayList<>();
      requestList.add(request);
      return requestList;
    }
    float posAvailable = 0;
    float tdAvailable = 0;
    if (DirectionEnum.LONG.equals(request.getDirection())) {
      posAvailable = shortPos - shortPosFrozen;
      tdAvailable = shortTd - shortTdFrozen;
    } else {
      posAvailable = longPos - longPosFrozen;
      tdAvailable = longTd - longTdFrozen;
    }

    if (request.getVolume() > posAvailable) {
      return new ArrayList<>();
    } else if (request.getVolume() <= tdAvailable) {
      OrderRequest requestTd = (OrderRequest) request.deepClone();
      requestTd.setOffset(OffsetEnum.CLOSETODAY);
      List<OrderRequest> requestList = new ArrayList<>();
      requestList.add(requestTd);
      return requestList;
    } else {
      List<OrderRequest> requestList = new ArrayList<>();
      if (tdAvailable > 0) {
        OrderRequest requestTd = (OrderRequest) request.deepClone();
        requestTd.setOffset(OffsetEnum.CLOSETODAY);
        requestTd.setVolume(tdAvailable);
        requestList.add(requestTd);
      }
      OrderRequest requestYd = (OrderRequest) request.deepClone();
      requestYd.setOffset(OffsetEnum.CLOSEYESTERDAY);
      requestYd.setVolume(request.getVolume() - tdAvailable);
      requestList.add(requestYd);
      return requestList;
    }
  }

  public List<OrderRequest> convertOrderRequestLock(OrderRequest request) {
    float tdVolume = 0;
    float ydAvailable = 0;
    if (DirectionEnum.LONG.equals(request.getDirection())) {
      tdVolume = shortTd;
      ydAvailable = shortYd - shortYdFrozen;
    } else {
      tdVolume = longTd;
      ydAvailable = longYd - longYdFrozen;
    }

    if (tdVolume > 0) {
      List<OrderRequest> requestList = new ArrayList<>();
      OrderRequest requestOpen = (OrderRequest) request.deepClone();
      requestOpen.setOffset(OffsetEnum.OPEN);
      requestList.add(requestOpen);
      return requestList;
    } else {
      float openVolume = Math.max(0, request.getVolume() - ydAvailable);
      List<OrderRequest> requestList = new ArrayList<>();
      if (ydAvailable > 0) {
        OrderRequest requestYd = (OrderRequest) request.deepClone();
        if (ExchangeEnum.SHFE.equals(exchange)) {
          requestYd.setOffset(OffsetEnum.CLOSEYESTERDAY);
        } else {
          requestYd.setOffset(OffsetEnum.CLOSE);
        }
        requestList.add(requestYd);
      }

      if (openVolume > 0) {
        OrderRequest requestOpen = (OrderRequest) request.deepClone();
        requestOpen.setOffset(OffsetEnum.OPEN);
        requestOpen.setVolume(openVolume);
        requestList.add(requestOpen);
      }
      return requestList;
    }
  }
}
