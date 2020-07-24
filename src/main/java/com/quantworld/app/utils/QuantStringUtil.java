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
package com.quantworld.app.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuantStringUtil {
  private static Logger logger = LoggerFactory.getLogger(QuantStringUtil.class);

  public static List<String> getAtUser(String str) {
    Pattern p = Pattern.compile("(?<=@).*?(?= )");
    Matcher m = p.matcher(str);
    List<String> result = new ArrayList<String>();
    while (m.find()) {
      if (StringUtils.isNoneBlank(m.group().trim())) {
        result.add(m.group().trim());
      }
    }
    return result;
  }

  public static String getSubUtilSimple(String soap, String rgex, int index) {
    Pattern pattern = Pattern.compile(rgex);
    Matcher m = pattern.matcher(soap);
    while (m.find()) {
      return m.group(index);
    }
    return "";
  }

  public static JSONObject convertURLParamToJsonFormat(String originalString) {
    JSONObject convertedJsonObject = new JSONObject();
    try {
      String[] stringArray = URLDecoder.decode(originalString, "utf-8").split("&");
      for (String s : stringArray) {
        String[] keyAndValue = s.split("=");
        if (!keyAndValue[0].contains("[") && !keyAndValue[0].contains("]")) {
          convertedJsonObject.put(keyAndValue[0], keyAndValue[1]);
        } else {
          String subKey = getSubUtilSimple(keyAndValue[0], "\\[(.*)\\]", 1);
          String key = getSubUtilSimple(keyAndValue[0], "(.*)\\[", 1);
          if (convertedJsonObject.get(key) == null) {
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put(subKey, keyAndValue[1]);
            convertedJsonObject.put(key, subJsonObject);
          } else {
            JSONObject subJsonObject = (JSONObject) convertedJsonObject.get(key);
            subJsonObject.put(subKey, keyAndValue[1]);
            convertedJsonObject.put(key, subJsonObject);
          }
        }
      }
    } catch (UnsupportedEncodingException e) {
      logger.error("Can not convert URL param to json format", e);
    }
    return convertedJsonObject;
  }

  private static String getNewLocatedId(List<String> idList, String locatedId) {
    // 列表遍历计数
    int count = 0;
    for (String id : idList) {
      if (id.equals(locatedId)) {
        // 找到id并记下是索引号
        int index = count;
        // 特殊情况处理, 遇到尾部元素, 需要取上一个元素
        // 常用情况处理, 取下一个元素
        index = (index == idList.size() - 1) ? index - 1 : index + 1;
        // index是-1的情况,必然是只有一个元素并且被删除, 所以返回空字符串.
        return (index != -1) ? idList.get(index) : "";
      }
      count++;
    }
    // 找不到，返回空字符串
    return "";
  }

  public static boolean isNumeric(String str) {
    return str.matches("-?[0-9]+.*[0-9]*");
  }

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
