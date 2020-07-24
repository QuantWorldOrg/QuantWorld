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
package com.quantworld.app.service;

import com.quantworld.app.trader.engines.BaseEngine;

/**
 * @author: Shawn
 * @Date: 11/6/2019
 * @Description:
 */
public abstract class BaseApp {
  private String appName = "";
  private String name = "";
  private BaseEngine baseEngine = null;
  private String widgetName = "";

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BaseEngine getBaseEngine() {
    return baseEngine;
  }

  public void setBaseEngine(BaseEngine baseEngine) {
    this.baseEngine = baseEngine;
  }

  public String getWidgetName() {
    return widgetName;
  }

  public void setWidgetName(String widgetName) {
    this.widgetName = widgetName;
  }
}
