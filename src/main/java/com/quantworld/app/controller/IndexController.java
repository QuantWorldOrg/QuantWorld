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

import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.comm.Const;
import com.quantworld.app.comm.aop.LoggerManage;
import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.domain.Config;
import com.quantworld.app.domain.User;
import com.quantworld.app.repository.ConfigRepository;
import com.quantworld.app.service.StrategyBasicParamGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

  @Autowired
  ConfigRepository configRepository;

  @Autowired
  private StrategyBasicParamGenerator strategyBasicParamGenerator;

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    return "login";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home(Model model) {
    List<String> followList = new ArrayList<>();
    Config config = configRepository.findByUserId(getUserId());
    model.addAttribute("config", config);
    model.addAttribute("followList", followList);
    model.addAttribute("user", getUser());
    model.addAttribute("basicStrategyParamJson", new JSONObject());
    model.addAttribute("propertyJson", new JSONObject());
    logger.info("standard userID=" + getUserId());
    return "home";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String register() {
    return "register";
  }

  @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
  public String forgotPassword() {
    return "user/forgotPassword";
  }

  @RequestMapping(value = "/viewchart", method = RequestMethod.GET)
  public String viewChart() {
    return "view/viewchart";
  }

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  @LoggerManage(description = "首页")
  public String index(Model model) {
    model.addAttribute("collector", "");
    User user = super.getUser();
    if (null != user) {
      model.addAttribute("user", user);
    }
    return "index";
  }

  @RequestMapping(value = "/uploadHeadPortrait")
  @LoggerManage(description = "上传你头像页面")
  public String uploadHeadPortrait() {
    return "user/uploadHeadPortrait";
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  @LoggerManage(description = "登出")
  public String logout(HttpServletResponse response, Model model) {
    getSession().removeAttribute(Const.LOGIN_SESSION_KEY);
    getSession().removeAttribute(Const.LAST_REFERER);
    Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, "");
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
    return "index";
  }

  @RequestMapping(value = "/tool")
  @LoggerManage(description = "工具页面")
  public String tool(Model model) {
    return "tool";
  }

  @RequestMapping(value = "/newStrategyTemplate")
  @LoggerManage(description = "新建策略")
  public String addNewStrategyTemplate(Model model) {
    Map<String, String> strategyTemplateLabelMap = new ConcurrentHashMap<>();
    for (StrategyTemplateEnum template : StrategyTemplateEnum.values()) {
      strategyTemplateLabelMap.put(template.toString(), template.getDisplayLabel());
    }
    model.addAttribute("strategyTemplateLabelMap", strategyTemplateLabelMap);
    return "/strategy/newStrategyTemplate";
  }
}
