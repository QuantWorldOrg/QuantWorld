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
package com.quantworld.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

  private final static long minute = 60 * 1000;// 1分钟
  private final static long hour = 60 * minute;// 1小时
  private final static long day = 24 * hour;// 1天
  private final static long month = 31 * day;// 月
  private final static long year = 12 * month;// 年

  public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmssSSS";
  public final static String YYYY_MM_DD = "yyyy-MM-dd";
  public final static String ASIA_SHANGHAI = "Asia/Shanghai";

  /**
   * @return
   * @author neo
   * @date 2015-5-21
   */
  public static String getDateSequence() {
    return new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date());
  }


  /**
   * @return
   * @author neo
   * @date 2016年8月10日
   */
  public static Long getCurrentTime() {
    return System.currentTimeMillis();
  }


  public static String getTimeFormatText(Long date) {
    if (date == null) {
      return null;
    }
    long diff = new Date().getTime() - date;
    long r = 0;
    if (diff > year) {
      r = (diff / year);
      return r + "年前";
    }
    if (diff > month) {
      r = (diff / month);
      return r + "个月前";
    }
    if (diff > day) {
      r = (diff / day);
      return r + "天前";
    }
    if (diff > hour) {
      r = (diff / hour);
      return r + "个小时前";
    }
    if (diff > minute) {
      r = (diff / minute);
      return r + "分钟前";
    }
    return "刚刚";
  }

  /**
   * 将时间戳转换成当天0点
   *
   * @param timestamp
   * @return
   */
  public static long getDayBegin(long timestamp) {
    String pattern = YYYY_MM_DD;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ASIA_SHANGHAI));
    String toDayString = simpleDateFormat.format(new Date(timestamp));
    Date toDay = null;
    try {
      toDay = org.apache.commons.lang3.time.DateUtils.parseDate(toDayString, new String[]{pattern});

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return toDay.getTime();
  }

  /**
   * 获取一个月之前的时间戳
   *
   * @return
   */
  public static long getLastMonthTime() {
    return getDayBegin(getCurrentTime()) - 86400000l * 30;
  }

  public static Date randomDate(String beginDate, String endDate) {
    try {
      SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
      format.setTimeZone(TimeZone.getTimeZone(ASIA_SHANGHAI));
      Date start = format.parse(beginDate);
      Date end = format.parse(endDate);

      if (start.getTime() >= end.getTime()) {
        return null;
      }
      long date = random(start.getTime(), end.getTime());
      return new Date(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static long random(long begin, long end) {
    long rtn = begin + (long) (Math.random() * (end - begin));
    if (rtn == begin || rtn == end) {
      return random(begin, end);
    }
    return rtn;
  }

  public static Date parse(String pattern, String dateString, String timeZone) {
    Date targetDate;
    try {
      SimpleDateFormat format = new SimpleDateFormat(pattern);
      format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
      targetDate = format.parse(dateString);
    } catch (Exception e) {
      return new Date();
    }

    return targetDate;
  }

}
