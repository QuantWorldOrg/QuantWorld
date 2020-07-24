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
 * @Description:
 */
public enum ProductEnum {
  NONE("NONE"),
  EQUITY("股票"),
  FUTURES("期货"),
  OPTION("期权"),
  INDEX("指数"),
  FOREX("外汇"),
  SPOT("现货"),
  ETF("ETF"),
  BOND("债券"),
  WARRANT("权证"),
  SPREAD("价差"),
  FUND("基金");

  String value;

  ProductEnum(String value) {
    this.value = value;
  }
}
