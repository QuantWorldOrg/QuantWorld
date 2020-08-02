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
import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.comm.aop.LoggerManage;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.domain.Exchange;
import com.quantworld.app.domain.ProxyConfiguration;
import com.quantworld.app.repository.ExchangeRepository;
import com.quantworld.app.repository.ProxyRepository;
import com.quantworld.app.service.StrategyBasicParamGenerator;
import com.quantworld.app.trader.domain.StrategyEntity;
import com.quantworld.app.trader.engines.AdminEngine;
import com.quantworld.app.trader.oms.OrderManagementSystem;
import com.quantworld.app.trader.repository.StrategyEntityDAO;
import com.quantworld.app.utils.QuantStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.quantworld.app.constants.QuantConstants.ALL;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {
  @Autowired
  private StrategyEntityDAO strategyEntityDAO;

  @Autowired
  private StrategyBasicParamGenerator strategyBasicParamGenerator;

  @Autowired
  private AdminEngine adminEngine;

  @Autowired
  OrderManagementSystem orderManagementSystem;

  @Autowired
  private ExchangeRepository exchangeRepository;

  @Autowired
  private ProxyRepository proxyRepository;

  @RequestMapping(value = "/standard/exchange")
  @LoggerManage(description = "文章列表standard")
  public String exchange(Model model) {
    List<Exchange> exchangeList = exchangeRepository.findAll();
    model.addAttribute("exchangeList", exchangeList);
    return "/standard/exchange";
  }

  @RequestMapping(value = "/standard/proxy")
  @LoggerManage(description = "文章列表standard")
  public String proxy(Model model) {
    List<ProxyConfiguration> proxyConfigurations = proxyRepository.findAll();
    model.addAttribute("proxy", proxyConfigurations.isEmpty() ? new ProxyConfiguration() : proxyConfigurations.get(0));
    model.addAttribute("id", proxyConfigurations.isEmpty() ? StringUtils.EMPTY : proxyConfigurations.get(0).getId());
    model.addAttribute("proxyStatus", proxyConfigurations.isEmpty() ? false : proxyConfigurations.get(0).isStatus());
    return "/standard/proxy";
  }


  @RequestMapping(value = "/standard/strategyRepository")
  @LoggerManage(description = "文章列表standard")
  public String strategyRepository() {
    return "/standard/strategyRepository";
  }

  @RequestMapping(value = "/standard/{uuid}/{userId}")
  @LoggerManage(description = "文章列表standard")
  public String standard(Model model, @PathVariable("uuid") String uuid, @PathVariable("userId") long userId) throws IOException {
    StrategyEntity strategyEntity = strategyEntityDAO.findByUUID(uuid);
    String strategyParamFileName = strategyEntity.getStrategyParam().getStrategyTemplateName().toString().toLowerCase();
    InputStream inputStream = getClass().getResourceAsStream("/i18n/" + strategyParamFileName + ".properties");
    if (inputStream != null) {
      BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
      Properties properties = new Properties();
      properties.load(bf);
      JSONObject strategyJson = (JSONObject) JSON.toJSON(strategyEntity);
      model.addAttribute("properties", properties);
      model.addAttribute("uuid", uuid);
      model.addAttribute("strategyName", strategyJson.get("strategyName"));
      model.addAttribute("strategyRunningFlag", strategyEntity.getStrategyParam().getStrategyRunningFlag());
      model.addAttribute("mutableParameter", getMutableParameter(strategyJson.getJSONObject(StrategyEntity.STRATEGY_PARAM)));
      model.addAttribute("immutableParameter", getImmutableParameter(strategyJson.getJSONObject(StrategyEntity.STRATEGY_PARAM)));
      model.addAttribute("userId", getUserId());
      inputStream.close();
    }
    return "collect/standard";
  }

  /**
   * @param originalJSON
   * @return
   */
  private JSONObject getMutableParameter(JSONObject originalJSON) {
    JSONObject jsonObject = new JSONObject();
    for (Map.Entry<String, Object> entry : originalJSON.entrySet()) {
      if (entry.getValue() != null && QuantStringUtil.isNumeric(entry.getValue().toString())) {
        jsonObject.put(entry.getKey(), entry.getValue());
      }
    }
    return reorderJSONData(jsonObject);
  }

  /**
   * @param originalJSON
   * @return
   */
  private JSONObject getImmutableParameter(JSONObject originalJSON) {
    JSONObject jsonObject = new JSONObject();
    for (Map.Entry<String, Object> entry : originalJSON.entrySet()) {
      if (entry.getValue() != null && !QuantStringUtil.isNumeric(entry.getValue().toString())) {
        jsonObject.put(entry.getKey(), entry.getValue());
      }
    }
    return reorderJSONData(jsonObject);
  }

  private JSONObject reorderJSONData(JSONObject legacyJSONObject) {
    TreeMap<Object, Object> newStrategyParamMap = new TreeMap<>();
    for (Map.Entry<String, Object> entry : legacyJSONObject.entrySet()) {
      newStrategyParamMap.put(entry.getKey(), entry.getValue());
    }
    return (JSONObject) JSON.toJSON(newStrategyParamMap);
  }

  @RequestMapping(value = "/standard/submitStrategy", method = RequestMethod.POST)
  @LoggerManage(description = "提交策略")
  public String submitStrategy(Model model, String name) {
    try {
      model.addAttribute("basicStrategyParamJson", strategyBasicParamGenerator.getBasicStrategyParam(name));
      model.addAttribute("name", name);
      return "fragments/alert::generateStrategy";
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return "error";
    }
  }

  @RequestMapping(value = "/standard/startStrategyAlert", method = RequestMethod.POST)
  @LoggerManage(description = "提交策略")
  public String startStrategyAlert(Model model, @RequestBody String data) {
    try {
      JSONObject startStrategyData = QuantStringUtil.convertURLParamToJsonFormat(data);
      model.addAttribute("startStrategyData", startStrategyData);
      return "fragments/alert::startStrategy";
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return "error";
    }
  }


  @RequestMapping(value = "/standard/deleteStrategyAlert/{uuid}/{strategyName}", method = RequestMethod.GET)
  @LoggerManage(description = "提交策略")
  public String deleteStrategyAlert(Model model, @PathVariable String uuid, @PathVariable String strategyName) {
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(StrategyEntity.ID, uuid);
      jsonObject.put(StrategyEntity.STRATEGY_NAME, strategyName);
      model.addAttribute("deletedStrategyData", jsonObject);
      return "fragments/alert::deleteStrategy";
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return "error";
    }
  }

  @RequestMapping(value = "/standard/stopStrategyAlert/{uuid}/{strategyName}", method = RequestMethod.GET)
  @LoggerManage(description = "提交策略")
  public String stopStrategyAlert(Model model, @PathVariable String uuid, @PathVariable String strategyName) {
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(StrategyEntity.ID, uuid);
      jsonObject.put(StrategyEntity.STRATEGY_NAME, strategyName);
      model.addAttribute("stopStrategyData", jsonObject);
      return "fragments/alert::stopStrategy";
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return "error";
    }
  }

  @RequestMapping(value = "/standard/contract")
  @LoggerManage(description = "提交策略")
  public String loadContractQueryPage(Model model) {
    List<String> exchangeList = new ArrayList<>();
    for (ExchangeEnum exchangeEnum : ExchangeEnum.values()) {
      if (ExchangeEnum.NONE.equals(exchangeEnum)) {
        continue;
      }
      exchangeList.add(exchangeEnum.name());
    }
    Collections.sort(exchangeList);
    exchangeList.add(0, ALL);

    model.addAttribute("exchangeList", exchangeList);
    model.addAttribute("contractRecords", updateAllExchangeContract());
    return "/standard/contract";
  }

  @RequestMapping(value = "/standard/personalAsset")
  @LoggerManage(description = "提交策略")
  public String loadAccountQueryPage(Model model) {
    List<String> exchangeList = new ArrayList<>();
    for (ExchangeEnum exchangeEnum : ExchangeEnum.values()) {
      exchangeList.add(exchangeEnum.name());
    }
    Collections.sort(exchangeList);
    exchangeList.add(0, ALL);

    model.addAttribute("exchangeList", exchangeList);
    model.addAttribute("contractRecords", updateAllExchangeAccount());
    return "/standard/personalAsset";
  }

  private Object updateAllExchangeAccount() {
    return null;
  }

  private JSONObject updateAllExchangeContract() {

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
