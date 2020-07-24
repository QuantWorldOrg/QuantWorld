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
package com.quantworld.app.broker.gateway.huobi.sdk;

import com.quantworld.app.broker.exception.BrokerApiException;
import com.quantworld.app.broker.gateway.huobi.sdk.util.UrlParamsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ApiSignature {

  public static final String op = "op";
  public static final String opValue = "auth";
  private static final String accessKeyId = "AccessKeyId";
  private static final String signatureMethod = "SignatureMethod";
  private static final String signatureMethodValue = "HmacSHA256";
  private static final String signatureVersion = "SignatureVersion";
  public static final String signatureVersionValue = "2";
  private static final String timestamp = "Timestamp";
  private static final String signature = "Signature";

  private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter
      .ofPattern("uuuu-MM-dd'T'HH:mm:ss");
  private static final ZoneId ZONE_GMT = ZoneId.of("Z");


  public static UrlParamsBuilder createSignature(String accessKey, String secretKey, String method, String host,
                       String address) {
    UrlParamsBuilder builder = UrlParamsBuilder.build();

    StringBuilder sb = new StringBuilder(1024);

    if (accessKey == null || "".equals(accessKey) || secretKey == null || "".equals(secretKey)) {
      throw new BrokerApiException(BrokerApiException.KEY_MISSING,
          "API key and secret key are required");
    }

    sb.append(method.toUpperCase()).append('\n')
        .append(host.toLowerCase()).append('\n')
        .append(address).append('\n');

    builder.putToUrl(accessKeyId, accessKey)
        .putToUrl(signatureVersion, signatureVersionValue)
        .putToUrl(signatureMethod, signatureMethodValue)
        .putToUrl(timestamp, gmtNow());

    sb.append(builder.buildSignature());
    Mac hmacSha256;
    try {
      hmacSha256 = Mac.getInstance(signatureMethodValue);
      SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
          signatureMethodValue);
      hmacSha256.init(secKey);
    } catch (NoSuchAlgorithmException e) {
      throw new BrokerApiException(BrokerApiException.RUNTIME_ERROR,
          "[Signature] No such algorithm: " + e.getMessage());
    } catch (InvalidKeyException e) {
      throw new BrokerApiException(BrokerApiException.RUNTIME_ERROR,
          "[Signature] Invalid key: " + e.getMessage());
    }
    String payload = sb.toString();
    byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));

    String actualSign = Base64.getEncoder().encodeToString(hash);

    builder.putToUrl(signature, actualSign);
    return builder;
  }

  private static long epochNow() {
    return Instant.now().getEpochSecond();
  }

  static String gmtNow() {
    return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
  }
}
