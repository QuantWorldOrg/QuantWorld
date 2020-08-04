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
package com.quantworld.app.broker.gateway.huobi;

import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.huobi.api.HuobiRestApi;
import com.quantworld.app.broker.gateway.huobi.ws.HuobiWebSocketTradeApiV21;
import com.quantworld.app.broker.gateway.huobi.ws.HuobiWebSocketDataApi;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.HistoryRequest;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.constants.ExchangeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class HuobiGateway extends BaseGateway {

  private static final Logger logger = LoggerFactory.getLogger(HuobiGateway.class);

  private HuobiWebSocketDataApi huobiWebSocketDataApi;

  private HuobiWebSocketTradeApiV21 huobiWebSocketTradeApiV21;

  private HuobiRestApi huobiRestApi;

  private static final String GATEWAY_NAME = ExchangeEnum.HUOBI.getGatewayName();

  public HuobiGateway() {
    super(GATEWAY_NAME);
    try {
      this.huobiWebSocketDataApi = new HuobiWebSocketDataApi(this);
      this.huobiWebSocketTradeApiV21 = new HuobiWebSocketTradeApiV21(this);
      this.huobiRestApi = new HuobiRestApi(this);
    } catch (URISyntaxException e) {
      logger.error("Failed to initiate gateway: {}", GATEWAY_NAME);
    }
  }

  @Override
  public void connect(Map<String, String> setting) {
    connect();
  }

  private void connect() {
    if (!huobiWebSocketDataApi.isOpen()) {
      huobiWebSocketDataApi.connect();
    }

    if (!huobiWebSocketTradeApiV21.isOpen()) {
      huobiWebSocketTradeApiV21.connect();
    }
  }

  @Override
  public void subscribe(SubscribeRequest request) {
    huobiWebSocketDataApi.subscribe(request);
    huobiWebSocketTradeApiV21.subscribe(request);
  }

  @Override
  public void unSubscribe(SubscribeRequest request) {
    huobiWebSocketDataApi.unSubscribe(request);
    huobiWebSocketTradeApiV21.unSubscribe(request);
  }

  @Override
  public String sendOrder(OrderRequest request) {
    return huobiRestApi.sendOrder(request);
  }

  @Override
  public void cancelOrder(CancelRequest cancelRequest) {
    huobiRestApi.cancelOrder(cancelRequest);
  }

  @Override
  public void queryAccount() {
    huobiRestApi.queryAccount();
  }

  @Override
  public void queryPosition() {

  }

  @Override
  public void queryContract() {
    huobiRestApi.queryContract();
  }

  @Override
  public List<String> queryHistory(HistoryRequest request) {
    return null;
  }

  @Override
  public void close() {

  }

  public void processTimerEvent() {

  }

  public void initQuery() {

  }

  public HuobiWebSocketDataApi getHuobiWebSocketDataApi() {
    return huobiWebSocketDataApi;
  }

  public void setHuobiWebSocketDataApi(HuobiWebSocketDataApi huobiWebSocketDataApi) {
    this.huobiWebSocketDataApi = huobiWebSocketDataApi;
  }

//  public HuobiWebSocketTradeApi getHuobiWebSocketTradeApi() {
//    return huobiWebSocketTradeApi;
//  }
//
//  public void setHuobiWebSocketTradeApi(HuobiWebSocketTradeApi huobiWebSocketTradeApi) {
//    this.huobiWebSocketTradeApi = huobiWebSocketTradeApi;
//  }

  public HuobiWebSocketTradeApiV21 getHuobiWebSocketTradeApiV21() {
    return huobiWebSocketTradeApiV21;
  }

  public void setHuobiWebSocketTradeApiV21(HuobiWebSocketTradeApiV21 huobiWebSocketTradeApiV21) {
    this.huobiWebSocketTradeApiV21 = huobiWebSocketTradeApiV21;
  }
}
