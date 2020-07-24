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
package com.quantworld.app.broker.gateway;

import org.java_websocket.client.WebSocketClient;

/**
 * @author: Shawn
 * @Date: 3/5/2020
 * @Description:
 */
public class QuantWebSocketClient {

  private WebSocketClient webSocketClient;

  public QuantWebSocketClient(WebSocketClient webSocketClient) {
    this.webSocketClient = webSocketClient;
  }

  public boolean isConnected() {
    try {
      return webSocketClient != null && webSocketClient.getConnection().isOpen();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * websocket断了之后需要重连，所以需要新的线程和对象，所以目前这样做是最好的。如果用spring框架创建单例对象，
   * websocket不能正常工作。
   * 问题：如果线程太多，
   *
   * @param doesReconnect: 初始值是false，但是断了重连后，该值会变成true
   */
  public void start(Boolean doesReconnect) {
    if (!isConnected()) {
      new WebsocketCreationUtil(webSocketClient, doesReconnect).run();
    }
  }
}
