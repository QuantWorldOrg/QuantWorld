package com.quantworld.app.broker.gateway.huobi.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashBiMap;
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.LocalOrderManager;
import com.quantworld.app.broker.gateway.huobi.HuobiContants;
import com.quantworld.app.broker.gateway.huobi.sdk.ApiSignature;
import com.quantworld.app.broker.gateway.huobi.sdk.util.UrlParamsBuilder;
import com.quantworld.app.data.AccountData;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.OrderData;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OrderTypeEnum;
import com.quantworld.app.data.constants.ProductEnum;
import com.quantworld.app.data.constants.StatusEnum;
import com.quantworld.app.utils.DateUtils;
import com.quantworld.app.utils.ProxyUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.quantworld.app.broker.gateway.huobi.HuobiContants.HUOBI_ORDER_STATE;

public class HuobiRestApi extends RestTemplate {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private BaseGateway gateway;

  private String accountId;

  private static String url = HuobiContants.HTTPS_PROTOCOL + HuobiContants.SIGN_HOST;

  private LocalOrderManager orderManager;

  private static HashBiMap<DirectionEnum, String> DIRECTION_MAP = HashBiMap.create();
  private static HashBiMap<OrderTypeEnum, String> ORDER_TYPE_MAP = HashBiMap.create();

  static {
    DIRECTION_MAP.put(DirectionEnum.LONG, "buy");
    DIRECTION_MAP.put(DirectionEnum.SHORT, "sell");
    ORDER_TYPE_MAP.put(OrderTypeEnum.MARKET, "market");
    ORDER_TYPE_MAP.put(OrderTypeEnum.LIMIT, "limit");
  }

  public HuobiRestApi(BaseGateway gateway) {
    this.gateway = gateway;
    this.orderManager = gateway.getOrderManager();
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    Proxy proxy = ProxyUtil.getProxy();
    factory.setProxy(proxy);
    factory.setConnectTimeout(3000);
    factory.setReadTimeout(3000);
    super.setRequestFactory(factory);
  }

  public void queryAccount() {
    StringBuilder path = new StringBuilder();
    String address = "/v1/account/accounts";
    path.append(url).append(address).append(createRequestUrl(address, HuobiContants.GET));
    ResponseEntity<String> responseEntity = null;//getForEntity(new URI(path.toString()), String.class);
    try {
      responseEntity = getForEntity(new URI(path.toString()), String.class);
    } catch (Exception e) {
      gateway.writeLog("查询账户失败。");
    }
    String responseBody = responseEntity.getBody();
    onQueryAccount(JSON.parseObject(responseBody));
  }

  private void onQueryAccount(JSONObject data) {
    if (hasError(data)) {
      return;
    }
    JSONArray array = data.getJSONArray("data");
    for (Object jsonObject : array) {
      JSONObject accountData = ((JSONObject) jsonObject);
      if ("spot".equals(accountData.getString("type"))) {
        accountId = accountData.getString("id");
        gateway.writeLog(String.format("账户代码：%s，查询成功", accountId));
      }
    }
    queryAccountBalance();
  }

  public void queryAccountBalance() {
    StringBuilder path = new StringBuilder();
    String originalUrl = String.format("/v1/account/accounts/%s/balance", accountId);
    path.append(url).append(originalUrl);
    path.append(createRequestUrl(originalUrl, HuobiContants.GET));
    ResponseEntity<String> responseEntity = null;
    try {
      responseEntity = getForEntity(new URI(path.toString()), String.class);
    } catch (Exception e) {
      gateway.writeLog(String.format("账户查询失败: %s", accountId));
    }
    String responseBody = responseEntity.getBody();
    onQueryAccountBalance(JSON.parseObject(responseBody));
  }

  public void queryOrder() {
    StringBuilder path = new StringBuilder();
    String address = "/v1/order/openOrders";
    path.append(url).append(address).append(createRequestUrl(address, HuobiContants.GET));
    ResponseEntity<String> responseEntity = null;
    try {
      responseEntity = getForEntity(new URI(path.toString()), String.class);
    } catch (URISyntaxException e) {
      gateway.writeLog("查询订单失败");
    }
    String responseBody = responseEntity.getBody();
    onQueryOrder(JSON.parseObject(responseBody));
  }

  public void queryContract() {
    StringBuilder path = new StringBuilder();
    String address = "/v1/common/symbols";
    path.append(url).append(address).append(createRequestUrl(address, HuobiContants.GET));
    ResponseEntity<String> responseEntity = null;
    try {
      responseEntity = getForEntity(new URI(path.toString()), String.class);
    } catch (URISyntaxException e) {
      gateway.writeLog("查询合约失败");
    }
    String responseBody = responseEntity.getBody();
    onQueryContract(JSON.parseObject(responseBody));
  }


