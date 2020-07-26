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
