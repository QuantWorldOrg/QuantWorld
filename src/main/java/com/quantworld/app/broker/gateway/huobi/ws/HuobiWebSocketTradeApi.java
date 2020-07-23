package com.quantworld.app.broker.gateway.huobi.ws;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.LocalOrderManager;
import com.quantworld.app.broker.gateway.QuantWebSocketClient;
import com.quantworld.app.broker.gateway.huobi.HuobiGateway;
import com.quantworld.app.broker.gateway.huobi.sdk.ApiSignature;
import com.quantworld.app.broker.gateway.huobi.sdk.util.InternalUtils;
import com.quantworld.app.broker.gateway.huobi.sdk.util.UrlParamsBuilder;
import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.StatusEnum;
import com.quantworld.app.utils.DateUtils;
import com.quantworld.app.broker.gateway.huobi.HuobiContants;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HuobiWebSocketTradeApi extends HuobiWebSocketApiBase {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static String tradeURI = HuobiContants.WSS_PROTOCOL + HuobiContants.SIGN_HOST + ":" + HuobiContants.PORT + HuobiContants.AO_PATH;

  private Map<String, AccountData> accountDataMap = new ConcurrentHashMap<>();

  private Map<String, OrderData> orderDataMap = new ConcurrentHashMap<>();

  private Map<String, TradeData> tradeDataMap = new ConcurrentHashMap<>();

  private Map<String, PositionData> positionDataMap = new ConcurrentHashMap<>();

  private LocalOrderManager orderManager;

  private Set<String> subTopic = new HashSet<>();


  public HuobiWebSocketTradeApi(BaseGateway gateway) throws URISyntaxException {
    super(tradeURI, gateway);
    if (gateway.getOrderManager() == null) {
      gateway.setOrderManager(new LocalOrderManager(gateway) {
        @Override
        public void pushDataCallback(JSONObject data) {
          onData(data);
        }
      });
    }
    this.orderManager = gateway.getOrderManager();
  }

  public HuobiWebSocketTradeApi(URI serverUri, BaseGateway gateway) {
    super(serverUri, gateway);
  }

  public HuobiWebSocketTradeApi(URI serverUri) {
    super(serverUri);
  }

  public HuobiWebSocketTradeApi(URI serverUri, Draft protocolDraft) {
    super(serverUri, protocolDraft);
  }

  public HuobiWebSocketTradeApi(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  public HuobiWebSocketTradeApi(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
    super(serverUri, protocolDraft, httpHeaders);
  }

  public HuobiWebSocketTradeApi(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders,
                                int connectTimeout) {
    super(serverUri, protocolDraft, httpHeaders, connectTimeout);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    if (HuobiContants.SUCCESS_CODE == handshakedata.getHttpStatus()) {
      gateway.writeLog("交易 Websocket API 2.0 连接成功");
      login();
      InternalUtils.await(100);
      resetRequestQueue("交易 Websocket");
    }
  }

  @Override
  public void onLogin() {
    gateway.writeLog("交易Websocket API 2.0 登录成功");
  }

  public void login() {
    sendAuth(HuobiContants.ACCESS_KEY, HuobiContants.SECRET_KEY, HuobiContants.SIGN_HOST);
  }

  /**
   * V2
   *
   * @param apiKey
   * @param secretKey
   * @param tradingHost
   */
  @Override
  protected void sendAuth(String apiKey, String secretKey, String tradingHost) {
    UrlParamsBuilder builder;
    try {
      URI uri = new URI(HuobiContants.WSS_PROTOCOL + HuobiContants.SIGN_HOST + HuobiContants.AO_PATH);
      builder = ApiSignature.createSignature(apiKey, secretKey, "GET", tradingHost, uri.getPath());
    } catch (Exception e) {
      gateway.writeLog(e.getMessage());
      return;
    }
    builder.putToUrl(ApiSignature.op, ApiSignature.opValue)
        .putToUrl("cid", System.currentTimeMillis());
    send(builder.buildUrlToJsonString());
  }

  @Override
  public void subscribe(SubscribeRequest request) {
    if (request == null || subTopic.contains(request.getSymbol())) {
      return;
    }
    subTopic.add(request.getSymbol());
    offerRequest(request);
    String symbol = request.getSymbol();
    AccountData accountData = new AccountData();
    accountData.setAsset(symbol);
    accountDataMap.put(symbol, accountData);

    OrderData orderData = new OrderData();
    orderData.setExchange(ExchangeEnum.HUOBI);
    orderData.setGatewayName(gatewayName);
    orderData.setSymbol(symbol);
    orderData.setTime(DateUtils.getCurrentTime());
    orderDataMap.put(symbol, orderData);

    TradeData tradeData = new TradeData();
    tradeData.setExchange(ExchangeEnum.HUOBI);
    tradeData.setGatewayName(gatewayName);
    tradeData.setSymbol(symbol);
    tradeData.setTime(DateUtils.getCurrentTime());
    tradeDataMap.put(symbol, tradeData);

    PositionData positionData = new PositionData();
    positionData.setExchange(ExchangeEnum.HUOBI);
    positionData.setGatewayName(gatewayName);
    positionData.setSymbol(symbol);
    positionDataMap.put(symbol, positionData);

    JSONObject subOrders = new JSONObject();
    subOrders.put("op", "sub");
    subOrders.put("cid", System.currentTimeMillis());
    subOrders.put("topic", String.format("orders.%s.update", symbol));
    send(subOrders.toJSONString());
  }

  @Override
  public void unSubscribe(SubscribeRequest request) {
    // TODO
  }

  @Override
  public void onData(JSONObject data) {
    if (data == null) {
      return;
    }
    logger.info("更新数据：{}.", data.toJSONString());
    String op = data.getString("op");
    if (!"notify".equals(op)) {
      return;
    }

    String topic = data.getString("topic");
    if (topic.contains("orders")) {
      onOrder(data);
    }
  }

  private void onOrder(JSONObject data) {
    logger.info("订单：{}.", data.toJSONString());
    JSONObject tradingData = data.getJSONObject("data");
    String sysOrderId = tradingData.getString("order-id");
    OrderData orderData = orderManager.getOrderWithSysOrderId(sysOrderId);
    if (orderData == null) {
      orderManager.addPushData(sysOrderId, data);
      return;
    }
    float tradeVolume = data.getFloat("filled-amount");
    float orderTraded = orderData.getTraded() + tradeVolume;
    orderData.setTraded(orderTraded);
    StatusEnum statusEnum;
    if (HuobiContants.HUOBI_ORDER_STATUS.get(tradingData.getString("order-state")) == null) {
      statusEnum = StatusEnum.NONE;
    } else {
      statusEnum = HuobiContants.HUOBI_ORDER_STATUS.get(tradingData.getString("order-state"));
    }
    orderData.setStatus(statusEnum);
    orderManager.onOrder(orderData);
    if (tradeVolume == 0) {
      return;
    }
    TradeData tradeData = new TradeData();
    tradeData.setSymbol(orderData.getSymbol());
    tradeData.setExchange(ExchangeEnum.HUOBI);
    tradeData.setOrderId(orderData.getOrderId());
    tradeData.setTradeId(data.getString("seq-id"));
    tradeData.setDirection(orderData.getDirection());
    tradeData.setPrice(data.getFloat("price"));
    tradeData.setVolume(data.getFloat("filled-amount"));
    tradeData.setTime(DateUtils.getCurrentTime());
    tradeData.setGatewayName(gatewayName);
    gateway.onTrade(tradeData);
  }

  @Override
  public void onReconnect() {
    try {
      if (subTopic == null) {
        return;
      }
      subTopic.clear();
      HuobiGateway huobiGateWay = (HuobiGateway) gateway;
      HuobiWebSocketTradeApi reconnectSocketDataTradeApi = new HuobiWebSocketTradeApi(huobiGateWay);
      reconnectSocketDataTradeApi.setSubscribeRequestQueue(subscribeRequestQueue);
//      huobiGateWay.setHuobiWebSocketTradeApi(reconnectSocketDataTradeApi);
      new QuantWebSocketClient(reconnectSocketDataTradeApi).start(true);

      String subject = ExchangeEnum.HUOBI.name() + "交易API重连";
      String content = "网络不稳定，websocket交易api尝试重连";
      sendEmail(subject, content);
    } catch (Exception e) {
      gateway.writeLog("市场行情重连失败");
    }
  }
}
