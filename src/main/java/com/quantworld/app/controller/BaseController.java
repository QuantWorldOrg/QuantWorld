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
package com.quantworld.app.controller;


import com.quantworld.app.comm.Const;
import com.quantworld.app.domain.User;
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.utils.Des3EncryptionUtil;
import com.quantworld.app.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected Response result(ExceptionMsg msg) {
    return new Response(msg);
  }

  protected Response result() {
    return new Response();
  }

  protected HttpServletRequest getRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }

  protected HttpSession getSession() {
    return getRequest().getSession();
  }

  protected User getUser() {
    return (User) getSession().getAttribute(Const.LOGIN_SESSION_KEY);
  }

  protected String getUserId() {
    String id = "";
    User user = getUser();
    if (user != null) {
      id = user.getId();
    }
    return id;
  }

  protected String getUserName() {
    String userName = "宽界";
    User user = getUser();
    if (user != null) {
      userName = user.getUserName();
    }
    return userName;
  }

  protected String getUserIp() {
    String value = getRequest().getHeader("X-Real-IP");
    if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
      return value;
    } else {
      return getRequest().getRemoteAddr();
    }
  }

  protected String getPwd(String password) {
    try {
      String pwd = MD5Util.encrypt(password + Const.PASSWORD_KEY);
      return pwd;
    } catch (Exception e) {
      logger.error("密码加密异常：", e);
    }
    return null;
  }

  protected String cookieSign(String value) {
    try {
      value = value + Const.PASSWORD_KEY;
      String sign = Des3EncryptionUtil.encode(Const.DES3_KEY, value);
      return sign;
    } catch (Exception e) {
      logger.error("cookie签名异常：", e);
    }
    return null;
  }
}
