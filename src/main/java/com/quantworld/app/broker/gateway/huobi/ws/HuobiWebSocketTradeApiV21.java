package com.quantworld.app.broker.gateway.huobi.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.LocalOrderManager;
import com.quantworld.app.broker.gateway.QuantWebSocketClient;
import com.quantworld.app.broker.gateway.huobi.HuobiGateway;
import com.quantworld.app.broker.gateway.huobi.sdk.ApiSignatureV2;
import com.quantworld.app.broker.gateway.huobi.sdk.util.InternalUtils;
import com.quantworld.app.broker.gateway.huobi.sdk.util.UrlParamsBuilder;
import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.PositionData;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.TradeData;
import com.quantworld.app.data.constants.DirectionEnum;
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

public class HuobiWebSocketTradeApiV21 extends HuobiWebSocketApiBase {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static String tradeURI = HuobiContants.WSS_PROTOCOL + HuobiContants.SIGN_HOST + ":" + HuobiContants.PORT + HuobiContants.AO_PATH_V2;

  private Map<String, AccountData> accountDataMap = new ConcurrentHashMap<>();

  private Map<String, OrderData> orderDataMap = new ConcurrentHashMap<>();

  private Map<String, TradeData> tradeDataMap = new ConcurrentHashMap<>();

  private Map<String, PositionData> positionDataMap = new ConcurrentHashMap<>();

  private LocalOrderManager orderManager;

  private Set<String> subTopic = new HashSet<>();

