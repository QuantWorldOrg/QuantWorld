package com.quantworld.app.trader.cep;

import com.quantworld.app.data.constants.EventTypeEnum;

/**
 * @author: Shawn
 * @Date: 10/25/2019
 * @Description:
 */
public class BlockedTestSubcriber {
  int id;

  public BlockedTestSubcriber(int id) {
    this.id = id;
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_TIMER)
  private void onEvent(Event event) {
    int count = 0;
    System.out.println("Blocked date:" + event.getData());
/*    while(true) {
      if(count++ == 50) break;
      try {
        System.out.println("Blocked");
        System.out.println(Thread.currentThread());
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }*/
  }
}