  public String sendOrder(OrderRequest request) {
    if (request == null) {
      return StringUtils.EMPTY;
    }
    StringBuilder path = new StringBuilder();
    String address = "/v1/order/orders/place";
    path.append(url).append(address).append(createRequestUrl(address, HuobiContants.POST));
    String type = DIRECTION_MAP.get(request.getDirection()) + "-" + ORDER_TYPE_MAP.get(request.getType());
    String localOrderId = orderManager.getNewLocalOrderId();
    OrderData orderData = request.createOrderData(localOrderId, gateway.getGatewayName());
    orderData.setTime(DateUtils.getCurrentTime());
    JSONObject postData = new JSONObject();
    postData.put("account-id", accountId);
    postData.put("amount", request.getVolume());
    postData.put("symbol", request.getSymbol());
    postData.put("type", type);
    postData.put("price", request.getPrice());
    postData.put("client-order-id", localOrderId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity httpEntity = new HttpEntity(postData, headers);
    try {
      ResponseEntity<String> responseEntity = postForEntity(new URI(path.toString()), httpEntity, String.class);
      onSendOrder(localOrderId, JSON.parseObject(responseEntity.getBody()), orderData);
    } catch (Exception e) {
      gateway.writeLog(String.format("发送订单失败，订单ID: %s", localOrderId));
      onSendOrderFailed(orderData);
    }
    return orderData.getQwOrderId();
  }

  public String getOpenOrders() {

    StringBuilder path = new StringBuilder();
    String address = "/v1/order/openOrders";
    path.append(url).append(address).append(createRequestUrl(address, HuobiContants.GET));
    ResponseEntity<String> responseEntity = null;
    try {
      responseEntity = getForEntity(new URI(path.toString()), String.class);
    } catch (Exception e) {
      gateway.writeLog("查询账户失败。");
    }
    return responseEntity.getBody();
  }

  private void onSendOrderFailed(OrderData orderData) {
    orderData.setGatewayName(gateway.getGatewayName());
    orderData.setStatus(StatusEnum.REJECTED);
    gateway.onOrder(orderData);
  }


  public void cancelOrder(CancelRequest request) {
    String sysOrderId = orderManager.getSysOrderId(request.getOrderId());
    if (sysOrderId != null) {
      StringBuilder path = new StringBuilder();
      String originalUrl = String.format("/v1/order/orders/%s/submitcancel", sysOrderId);
      path.append(url).append(originalUrl);
      path.append(createRequestUrl(originalUrl, HuobiContants.POST));
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity httpEntity = new HttpEntity(new JSONObject(), headers);
      // It can response the system order id
      try {
        ResponseEntity<String> responseEntity = postForEntity(new URI(path.toString()), httpEntity, String.class);
        onCancelOrder(request.getOrderId(), JSON.parseObject(responseEntity.getBody()));
      } catch (Exception e) {
        gateway.writeLog(String.format("取消订单失败，订单ID: %s", request.getOrderId()));
      }
    } else {
      gateway.writeLog(String.format("系统订单ID为空: %s", request.getSymbol()));
    }
  }

  private void onQueryOrder(JSONObject data) {
    if (hasError(data)) {
      return;
    }
    JSONArray orders = data.getJSONArray("data");
    for (Object orderObj : orders) {
      JSONObject jsonObject = (JSONObject) orderObj;
      String sysOrderId = jsonObject.getString("id");
      String localOrderId = orderManager.getLocalOrderId(sysOrderId);
      String type = jsonObject.getString("type");
      String[] directionAndOrderType = type.split("-");
      DirectionEnum directionEnum = DIRECTION_MAP.inverse().get(directionAndOrderType[0]);
      OrderTypeEnum orderTypeEnum = ORDER_TYPE_MAP.inverse().get(directionAndOrderType[1]);
      long time = jsonObject.getLong("created-at");

      OrderData orderData = new OrderData();
      orderData.setOrderId(localOrderId);
      orderData.setSymbol(jsonObject.getString("symbol"));
      orderData.setExchange(ExchangeEnum.HUOBI);
      orderData.setPrice(jsonObject.getFloat("price"));
      orderData.setVolume(jsonObject.getFloat("amount"));
      orderData.setType(orderTypeEnum);
      orderData.setDirection(directionEnum);
      orderData.setStatus(HuobiContants.HUOBI_ORDER_STATUS.get(jsonObject.getString("state")));
      orderData.setTime(time);
      orderData.setGatewayName(gateway.getGatewayName());

      orderManager.onOrder(orderData);
    }
    gateway.writeLog("委托信息查询成功");
  }

  private void onQueryContract(JSONObject data) {
    if (hasError(data)) {
      return;
    }
    JSONArray orders = data.getJSONArray("data");
    for (Object orderObj : orders) {
      JSONObject jsonObject = (JSONObject) orderObj;
      String baseCur = jsonObject.getString("base-currency");
      String quoteCur = jsonObject.getString("quote-currency");
      String name = baseCur + "/" + quoteCur;
      ContractData contractData = new ContractData();
      contractData.setSymbol(jsonObject.getString("symbol"));
      contractData.setExchange(ExchangeEnum.HUOBI);
      contractData.setName(name.toLowerCase());
      contractData.setSize(1);
      contractData.setProduct(ProductEnum.SPOT);
      contractData.setGatewayName(gateway.getGatewayName());
      contractData.setMaxOrderAmount(jsonObject.getDouble("max-order-amt").floatValue());
      contractData.setMinOrderValue(jsonObject.getDouble("min-order-value").floatValue());
      contractData.setMinOrderAmount(jsonObject.getDouble("min-order-amt").floatValue());
      contractData.setPricePrecision(jsonObject.getDouble("price-precision").floatValue());

      gateway.onContract(contractData);
      gateway.updateSymbolNameMap(contractData.getSymbol(), contractData.getName());
    }
    gateway.writeLog("合约查询成功");
  }

  private void onSendOrder(String localOrderId, JSONObject data, OrderData orderData) {

    orderData.setGatewayName(gateway.getGatewayName());
    if (hasError(data, "委托")) {
      orderData.setStatus(StatusEnum.REJECTED);
      logger.info("订单发生错误onOrder:{}, {}", orderData.getQwOrderId(), orderData.getStatus());
      orderManager.onOrder(orderData);
      return;
    }
    orderData.setStatus(StatusEnum.SUBMITTING);
    logger.info("提交并onOrder:{}", orderData.getQwOrderId());
    orderManager.onOrder(orderData);
    if (data.getString("data") != null) {
      String sysOrderId = data.getString("data");
      orderManager.updateOrderIdMap(localOrderId, sysOrderId);
    }
  }

  private void onCancelOrder(String localOrderId, JSONObject data) {
    OrderData orderData = orderManager.getOrderWithLocalOrderId(localOrderId);
    if (hasError(data, "撤单")) {
      orderData.setStatus(StatusEnum.REJECTED);
    } else {
      orderData.setStatus(StatusEnum.CANCELED);
      gateway.writeLog(String.format("委托撤单成功：%s", orderData.getOrderId()));
    }
    orderData.setGatewayName(gateway.getGatewayName());
    orderManager.onOrder(orderData);
  }

  private void onQueryAccountBalance(JSONObject data) {
    if (hasError(data)) {
      return;
    }
    Map<String, Map<String, Float>> currencyInfo = new HashMap<>();
    JSONArray array = data.getJSONObject("data").getJSONArray("list");
    for (Object object : array) {
      JSONObject jsonObject = (JSONObject) object;
      String currency = jsonObject.getString("currency");
      String type = jsonObject.getString("type");
      float balance = jsonObject.getFloat("balance");

      if (currencyInfo.get(currency) == null) {
        Map<String, Float> balanceAndFrozen = new HashMap<>();
        balanceAndFrozen.put(type, balance);
        currencyInfo.put(currency, balanceAndFrozen);
      } else {
        currencyInfo.get(currency).put(type, balance);
      }
    }

    for (Map.Entry<String, Map<String, Float>> entry : currencyInfo.entrySet()) {
      Map<String, Float> balanceAndFrozen = entry.getValue();
      float trade = balanceAndFrozen.get("trade");
      float frozen = balanceAndFrozen.get("frozen");
      float balance = trade + frozen;
      AccountData accountData = new AccountData();
      accountData.setAccountId(accountId);
      accountData.setAsset(entry.getKey());
      accountData.setBalance(balance);
      accountData.setAvailable(trade);
      accountData.setFrozen(frozen);
      accountData.setGatewayName(gateway.getGatewayName());
      accountData.setExchange(ExchangeEnum.HUOBI);
      if (balance > 0) {
        gateway.onAccount(accountData);
      }
    }
  }


  public boolean hasError(JSONObject data, String function) {
    if (!"error".equals(data.getString("status"))) {
      return false;
    }
    String errorCode = data.getString("err-code");
    String errorMsg = data.getString("err-msg");
    String orderState = data.getString("order-state");

    gateway.writeLog(String.format("%s请求出错，代码:%s, 信息:%s, 订单状态:%s", function, errorCode, errorMsg, orderState));
    return true;
  }

  public boolean hasError(JSONObject data) {
    return hasError(data, StringUtils.EMPTY);
  }

  private String createRequestUrl(String address, String method) {
    UrlParamsBuilder builder = ApiSignature.createSignature(HuobiContants.ACCESS_KEY, HuobiContants.SECRET_KEY, method, HuobiContants.SIGN_HOST, address);
    return builder.buildUrl();
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
}
