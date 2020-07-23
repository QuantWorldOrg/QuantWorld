package com.quantworld.app.utils;

public class MessageUtil {

  public static String getMessage(String template, String... keys) {
    int count = 0;
    for (String key : keys) {
      template = template.replace("{" + count++ + "}", key);
    }
    return template;
  }

}
