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
package com.quantworld.app.trader.cep;


import com.quantworld.app.data.constants.EventTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: Shawn
 * @Date: 10/24/2019
 * @Description:
 */
@Service
public class EventDispatcher {

  public static final String NAME = "eventDispatcher";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private volatile boolean isActive;
  private Thread timer;
  private Queue<Event> eventQueue;
  private Thread run;
  // 1 millisecond
  private long interval = 1L;
  private Map<EventTypeEnum, ConcurrentHashMap<Integer, Object>> events;
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  public EventDispatcher() {
    this.init();
  }

  public EventDispatcher(long interval) {
    this.interval = interval;
    this.init();
  }

  private void init() {
    eventQueue = new ConcurrentLinkedQueue<>();
    events = new ConcurrentHashMap<>();
    timer = new Thread(() -> {
      while (isActive) {
        try {
          TimeUnit.MILLISECONDS.sleep(interval);
          publishTimer();
        } catch (Exception e) {
          logger.error("Failed to generate timer event", e);
        }
      }
    });
    logger.warn("The timer thread id is {}", timer.getId());
    run = new Thread(() -> {
      while (isActive) {
        try {
          if (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            publish(event);
          }
        } catch (Exception e) {
          logger.error("Failed to publish event", e);
        }
      }
    });
  }

  public void start() {
    this.isActive = true;
    this.run.start();
    this.timer.start();
  }

  public void stop() {
    this.isActive = false;
    try {
      this.run.join();
      this.timer.join();
    } catch (Exception e) {
      logger.error("Failed to stop Event engine and timer event", e);
    }
  }

  public void close() {
    stop();
  }

  public void subscribe(EventTypeEnum eventType, Object subscriber) {
    if (!events.containsKey(eventType)) {
      events.put(eventType, new ConcurrentHashMap<>());
    }
    // Add
    events.get(eventType).put(subscriber.hashCode(), subscriber);
  }

  /**
   * Unsubcribe eventType
   *
   * @param eventType
   * @param subscriber
   * @return: true: subscriber has unscribe the eventType sunccessfully.
   * false: there is no this eventType in the events, no need to unscribe.
   */
  public boolean unsubscribe(EventTypeEnum eventType, Object subscriber) {
    if (events.containsKey(eventType)) {
      events.get(eventType).remove(subscriber.hashCode());
      return true;
    }
    return false;
  }

  /**
   * Publish the events except the timer event
   *
   * @param event
   */
  private void publish(Event event) {
    if (event != null && events.containsKey(event.getType())) {
      for (Map.Entry<Integer, Object> subs : events.get(event.getType()).entrySet()) {
        Object subscriberObj = subs.getValue();
        if (subscriberObj != null && subscriberObj.getClass() != null) {
          for (final Method method : subscriberObj.getClass().getDeclaredMethods()) {
            OnEvent annotation = method.getAnnotation(OnEvent.class);
            if (annotation != null && annotation.eventType().equals(event.getType())) {
              executorService.execute(() -> deliverEvent(subscriberObj, method, event));
            }
          }
        }
      }
    }
  }

  /**
   * Only publish the timer event.
   * Why need a new method to publish the timer event?
   * If use the "publish" method, the system will create several threads to publish event asynchronously.
   * Hence, it's needed to lock one CPU and one thread to execute the timer event publishing.
   */
  private void publishTimer() {
    if (events.containsKey(EventTypeEnum.EVENT_TIMER)) {
      for (Map.Entry<Integer, Object> subs : events.get(EventTypeEnum.EVENT_TIMER).entrySet()) {
        Object subscriberObj = subs.getValue();
        if (subscriberObj != null && subscriberObj.getClass() != null) {
          for (final Method method : subscriberObj.getClass().getDeclaredMethods()) {
            OnEvent annotation = method.getAnnotation(OnEvent.class);
            if (annotation != null && annotation.eventType().equals(EventTypeEnum.EVENT_TIMER)) {
              deliverEvent(subscriberObj, method, new Event(EventTypeEnum.EVENT_TIMER, new Date()));
            }
          }
        }
      }
    }
  }

  public void putEvent(Event event) {
    if (event != null && event.getData() != null) {
      eventQueue.offer(event);
    }
  }

  private <T> void deliverEvent(T subscriber, Method method, Event event) {
    try {
      boolean methodFound = false;
      for (final Class paramClass : method.getParameterTypes()) {
        if (paramClass.equals(event.getClass())) {
          methodFound = true;
          break;
        }
      }
      if (methodFound) {
        method.setAccessible(true);
        if (subscriber != null) {
          method.invoke(subscriber, event);
        }
      }

    } catch (Exception e) {
      logger.error("Deliver message has failed", e);
    }
  }
}
