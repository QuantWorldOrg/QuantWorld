package com.quantworld.app.broker.gateway.huobi.ws;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.QuantWebSocketClient;
import com.quantworld.app.broker.gateway.huobi.HuobiGateway;
import com.quantworld.app.broker.gateway.huobi.sdk.util.InternalUtils;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.TickData;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.quantworld.app.broker.gateway.huobi.HuobiContants.MARKET_PATH;
import static com.quantworld.app.broker.gateway.huobi.HuobiContants.PORT;
import static com.quantworld.app.broker.gateway.huobi.HuobiContants.SIGN_HOST;
import static com.quantworld.app.broker.gateway.huobi.HuobiContants.SUCCESS_CODE;
import static com.quantworld.app.broker.gateway.huobi.HuobiContants.WSS_PROTOCOL;

public class HuobiWebSocketDataApi extends HuobiWebSocketApiBase {

  private long reqId = 0;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private Map<String, TickData> tickDataMap = new ConcurrentHashMap<>();

  private Set<String> subTopic = new HashSet<>();

  private static String tradeURI = WSS_PROTOCOL + SIGN_HOST + ":" + PORT + MARKET_PATH;

  public HuobiWebSocketDataApi(BaseGateway gateway) throws URISyntaxException {
    super(tradeURI, gateway);
  }

  public HuobiWebSocketDataApi(URI serverUri, BaseGateway gateway) {
    super(serverUri, gateway);
  }

  public HuobiWebSocketDataApi(URI serverUri) {
    super(serverUri);
  }

  public HuobiWebSocketDataApi(URI serverUri, Draft protocolDraft) {
    super(serverUri, protocolDraft);
  }

