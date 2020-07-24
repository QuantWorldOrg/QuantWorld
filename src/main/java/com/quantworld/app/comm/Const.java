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
package com.quantworld.app.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Const {

  public static String BASE_PATH;

  public static String LOGIN_SESSION_KEY = "Quant_user";

  public static String PASSWORD_KEY = "@#$%^&*()OPG#$%^&*(HG";

  public static String DES3_KEY = "9964DYByKL967c3308imytCB";

  public static String default_logo = "img/logo.jpg";

  public static String userAgent = "Mozilla";

  public static String default_Profile = BASE_PATH + "/img/logo.jpg";

  public static String LAST_REFERER = "LAST_REFERER";

  public static int COOKIE_TIMEOUT = 30 * 24 * 60 * 60;


  @Autowired(required = true)
  public void setBasePath(@Value("${quant.base.path}") String basePath) {
    Const.BASE_PATH = basePath;
  }
}
