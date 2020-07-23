package com.quantworld.app.broker.gateway;

import com.quantworld.app.utils.ProxyUtil;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * @author: Shawn
 * @Date: 3/5/2020
 * @Description:
 */
public class WebsocketCreationUtil implements Runnable {

  private Logger logger = LoggerFactory.getLogger(WebsocketCreationUtil.class);

  private WebSocketClient webSocketClient;
  private boolean doesReconnect;

  WebsocketCreationUtil(WebSocketClient webSocketClient, boolean doesReconnect) {
    this.webSocketClient = webSocketClient;
    this.doesReconnect = doesReconnect;
  }

  @Override
  public void run() {

    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
      }

      @Override
      public void checkClientTrusted(X509Certificate[] chain,
                                     String authType) {
      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain,
                                     String authType) {
      }
    }};

    SSLContext sc = null;
    try {
      sc = SSLContext.getInstance("TLS");
      //创建WebSocket工厂
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      // webSocketClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
      if (doesReconnect) {
        logger.info("********************* START RECONNECTING *********************");
      } else {
        logger.info("******************** START CONNECTING ********************");
      }
//      webSocketClient.setSocket(sc.getSocketFactory().createSocket());
      webSocketClient.connect();
    } catch (Exception e) {
      logger.warn("WebSocket重连失败");
    }
  }
}