  public HuobiWebSocketDataApi(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  public HuobiWebSocketDataApi(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
    super(serverUri, protocolDraft, httpHeaders);
  }

  public HuobiWebSocketDataApi(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders,
                               int connectTimeout) {
    super(serverUri, protocolDraft, httpHeaders, connectTimeout);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    if (SUCCESS_CODE == handshakedata.getHttpStatus()) {
      gateway.writeLog("行情Websocket API连接成功");
      InternalUtils.await(100);
      resetRequestQueue("行情Websocket");
    }
  }

  public void subscribe(SubscribeRequest request) {
    if (request == null || subTopic.contains(request.getSymbol())) {
      return;
    }
    subTopic.add(request.getSymbol());
    offerRequest(request.deepClone());

    TickData tickData = new TickData();
    String symbol = request.getSymbol();
    tickData.setSymbol(request.getSymbol());
    String name = gateway.getSymbolNameMap().get(request.getSymbol());
    tickData.setName(StringUtils.isBlank(name) ? StringUtils.EMPTY : name);
    tickData.setExchange(ExchangeEnum.HUOBI);
    tickData.setDatetime(DateUtils.getCurrentTime());
    tickData.setGatewayName(gatewayName);

    tickDataMap.put(symbol, tickData);
    reqId++;
    JSONObject subDepthReq = new JSONObject();
    subDepthReq.put("sub", String.format("market.%s.depth.step1", symbol));
    subDepthReq.put("id", reqId);
    // sub depth
    send(subDepthReq.toJSONString());

    reqId++;
    JSONObject subMarketReq = new JSONObject();
    subMarketReq.put("sub", String.format("market.%s.detail", symbol));
    subMarketReq.put("id", reqId);
    // sub market
    send(subMarketReq.toJSONString());
  }

  @Override
  public void unSubscribe(SubscribeRequest request) {
    if (request == null || subTopic.contains(request.getSymbol())) {
      return;
    }
    subTopic.remove(request.getSymbol());
    reqId++;
    JSONObject subDepthReq = new JSONObject();
    String symbol = request.getSymbol();
    subDepthReq.put("unsub", String.format("market.%s.depth.step1", symbol));
    subDepthReq.put("id", reqId);
    // unsub depth
    send(subDepthReq.toJSONString());

    reqId++;
    JSONObject subMarketReq = new JSONObject();
    subMarketReq.put("unsub", String.format("market.%s.detail", symbol));
    subMarketReq.put("id", reqId);
    // unsub market
    send(subMarketReq.toJSONString());
  }

  @Override
  public void onData(JSONObject data) {
    if (data == null) {
      return;
    }
    if (data.toString().contains(SocketEnum.DEPTH.getName())) {
      onMarketDepth(data);
    } else if (data.toString().contains(SocketEnum.DETAIL.getName())) {
      onMarketDetail(data);
    } else if (data.toString().contains(SocketEnum.ERR_CODE.getName())) {
      onErrCode(data);
    }
  }

  private void onMarketDetail(JSONObject data) {
    // symbol, like usdt
    String symbol = data.get(SocketEnum.CHANNEL.getName()).toString().split("\\.")[1];
    TickData tickData = new TickData();
    if (tickDataMap.get(symbol) == null) {
      tickDataMap.put(symbol, tickData);
    } else {
      tickData = tickDataMap.get(symbol);
    }
    long datetime = Long.parseLong(data.get(SocketEnum.TS.getName()).toString()) / 1000;
    tickData.setDatetime(datetime);
    JSONObject tick = (JSONObject) data.get(SocketEnum.TICK.getName());

    tickData.setOpenPrice(tick.getFloat(SocketEnum.OPEN.getName()));
    tickData.setHighPrice(tick.getFloat(SocketEnum.HIGH.getName()));
    tickData.setLastPrice(tick.getFloat(SocketEnum.CLOSE.getName()));
    tickData.setLowPrice(tick.getFloat(SocketEnum.LOW.getName()));
    tickData.setVolume(tick.getFloat(SocketEnum.VOL.getName()));
    tickData.setGatewayName(gatewayName);

    tickDataMap.put(symbol, tickData);
    if (tickData.getBidPrice1() != 0) {
      TickData tickDataClone = tickData.deepClone();
      gateway.onTick(tickDataClone);
    }
  }

  private void onErrCode(JSONObject data) {
    String code = (String) data.get(SocketEnum.ERR_CODE.getName());
    String msg = (String) data.get(SocketEnum.ERR_MSG.getName());
    gateway.writeLog(String.format("错误代码%d, 错误信息%s", code, msg));
  }

  private void onBBO(JSONObject data) {

  }

  private void onMBP(JSONObject data) {
  }

  private void onMarketDepth(JSONObject data) {
    // symbol, like usdt
    String symbol = data.get(SocketEnum.CHANNEL.getName()).toString().split("\\.")[1];
    TickData tickData = new TickData();
    if (tickDataMap.get(symbol) == null) {
      tickDataMap.put(symbol, tickData);
    } else {
      tickData = tickDataMap.get(symbol);
    }
    long datetime = Long.parseLong(data.get(SocketEnum.TS.getName()).toString()) / 1000;
    tickData.setDatetime(datetime);
    JSONObject tick = (JSONObject) data.get(SocketEnum.TICK.getName());
    JSONArray asks = (JSONArray) tick.get(SocketEnum.ASKS.getName());
    int size = Math.min(asks.size(), 5);
    for (int i = 0; i < size; i++) {
      int index = i + 1;
      setValue(tickData, "askPrice" + index, asks.getJSONArray(i).getFloat(0));
      setValue(tickData, "askVolume" + index, asks.getJSONArray(i).getFloat(1));
    }

    JSONArray bids = (JSONArray) tick.get(SocketEnum.BIDS.getName());
    size = Math.min(bids.size(), 5);
    for (int i = 0; i < size; i++) {
      int index = i + 1;
      setValue(tickData, "bidPrice" + index, bids.getJSONArray(i).getFloat(0));
      setValue(tickData, "bidVolume" + index, bids.getJSONArray(i).getFloat(1));
    }
    tickDataMap.put(symbol, tickData);

    if (tickData.getLastPrice() != 0) {
      TickData tickDataClone = tickData.deepClone();
      gateway.onTick(tickDataClone);
    }
  }

  private void setValue(TickData tickData, String fieldName, float val) {
    try {
      Field field = tickData.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(tickData, val);
    } catch (Exception e) {
      logger.error("Cannot get field for the tick data:{}", fieldName);
    }
  }

  private void onKline(JSONObject data) {
  }

  @Override
  public void onReconnect() {
    try {
      if (subTopic == null) {
        return;
      }
      subTopic.clear();
      HuobiGateway huobiGateWay = (HuobiGateway) gateway;
      HuobiWebSocketDataApi reconnectSocketDataApi = new HuobiWebSocketDataApi(huobiGateWay);
      reconnectSocketDataApi.setSubscribeRequestQueue(subscribeRequestQueue);
      huobiGateWay.setHuobiWebSocketDataApi(reconnectSocketDataApi);
      new QuantWebSocketClient(reconnectSocketDataApi).start(true);
      String subject = ExchangeEnum.HUOBI.name() + "行情API重连";
      String content = "网络不稳定，websocket行情api尝试重连";
      sendEmail(subject, content);
    } catch (Exception e) {
      gateway.writeLog("市场行情重连失败");
    }
  }
}
