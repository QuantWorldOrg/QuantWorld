package com.quantworld.app.service.impl;

import com.quantworld.app.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;

/**
 * Created by Shawn
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceImplTest {

  @Autowired
  private MailService mailService;

  @Autowired
  private TemplateEngine templateEngine;

  @Test
  public void testSimpleMail() throws Exception {
    mailService.sendSimpleTextMailActual("你好，肖峰", "我是宽界", new String[]{"shawnpeng@yeah.net"}, null, null, null);
  }
}
