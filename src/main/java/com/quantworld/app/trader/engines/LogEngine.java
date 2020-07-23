package com.quantworld.app.trader.engines;

import com.quantworld.app.data.LogData;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.cep.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Shawn
 * @Date: 11/6/2019
 * @Description:
 */
@Component
public class LogEngine extends BaseEngine {


  private final Logger logger = LoggerFactory.getLogger(getClass());

  private EventDispatcher eventDispatcher;

  @Autowired
  public LogEngine(EventDispatcher eventDispatcher) {
    this.eventDispatcher = eventDispatcher;
    init();
  }

  private void init() {
    registerEvent();
  }
  private void registerEvent() {
    eventDispatcher.subscribe(EventTypeEnum.EVENT_LOG, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_STRATEGY_LOG, this);
    eventDispatcher.subscribe(EventTypeEnum.EVENT_STRATEGY_SETTING_UPDATED, this);
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_LOG)
  public void processLog(Event event) {
    if (isValidEvent(event))  {
      logger.info(((LogData)event.getData()).getMsg());
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_STRATEGY_LOG)
  public void processStrategyLog(Event event) {
    if (isValidEvent(event)) {
      logger.info((String) event.getData());
    }
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_STRATEGY_SETTING_UPDATED)
  public void processStrategySaveLog(Event event) {
  }

  @Override
  public void close() {

  }
}
