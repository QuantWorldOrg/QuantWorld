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
package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description: Offset of order/trade.
 */
public enum OffsetEnum {
  NONE("NONE"),
  OPEN("开"),
  CLOSE("平"),
  CLOSETODAY("平今"),
  CLOSEYESTERDAY("平昨");
  String value;

  OffsetEnum(String value) {
    this.value = value;
  }
}
