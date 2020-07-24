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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtil {

  public static Logger logger = LoggerFactory.getLogger(HtmlUtil.class);

  public static synchronized boolean isConnect(String urlStr) {
    int counts = 0;
    if (urlStr == null || urlStr.length() <= 0) {
      return false;
    }
    while (counts < 3) {
      try {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int state = con.getResponseCode();
        if (state == 200) {
          return true;
        }
        break;
      } catch (Exception ex) {
        counts++;
        continue;
      }
    }
    return false;
  }

  public static String getDomainUrl(String urlStr) {
    String domainUrl = urlStr;
    try {
      URL url = new URL(urlStr);
      domainUrl = url.getProtocol() + "://" + url.getAuthority();
    } catch (Exception e) {
      // TODO: handle exception
      logger.error("getDomainUrl is erro,url :" + urlStr, e);
    }
    return domainUrl;
  }


  public static String getHost(String urlStr) {
    String host = urlStr;
    try {
      URL url = new URL(urlStr);
      host = url.getHost();
    } catch (Exception e) {
      // TODO: handle exception
      logger.error("getHost is erro,url :" + urlStr, e);
    }
    return host;
  }


  public static void main(String[] args) {
    System.out.println(getDomainUrl("http://common.cnblogs.com/favicon.ico"));
  }

}
