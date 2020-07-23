package com.quantworld.app.trader.cep;

import com.quantworld.app.data.constants.EventTypeEnum;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Subscriber {

  private Lock lock = new ReentrantLock();

  @OnEvent(eventType = EventTypeEnum.EVENT_TIMER)
  private void getTimer(Event event) {
    System.out.println(Thread.currentThread() + "==" + System.currentTimeMillis() +  " -- Current time:" + event.getData());
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_NONE)
  private void processTest(Event event) throws InterruptedException {
    lock.lock();
    long start = System.currentTimeMillis();
    System.out.println(Thread.currentThread() + "==" + System.currentTimeMillis() + " : "  + event.getData());
    System.out.println(Thread.currentThread() + ": End");
    TimeUnit.SECONDS.sleep(1L);
    lock.unlock();
  }
}