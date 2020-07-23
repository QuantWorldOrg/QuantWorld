package com.quantworld.app.trader.cep;

import com.quantworld.app.data.constants.EventTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class EventDispatcherTest {

  @Autowired
  private EventDispatcher eventDispatcher;

  @Test
  public void testEventDispatcher() throws InterruptedException {
    Subscriber subscriber = new Subscriber();
    EventDispatcher eventDispatcher = new EventDispatcher();
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TIMER, subscriber);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_NONE, subscriber);
    eventDispatcher.start();
    for (int i = 0; i < 10000000; i++) {
      TimeUnit.MILLISECONDS.sleep(10);
      eventDispatcher.putEvent(new Event(EventTypeEnum.EVENT_NONE, "Block code test"));
    }
  }
}