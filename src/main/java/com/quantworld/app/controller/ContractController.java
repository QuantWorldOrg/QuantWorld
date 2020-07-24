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
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.GatewayFactory;
import com.quantworld.app.broker.gateway.huobi.sdk.util.InternalUtils;
import com.quantworld.app.comm.aop.LoggerManage;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.domain.result.ResponseData;
import com.quantworld.app.trader.engines.AdminEngine;
import com.quantworld.app.trader.oms.OrderManagementSystem;
import com.quantworld.app.constants.QuantConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/contract")
public class ContractController {

  @Autowired
  private OrderManagementSystem orderManagementSystem;

  @Autowired
  private AdminEngine adminEngine;

  @RequestMapping(value = "/getContractInfo/{exchangeName}", method = RequestMethod.GET)
  @LoggerManage(description = "加载合约查询页面")
  public Response getContractInfo(@PathVariable String exchangeName) {
    // 1. exchange query
    // 2. existing contract updating time/ number
    JSONObject data;
    if (QuantConstants.ALL.equals(exchangeName)) {
      data = updateAllExchangeContract();
    } else {
      data = updateSpecificExchangeContract(exchangeName);
    }

    JSONObject contractRecords = new JSONObject();
    contractRecords.put("contractRecords", data);
    return new ResponseData(ExceptionMsg.SUCCESS, contractRecords);
  }

  private JSONObject updateSpecificExchangeContract(String exchangeName) {
    String gatewayName = ExchangeEnum.valueOf(exchangeName).getGatewayName();
    if (StringUtils.isNotBlank(gatewayName)) {
      BaseGateway gateway = adminEngine.getGateway(gatewayName);
      if (gateway != null) {
        gateway.queryContract();
      } else {
        gateway = GatewayFactory.getGateway(gatewayName);
        if (gateway != null) {
          gateway.queryContract();
          adminEngine.addGateway(gateway);
          gateway.queryContract();
          adminEngine.connect(new ConcurrentHashMap<>(), gateway.getGatewayName());
        } else {
          return new JSONObject();
        }
      }

      InternalUtils.await(1000);
      List<ContractData> contractDataList = orderManagementSystem.getAllContractsData();
      int count = 0;
      for (ContractData contractData : contractDataList) {
        if (contractData.getExchange().name().equals(exchangeName)) {
          count++;
        }
      }

      JSONObject contractAndUpdateDate = new JSONObject();
      contractAndUpdateDate.put("contractNum", count);
      contractAndUpdateDate.put("lastUpdatedDate", new Date());
      JSONObject contractInfo = new JSONObject();
      contractInfo.put(exchangeName, contractAndUpdateDate);

      return contractInfo;
    } else {
      return new JSONObject();
    }
  }

  private JSONObject updateAllExchangeContract() {
    for (ExchangeEnum exchangeEnum : ExchangeEnum.values()) {
      if (ExchangeEnum.NONE.equals(exchangeEnum)) {
        continue;
      }
      String gatewayName = exchangeEnum.getGatewayName();
      if (StringUtils.isNotBlank(gatewayName)) {
        BaseGateway gateway = adminEngine.getGateway(gatewayName);
        if (gateway != null) {
          gateway.queryContract();
        } else {
          gateway = GatewayFactory.getGateway(gatewayName);
          if (gateway != null) {
            gateway.queryContract();
            adminEngine.addGateway(gateway);
            gateway.queryContract();
            adminEngine.connect(new ConcurrentHashMap<>(), gateway.getGatewayName());
          }
        }
      }
    }

    InternalUtils.await(1000);
    List<ContractData> contractDataList = orderManagementSystem.getAllContractsData();
    Map<String, Integer> exchangeContractNumMap = new ConcurrentHashMap<>();
    for (ContractData contractData : contractDataList) {
      String exchangeName = contractData.getExchange().name();
      if (exchangeContractNumMap.containsKey(exchangeName)) {
        int count = exchangeContractNumMap.get(exchangeName) + 1;
        exchangeContractNumMap.put(exchangeName, count);
      } else {
        exchangeContractNumMap.put(exchangeName, 1);
      }
    }

    JSONObject contractInfo = new JSONObject();
    for (Map.Entry<String, Integer> entry : exchangeContractNumMap.entrySet()) {
      JSONObject contractAndUpdateDate = new JSONObject();
      Date date = new Date();
      contractAndUpdateDate.put("contractNum", entry.getValue());
      contractAndUpdateDate.put("lastUpdatedDate", date);
      contractInfo.put(entry.getKey(), contractAndUpdateDate);
    }

    return contractInfo;
  }
}
