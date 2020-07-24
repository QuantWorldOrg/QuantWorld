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
package com.quantworld.app.broker.gateway.huobi.sdk.util;


import com.quantworld.app.broker.exception.BrokerApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

public abstract class InternalUtils {

  public static byte[] decode(byte[] data) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    decompress(bais, baos);
    baos.flush();
    baos.close();
    bais.close();
    return baos.toByteArray();
  }

  private static void decompress(InputStream is, OutputStream os) throws IOException {
    GZIPInputStream gis = new GZIPInputStream(is);
    int count;
    byte[] data = new byte[1024];
    while ((count = gis.read(data, 0, 1024)) != -1) {
      os.write(data, 0, count);
    }
    gis.close();
  }

  public static void await(long n) throws BrokerApiException {
    try {
      Thread.sleep(n);
    } catch (InterruptedException e) {
      throw new BrokerApiException(BrokerApiException.SYS_ERROR, "Error when sleep", e);
    }
  }
}
