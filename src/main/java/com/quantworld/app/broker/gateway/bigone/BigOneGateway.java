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
package com.quantworld.app.broker.gateway.bigone;

import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.HistoryRequest;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.constants.ExchangeEnum;

import java.util.List;
import java.util.Map;

public class BigOneGateway extends BaseGateway {

  private static final String GATEWAY_NAME = ExchangeEnum.BIGONE.getGatewayName();

  public BigOneGateway() {
    super(GATEWAY_NAME);
  }

  @Override
  public void connect(Map<String, String> setting) {

  }

  @Override
  public void subscribe(SubscribeRequest request) {

  }

  @Override
  public void unSubscribe(SubscribeRequest request) {

  }

  @Override
  public String sendOrder(OrderRequest request) {
    return null;
  }

  @Override
  public void cancelOrder(CancelRequest cancelRequest) {

  }

  @Override
  public void queryAccount() {

  }

  @Override
  public void queryPosition() {

  }

  @Override
  public void queryContract() {

  }

  @Override
  public List<String> queryHistory(HistoryRequest request) {
    return null;
  }

  @Override
  public void close() {

  }
}