  public HuobiWebSocketTradeApiV21(BaseGateway gateway) throws URISyntaxException {
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

  public HuobiWebSocketTradeApiV21(URI serverUri, BaseGateway gateway) {
    super(serverUri, gateway);
  }

  public HuobiWebSocketTradeApiV21(URI serverUri) {
    super(serverUri);
  }

  public HuobiWebSocketTradeApiV21(URI serverUri, Draft protocolDraft) {
    super(serverUri, protocolDraft);
  }

  public HuobiWebSocketTradeApiV21(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  public HuobiWebSocketTradeApiV21(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
    super(serverUri, protocolDraft, httpHeaders);
  }

  public HuobiWebSocketTradeApiV21(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders,
                                   int connectTimeout) {
    super(serverUri, protocolDraft, httpHeaders, connectTimeout);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    if (HuobiContants.SUCCESS_CODE == handshakedata.getHttpStatus()) {
      gateway.writeLog("资产 Websocket API 2.1 连接成功");
      login();
      InternalUtils.await(100);
      resetRequestQueue("资产 Websocket");
    }
  }

  @Override
  public void onLogin() {
    gateway.writeLog("资产Websocket API 2.1 登录成功");
  }

  public void login() {
    sendAuth(HuobiContants.ACCESS_KEY, HuobiContants.SECRET_KEY, HuobiContants.SIGN_HOST);
  }

  /**
   * V2_1
   *
   * @param apiKey
   * @param secretKey
   * @param tradingHost
   */
  protected void sendAuth(String apiKey, String secretKey, String tradingHost) {
    UrlParamsBuilder builder;
    try {
      URI uri = new URI(HuobiContants.WSS_PROTOCOL + HuobiContants.SIGN_HOST + HuobiContants.AO_PATH_V2);
      builder = ApiSignatureV2.createSignature(apiKey, secretKey, "GET", tradingHost, uri.getPath());
    } catch (Exception e) {
      gateway.writeLog(e.getMessage());
      return;
    }

    JSONObject signObj = JSON.parseObject(builder.buildUrlToJsonString());
    signObj.put("authType", "api");

    JSONObject json = new JSONObject();
    json.put("action", "req");
    json.put("ch", "auth");
    json.put("params", signObj);
    send(json.toJSONString());
  }

  @Override
  public void subscribe(SubscribeRequest request) {
    if (request == null || subTopic.contains(request.getSymbol())) {
      return;
    }

    String accountTopic = "accounts.update#1";
    if (!subTopic.contains(accountTopic)) {
      JSONObject subAccount = new JSONObject();
      subAccount.put("action", "sub");
      subAccount.put("ch", accountTopic);
      send(subAccount.toJSONString());
      subTopic.add(accountTopic);
    }

    String ordersTopic = "orders#" + request.getSymbol();
    if (!subTopic.contains(ordersTopic)) {
      JSONObject subOrders = new JSONObject();
      subOrders.put("action", "sub");
      subOrders.put("ch", ordersTopic);
      send(subOrders.toJSONString());
      subTopic.add(ordersTopic);
    }

    String tradeTopic = "trade.clearing#" + request.getSymbol();
    if (!subTopic.contains(tradeTopic)) {
      JSONObject subOrders = new JSONObject();
      subOrders.put("action", "sub");
      subOrders.put("ch", tradeTopic);
      send(subOrders.toJSONString());
      subTopic.add(tradeTopic);
    }
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
    String action = data.getString("action");
    if (!"push".equals(action)) {
      return;
    }

    String ch = data.getString("ch");
    if (ch.contains("accounts.update")) {
      onAccount(data);
    }

    if (ch.contains("trade.clearing")) {
      onTrade(data);
    }

    if (ch.contains("orders")) {
      onOrder(data);
    }
  }

  /**
   * Response format:
   * {
   * "action":"push",
   * "ch":"orders#btcusdt",
   * "data":
   * {
   * "orderSize":"2.000000000000000000",
   * "orderCreateTime":1583853365586,
   * "orderPrice":"77.000000000000000000",
   * "type":"sell-limit",
   * "orderId":27163533,
   * "clientOrderId":"liujin",
   * "orderStatus":"submitted",
   * "symbol":"btcusdt",
   * "eventType":"creation"
   * }
   * }
   */
  private void onOrder(JSONObject data) {
    logger.info("订单：{}.", data.toJSONString());
    JSONObject orderUpdatingData = data.getJSONObject("data");
    String sysOrderId = orderUpdatingData.getString("orderId");
    OrderData orderData = orderManager.getOrderWithSysOrderId(sysOrderId);
    if (orderData == null) {
      orderManager.addPushData(sysOrderId, data);
      return;
    }

    orderData.setVolume(data.getFloatValue("tradeVolume"));
    orderData.setRemainAmount(data.getFloatValue("remainAmt"));
    orderData.setGatewayName(gatewayName);

    StatusEnum statusEnum;
    if (HuobiContants.HUOBI_ORDER_STATUS.get(orderUpdatingData.getString("orderStatus")) == null) {
      statusEnum = StatusEnum.NONE;
    } else {
      statusEnum = HuobiContants.HUOBI_ORDER_STATUS.get(orderUpdatingData.getString("orderStatus"));
    }
    orderData.setStatus(statusEnum);
    orderManager.onOrder(orderData);
  }

  /**
   * {
   * "ch": "trade.clearing#btcusdt",
   * "data": {
   * "symbol": "btcusdt",
   * "orderId": 99998888,
   * "tradePrice": "9999.99",
   * "tradeVolume": "0.96",
   * "orderSide": "buy",
   * "aggressor": true,
   * "tradeId": 919219323232,
   * "tradeTime": 998787897878,
   * "transactFee": "19.88",
   * " feeDeduct ": "0",
   * " feeDeductType": ""
   * }
   * }
   *
   * @param data
   */
  private void onTrade(JSONObject data) {
    logger.info("清算：{}.", data.toJSONString());
    JSONObject tradingData = data.getJSONObject("data");

    if (tradingData.getFloatValue("tradeVolume") == 0) {
      return;
    }
    TradeData tradeData = new TradeData();
    tradeData.setSymbol(tradingData.getString("symbol"));
    tradeData.setExchange(ExchangeEnum.HUOBI);
    tradeData.setOrderId(tradingData.getString("orderId"));
    tradeData.setTradeId(tradingData.getString("tradeId"));
    tradeData.setPrice(tradingData.getFloat("tradePrice"));
    tradeData.setVolume(tradingData.getFloat("tradeVolume"));
    tradeData.setTime(DateUtils.getCurrentTime());
    tradeData.setFee(tradingData.getFloat("transactFee"));
    tradeData.setGatewayName(gatewayName);
    if ("buy".equals(tradingData.getString("orderSide"))) {
      tradeData.setDirection(DirectionEnum.LONG);
    } else {
      tradeData.setDirection(DirectionEnum.SHORT);
    }
    gateway.onTrade(tradeData);
  }

  private void onAccount(JSONObject data) {
    logger.info("账户：{}", data.toString());
    JSONObject accountsJSONData = data.getJSONObject("data");
    String symbol = accountsJSONData.getString("currency");
    String accountId = accountsJSONData.getString("accountId");
    // TODO: special case, I don't know why the account id is 5811085
    if ("5811085".equals(accountId)) {
      return;
    }
    float available = accountsJSONData.getFloatValue("available");
    float balance = accountsJSONData.getFloatValue("balance");
    AccountData accountData = accountDataMap.get(symbol);
    if (accountData == null) {
      accountData = new AccountData();
    }

    accountData.setAccountId(accountId);
    accountData.setAsset(symbol);
    accountData.setBalance(balance);
    accountData.setFrozen(balance - available);
    accountData.setAvailable(available);
    accountData.setExchange(ExchangeEnum.HUOBI);
    accountData.setGatewayName(gatewayName);
    accountDataMap.put(symbol, accountData);
    if (balance > 0) {
      gateway.onAccount(accountData);
    }
  }

  @Override
  public void onReconnect() {
    try {
      if (subTopic == null) {
        return;
      }

      subTopic.clear();
      HuobiGateway huobiGateWay = (HuobiGateway) gateway;
      HuobiWebSocketTradeApiV21 reconnectSocketDataAccountApi = new HuobiWebSocketTradeApiV21(huobiGateWay);
      reconnectSocketDataAccountApi.setSubscribeRequestQueue(subscribeRequestQueue);
      huobiGateWay.setHuobiWebSocketTradeApiV21(reconnectSocketDataAccountApi);
      new QuantWebSocketClient(reconnectSocketDataAccountApi).start(true);
      String subject = ExchangeEnum.HUOBI.name() + "资产API重连";
      String content = "网络不稳定，websocket资产api尝试重连";
      sendEmail(subject, content);
    } catch (Exception e) {
      gateway.writeLog("账户信息重连失败");
    }
  }
}
