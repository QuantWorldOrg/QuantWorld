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
package com.quantworld.app.trader.engines;

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.data.constants.EventTypeEnum;
import com.quantworld.app.service.MailService;
import com.quantworld.app.trader.cep.Event;
import com.quantworld.app.trader.cep.EventDispatcher;
import com.quantworld.app.trader.cep.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author: Shawn
 * @Date: 11/7/2019
 * @Description:
 */
@Component
public class EmailEngine extends BaseEngine {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private EventDispatcher eventDispatcher;

  private MailService mailService;

  @Autowired
  public EmailEngine(EventDispatcher eventDispatcher, MailService mailService) {
    this.eventDispatcher = eventDispatcher;
    this.mailService = mailService;
    init();
  }

  private void init() {
    registerEvent();
  }

  private void registerEvent() {
    eventDispatcher.subscribe(EventTypeEnum.EVENT_EMAIL, this);
  }

  @OnEvent(eventType = EventTypeEnum.EVENT_EMAIL)
  public void processEmail(Event event) {
    if (isValidEvent(event)) {
      JSONObject emailData = (JSONObject) event.getData();
      String subject = emailData.getString("subject");
      String content = emailData.getString("content");
      CompletableFuture.runAsync(() -> mailService.sendSimpleTextMailActual(subject, content, new String[]{"quantworld@yeah.net"}, null, null, null));
    }
  }
}
