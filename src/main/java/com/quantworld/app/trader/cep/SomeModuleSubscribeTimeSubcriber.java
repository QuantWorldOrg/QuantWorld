package com.quantworld.app.trader.cep;

import com.quantworld.app.data.constants.EventTypeEnum;

/**
 * @author: Shawn
 * @Date: 10/25/2019
 * @Description:
 */
public class SomeModuleSubscribeTimeSubcriber {
  int id;

  public SomeModuleSubscribeTimeSubcriber(int id) {
    this.id = id;
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TIMER)
  private void onTimerEvent(Event event) {
    System.out.println(id++ + ": date: " + event.getData());
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ACCOUNT)
  private void onAccountEvent(Event event) {
    System.out.println(id++ + ": date: " + event.getData());
  }
}
