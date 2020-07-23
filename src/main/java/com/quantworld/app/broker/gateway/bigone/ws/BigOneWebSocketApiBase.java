package com.quantworld.app.broker.gateway.bigone.ws;

import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.data.SubscribeRequest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BigOneWebSocketApiBase extends WebSocketClient {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  protected BaseGateway gateway;

  protected String gatewayName;

  protected Queue<SubscribeRequest> subscribeRequestQueue = new ConcurrentLinkedQueue<>();

  public BigOneWebSocketApiBase(String uri, BaseGateway gateway) throws URISyntaxException {
    super(new URI(uri), new Draft_6455());
    this.gateway = gateway;
    this.gatewayName = gateway.getGatewayName();
  }

  @Override
  public void onOpen(ServerHandshake serverHandshake) {

  }

  @Override
  public void onMessage(String s) {

  }

  @Override
  public void onClose(int i, String s, boolean b) {

  }

  @Override
  public void onError(Exception e) {

  }
}
