package com.quantworld.app.broker.gateway.huobi;

import com.quantworld.app.data.constants.StatusEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 3/2/2020
 * @Description:
 */
public class HuobiContants {

  public static final String ACCESS_KEY = "";
  public static final String SECRET_KEY = "";

  public static final String SIGN_HOST = "api.huobi.pro";
  public static final String SIGN_AWS_HOST = "api-aws.huobi.pro";

  public static final String WSS_PROTOCOL = "wss://";
  public static final String HTTPS_PROTOCOL = "https://";

  public static final String PORT = "443";
  public static final String AO_PATH = "/ws/v1";
  public static final String AO_PATH_V2 = "/ws/v2";
  public static final String MARKET_PATH = "/ws";
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final short SUCCESS_CODE = 101;

  public static final Map<String, StatusEnum> HUOBI_ORDER_STATUS = new ConcurrentHashMap<String, StatusEnum>() {
    {
      put("submitted", StatusEnum.NOTTRADED);
      put("partial-filled", StatusEnum.PARTTRADED);
      put("filled", StatusEnum.ALLTRADED);
      put("cancelling", StatusEnum.CANCELLING);
      put("partial-canceled", StatusEnum.PARTCANCELED);
      put("canceled", StatusEnum.CANCELED);
    }
  };


  /**
   * |order-state|Description
   * |    -1     |order was already closed in the long past (order state = canceled, partial-canceled, filled, partial-filled)
   * |     5     |partial-canceled
   * |     6     |filled
   * |     7     |canceled
   * |    10     |cancelling
   */
  public static final Map<String, StatusEnum> HUOBI_ORDER_STATE = new HashMap<String, StatusEnum>() {
    {
      put("null", StatusEnum.NONE);
      put("-1", StatusEnum.NONE);
      put("5", StatusEnum.PARTCANCELED);
      put("6", StatusEnum.ALLTRADED);
      put("7", StatusEnum.CANCELED);
      put("10", StatusEnum.CANCELLING);
    }
  };
}
