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
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.domain.result.ResponseData;
import com.quantworld.app.repository.ExchangeRepository;
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
@RequestMapping("/exchange")
public class ExchangeController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ExchangeRepository exchangeRepository;

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public Response addNewExchangeInfo(@RequestBody String data) throws ClassNotFoundException {

    Exchange exchange = (Exchange) JSON.toJavaObject(JSON.parseObject(data), Class.forName(Exchange.class.getName()));
    exchange.setUserName(getUserName());
    exchange.setCreateTime(new Timestamp(System.currentTimeMillis()));
    exchange.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
    if (StringUtils.isBlank(exchange.getAccessKey())) {
      return new ResponseData(ExceptionMsg.FAILED, "Access Key不可为空！");
    } else if (StringUtils.isBlank(exchange.getSecretKey())) {
      return new ResponseData(ExceptionMsg.FAILED, "Secret Key不可为空！");
    } else if (StringUtils.isBlank(exchange.getExchange())) {
      return new ResponseData(ExceptionMsg.FAILED, "交易所名不可为空！");
    } else if (StringUtils.isBlank(exchange.getType())) {
      return new ResponseData(ExceptionMsg.FAILED, "交易类型不可为空！");
    }
    Exchange save = exchangeRepository.save(exchange);
    logger.info("Exchange save: {}", save.toString());
    return new ResponseData(ExceptionMsg.SUCCESS);
  }
}
