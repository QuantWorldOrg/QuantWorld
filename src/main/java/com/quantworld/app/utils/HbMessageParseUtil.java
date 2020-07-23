package com.quantworld.app.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class HbMessageParseUtil {

  public static Double getClosePrice(String data) {
    if (data != null && data.contains("close")) {
      JSONObject jsonData = JSONObject.parseObject(data);
      return jsonData.getJSONObject("tick").getDouble("close");
    }
    return Double.MIN_VALUE;
  }

  public static String getCurrencyKey(String data) {
    if (data != null && data.contains("ch")) {
      JSONObject jsonData = JSONObject.parseObject(data);
      return jsonData.getString("ch");
    }
    return StringUtils.EMPTY;
  }
}
