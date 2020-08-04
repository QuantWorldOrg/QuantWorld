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
package com.quantworld.app.broker.gateway.huobi.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.QWContext;
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.huobi.sdk.util.InternalUtils;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.utils.ProxyUtil;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HuobiWebSocketApiBase extends WebSocketClient {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  protected BaseGateway gateway;

  protected String gatewayName;

  protected Queue<SubscribeRequest> subscribeRequestQueue = new ConcurrentLinkedQueue<>();

  protected EventDispatcher eventDispatcher;

  protected String accessKey;

  protected String secretKey;

  //TODO extract the enum to constant to simplify the code
  protected enum SocketEnum {

    AMOUNT("amount"),
    ASKS("asks"),
    BBO("bbo"),
    BIDS("bids"),
    CHANNEL("ch"),
    CLOSE("close"),
    COUNT("count"),
    DEPTH("depth"),
    DETAIL("detail"),
    ERR_CODE("err-code"),
    ERR_MSG("err-msg"),
    HIGH("high"),
    ID("id"),
    KLINE("kline"),
    LOW("low"),
    MBP("mbp"),
    OPEN("open"),
    TICK("tick"),
    TRADE("trade"),
    TS("ts"),
    VOL("vol");

    private String name;

    public String getName() {
      return name;
    }

    SocketEnum(String name) {
      this.name = name;
    }
  }

  public HuobiWebSocketApiBase(String uri, BaseGateway gateway) throws URISyntaxException {
    super(new URI(uri), new Draft_6455());
    super.setProxy(ProxyUtil.getProxy());
    this.gateway = gateway;
    this.gatewayName = gateway.getGatewayName();
    this.eventDispatcher = (EventDispatcher) QWContext.getBean(EventDispatcher.NAME);
  }

  public HuobiWebSocketApiBase(URI serverUri, BaseGateway gateway) {
    super(serverUri);
  }

  public HuobiWebSocketApiBase(URI serverUri) {
    super(serverUri);
  }

  public HuobiWebSocketApiBase(URI serverUri, Draft protocolDraft) {
    super(serverUri, protocolDraft);
  }

  public HuobiWebSocketApiBase(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  public HuobiWebSocketApiBase(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
    super(serverUri, protocolDraft, httpHeaders);
  }

  public HuobiWebSocketApiBase(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders,
                               int connectTimeout) {
    super(serverUri, protocolDraft, httpHeaders, connectTimeout);
  }

  public void connect(String key,
                      String secret,
                      String url,
                      String host,
                      String port) {

  }


  public void subscribe(SubscribeRequest request) {

  }

  public void unSubscribe(SubscribeRequest request) {

  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
  }

  @Override
  public void onMessage(String message) {
    JSONObject packet = JSON.parseObject(message);
    if (StringUtils.isNotBlank(message)) {
      if (message.contains("ping")) {
        processPingOnV2TradingLine(packet);
      } else if (message.contains("action") && "auth".equals(packet.getString("ch"))) {
        onLogin();
      } else if (message.contains("err-msg")) {
        onErrorMsg(packet);
      } else if (message.contains("sub")) {
        onSubMsg(packet);
      } else {
        onData(packet);
      }
    }
  }

  @Override
  public void onMessage(ByteBuffer data) {
    try {
      String message = new String(InternalUtils.decode(data.array()));
      JSONObject packet = JSON.parseObject(message);
      if (StringUtils.isNotBlank(message)) {
        if (message.contains("ping")) {
          processPingOnMarketData(packet);
        } else if (message.contains("op") && "auth".equals(packet.getString("op"))) {
          onLogin();
        } else if (message.contains("err-msg")) {
          onErrorMsg(packet);
        } else if (message.contains("subbed")) {
          onSubMsg(packet);
        } else {
          onData(packet);
        }
      }
    } catch (IOException e) {
      gateway.writeLog("获取订阅行情数据失败");
    }
  }

  private void onSubMsg(JSONObject packet) {
    if (packet.containsKey("subbed")) {
      if ("ok".equals(packet.getString("status"))) {
        gateway.writeLog(String.format("成功订阅: %s", packet.getString("subbed")));
      }
    } else if (packet.containsKey("unsubbed")) {
      if ("ok".equals(packet.getString("status"))) {
        gateway.writeLog(String.format("成功取消订阅: %s", packet.getString("subbed")));
      }
    }
  }

  public void onLogin() {
  }

  private void processPingOnMarketData(@NotNull JSONObject packet) {
    String pong = packet.toString();
    send(pong.replace("ping", "pong"));
  }

  private void processPingOnV2TradingLine(JSONObject packet) {
    long ts = packet.getJSONObject("data").getLong("ts");
    String pong = String.format("{\"action\": \"pong\",\"params\": {\"ts\": %d}}", ts);
    send(pong);
  }

  private void onErrorMsg(JSONObject packet) {
    String msg = packet.getString("err-msg");
    if ("invalid pong".equals(msg)) {
      return;
    }
    gateway.writeLog(msg);
  }

  public void onData(JSONObject data) {
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {

    logger.info("********************* CONNECTION CLOSE *********************");
    logger.info("code:{}", code);
    logger.info("reason:{}", reason);
    logger.info("remote:{}", remote);
    try {
      Thread.sleep(5000L);
    } catch (InterruptedException e) {
      logger.error("Thread sleep error:{}", e);
    } finally {
      try {
        // Only connection has been close, system should create one new connection to connect
        // the new websocket connection.
        // If there are multiple reconnection entry, it could be the connections explosion, like
        // 2->4->8->16.......->2^n
        onReconnect();
      } catch (Exception e) {
        logger.error("Reconnect error:{}", e);
      }
    }
  }

  public void onReconnect() {
  }

  protected void sendAuth(String apiKey, String secretKey, String tradingHost) {
  }

  @Override
  public void onError(Exception ex) {

    logger.error("********************* CONNECTION ERROR *********************", ex);
    String message = "";
    try {
      if (ex.getMessage() != null) {
        message = new String(ex.getMessage().getBytes(), "UTF-8");
        logger.error("Connection error mas: {}", message);
      }
    } catch (Exception e) {
      logger.error("Web socket connection error: {}", e);
      logger.error("Error message: {}", message);
    }
  }

  public void offerRequest(SubscribeRequest request) {
    subscribeRequestQueue.offer(request);
    gateway.writeLog(String.format("subscribeRequestQueue的大小：%s", subscribeRequestQueue.size()));
  }

  public SubscribeRequest pollRequest() {
    gateway.writeLog(String.format("subscribeRequestQueue的大小：%s", subscribeRequestQueue.size()));
    return subscribeRequestQueue.poll();
  }

  public void resetRequestQueue(String socketName) {
    if (!subscribeRequestQueue.isEmpty()) {
      Queue<SubscribeRequest> tempQueue = new ConcurrentLinkedQueue<>();
      gateway.writeLog(String.format("%s: Websocket重连重新订阅主题", socketName));
      while (!subscribeRequestQueue.isEmpty()) {
        SubscribeRequest request = pollRequest();
        tempQueue.offer(request);
      }
      while (!tempQueue.isEmpty()) {
        subscribe(tempQueue.poll());
      }
    }
  }

  public Queue<SubscribeRequest> getSubscribeRequestQueue() {
    return subscribeRequestQueue;
  }

  public void setSubscribeRequestQueue(Queue<SubscribeRequest> subscribeRequestQueue) {
    this.subscribeRequestQueue = subscribeRequestQueue;
  }

  protected void sendEmail(String subject, String content) {
    JSONObject data = new JSONObject();
    data.put("subject", subject);
    data.put("content", content);
    Event event = new Event(EventTypeEnum.EVENT_EMAIL, data);
    eventDispatcher.putEvent(event);
  }
}
