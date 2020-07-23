package com.quantworld.app.trader.engines;

import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.cep.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataEngine extends BaseEngine{

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private EventDispatcher eventDispatcher;

  @Autowired
  public DataEngine(EventDispatcher eventDispatcher) {
    this.eventDispatcher = eventDispatcher;
    init();
  }

  private void init() {
    registerEvent();
  }

  private void registerEvent() {
    eventDispatcher.subscribe(EventTypeEnum.EVENT_CONTRACT, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_ACCOUNT, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_ORDER, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_TRADE, this);
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ACCOUNT)
  public void processAccount(Event event) {
    if (isValidEvent(event)) {
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_ORDER)
  public void processOrder(Event event) {
    if (isValidEvent(event)) {
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TRADE)
  public void processTrade(Event event) {
    if (isValidEvent(event)) {
    }
  }
}
