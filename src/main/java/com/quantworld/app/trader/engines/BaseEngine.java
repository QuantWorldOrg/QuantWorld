package com.quantworld.app.trader.engines;

import com.quantworld.app.trader.cep.Event;

/**
 * @author: Shawn
 * @Date: 11/6/2019
 * @Description:
 */

public abstract class BaseEngine {
  private String engineName;

  public String getEngineName() {
    return engineName;
  }

  public void setEngineName(String engineName) {
    this.engineName = engineName;
  }

  public void close() {}

  protected boolean isValidEvent(Event event) {
    return event != null && event.getData() != null;
  }
}
