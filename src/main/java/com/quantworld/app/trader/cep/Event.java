package com.quantworld.app.trader.cep;

import com.quantworld.app.data.constants.EventTypeEnum;

/**
 * @author: Shawn
 * @Date: 10/24/2019
 * @Description:
 */
public class Event {

  private EventTypeEnum type;

  private Object data;

  public Event(EventTypeEnum type, Object data) {
    this.type = type;
    this.data = data;
  }
  public Event(EventTypeEnum type) {
    this.type = type;
  }

  public EventTypeEnum getType() {
    return type;
  }


  public Object getData() {
    return data;
  }
}
