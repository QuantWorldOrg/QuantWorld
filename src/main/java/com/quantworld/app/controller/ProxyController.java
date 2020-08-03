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

import com.alibaba.fastjson.JSON;
import com.quantworld.app.domain.Exchange;
import com.quantworld.app.domain.ProxyConfiguration;
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.domain.result.ResponseData;
import com.quantworld.app.repository.ExchangeRepository;
import com.quantworld.app.repository.ProxyRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/proxy")
public class ProxyController extends BaseController {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ProxyRepository proxyRepository;

  @RequestMapping(value = "/connect", method = RequestMethod.POST)
  public Response connectProxy(@RequestBody String data) throws ClassNotFoundException {

    ProxyConfiguration proxyConfiguration = (ProxyConfiguration) JSON.toJavaObject(JSON.parseObject(data), Class.forName(ProxyConfiguration.class.getName()));
    proxyRepository.save(proxyConfiguration);
    return new ResponseData(ExceptionMsg.SUCCESS);
  }

  @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
  public Response disconnectProxy(@RequestBody String data) throws ClassNotFoundException {

    ProxyConfiguration proxyConfiguration = (ProxyConfiguration) JSON.toJavaObject(JSON.parseObject(data), Class.forName(ProxyConfiguration.class.getName()));
    proxyConfiguration.setStatus(false);
    proxyRepository.save(proxyConfiguration);
    return new ResponseData(ExceptionMsg.SUCCESS);
  }
}
