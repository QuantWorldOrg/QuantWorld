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


public enum OrderTypeEnum {

  NONE("NONE"),
  LIMIT("限价"),
  MARKET("市价"),
  STOP("STOP"),
  FAK("FAK"),
  FOK("FOK"),
  BLP("BLP"),
  VWAP("VWAP"),
  TWAP("TWAP"),
  SOR("SOR");
  String value;

  OrderTypeEnum(String value) {
    this.value = value;
  }
}
