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
