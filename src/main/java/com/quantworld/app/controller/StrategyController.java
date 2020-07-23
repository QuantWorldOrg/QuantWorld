package com.quantworld.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.broker.gateway.GatewayFactory;
import com.quantworld.app.comm.aop.LoggerManage;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.domain.result.ResponseData;
import com.quantworld.app.service.StrategyBasicParamGenerator;
import com.quantworld.app.trader.domain.StrategyEntity;
import com.quantworld.app.trader.engines.AdminEngine;
import com.quantworld.app.trader.oms.EventProcessor;
import com.quantworld.app.trader.oms.strategies.StrategyParam;
import com.quantworld.app.trader.repository.StrategyEntityDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Shawn
 * @Date: 1/16/2020
 * @Description:
 */
@RestController
@RequestMapping("/strategies")
public class StrategyController extends BaseController {

  @Autowired
  private StrategyEntityDAO strategyEntityDAO;

  @Autowired
  private EventProcessor eventProcessor;

  @Autowired
  private AdminEngine adminEngine;

  @Autowired
  private StrategyBasicParamGenerator strategyBasicParamGenerator;

  @RequestMapping(value = "/newStrategy/{name}", method = RequestMethod.POST)
  @LoggerManage(description = "新建策略")
  public Response addNewStrategy(@RequestBody String data, @PathVariable String name) {
    try {
      StrategyTemplateEnum templateEnum = StrategyTemplateEnum.valueOf(name.toUpperCase());
      JSONObject strategyParamJson = JSON.parseObject(data);

      String gatewayName = ExchangeEnum.valueOf((String) strategyParamJson.get("exchangeEnum")).getGatewayName();
      BaseGateway gateway = adminEngine.getGateway(gatewayName);
      Object strategyParam = JSON.toJavaObject(strategyParamJson, Class.forName(templateEnum.getParam()));
      long strategyTempAmount = strategyEntityDAO.getMaxCountByStrategyTemplate(name.toUpperCase(), getUserId()) + 1;
      name = templateEnum.getDisplayLabel() + "_" + strategyTempAmount;
      StrategyEntity strategyEntity = new StrategyEntity(name, (StrategyParam) strategyParam);
      strategyEntity.setUserId(getUserId());
      if (gateway == null) {
        gateway = GatewayFactory.getGateway(gatewayName);
        if (gateway != null) {
          strategyEntityDAO.save(strategyEntity);
          adminEngine.addGateway(gateway);
          // TODO some settings should be loaded from DB as some setting like access key or secret key will be updated
          //  often. Hence this setting should be updated from front-end. The params should be passed in to adminengine
          //  to help connect correctly.
          adminEngine.connect(new ConcurrentHashMap<>(), gateway.getGatewayName());
          return new ResponseData(ExceptionMsg.SUCCESS, strategyEntity.getUuid());
        } else {
          return new ResponseData(ExceptionMsg.FAILED);
        }
      } else {
        strategyEntityDAO.save(strategyEntity);
        adminEngine.addGateway(gateway);
        // TODO some settings should be loaded from DB as some setting like access key or secret key will be updated
        //  often. Hence this setting should be updated from front-end. The params should be passed in to adminengine
        //  to help connect correctly.
        adminEngine.connect(new ConcurrentHashMap<>(), gateway.getGatewayName());
        return new ResponseData(ExceptionMsg.SUCCESS, strategyEntity.getUuid());
      }
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  @RequestMapping(value = "/startStrategy", method = RequestMethod.POST)
  @LoggerManage(description = "运行策略")
  public Response startStrategy(@RequestBody String data) {
    StrategyEntity existingStrategy = null;
    String strategyName = StringUtils.EMPTY;
    try {
      existingStrategy = strategyEntityDAO.findByUUID((String) (JSON.parseObject(data)).get(StrategyEntity.ID));
      strategyName = (String) (JSON.parseObject(data)).get(StrategyEntity.STRATEGY_NAME);
      JSONObject strategyParamJson = (JSONObject) JSON.parseObject(data).get(StrategyEntity.STRATEGY_PARAM);
      StrategyParam strategyParam = (StrategyParam) JSON.toJavaObject(strategyParamJson,
          Class.forName(StrategyTemplateEnum.valueOf(strategyParamJson.get("strategyTemplateName").toString()).getParam()));
      strategyParam.setStrategyRunningFlag(true);
      if (existingStrategy == null) {
        if (strategyName != null) {
          existingStrategy = new StrategyEntity(strategyName, strategyParam);
          existingStrategy.setUserId(getUserId());
          eventProcessor.saveStrategySetting(existingStrategy);
        }
      } else {
        if (strategyName != null) {
          existingStrategy.setStrategyName(strategyName);
          existingStrategy.setStrategyParam(strategyParam);
          existingStrategy.setUserId(getUserId());
          eventProcessor.updateStrategySetting(existingStrategy);
        }
      }
      if (existingStrategy != null) {
        eventProcessor.startStrategy(strategyName, existingStrategy.getStrategyParam());
        return new ResponseData(ExceptionMsg.SUCCESS, existingStrategy.getUuid());
      } else {
        logger.error("无法执行策略！");
        return new ResponseData(ExceptionMsg.FAILED);
      }
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      assert existingStrategy != null;
      existingStrategy.getStrategyParam().setStrategyRunningFlag(false);
      eventProcessor.updateStrategySetting(existingStrategy);
      eventProcessor.stopStrategy(strategyName);
      return new ResponseData(ExceptionMsg.FAILED, e.getMessage());
    }
  }

  @RequestMapping(value = "/stopStrategy", method = RequestMethod.POST)
  @LoggerManage(description = "停止策略")
  public Response stopStrategy(@RequestBody String data) {
    try {
      StrategyEntity existingStrategy = strategyEntityDAO.findByUUID((String) (JSON.parseObject(data)).get(StrategyEntity.ID));
      String strategyName = (String) (JSON.parseObject(data)).get(StrategyEntity.STRATEGY_NAME);
      existingStrategy.getStrategyParam().setStrategyRunningFlag(false);
      eventProcessor.updateStrategySetting(existingStrategy);
      eventProcessor.stopStrategy(strategyName);
      return new ResponseData(ExceptionMsg.SUCCESS, existingStrategy.getUuid());
    } catch (Exception e) {
      logger.error("策略停止失败！", e);
      return new ResponseData(ExceptionMsg.FAILED, e.getMessage());
    }
  }

  @RequestMapping(value = "/deleteStrategy", method = RequestMethod.POST)
  @LoggerManage(description = "删除策略")
  public Response deleteStrategy(@RequestBody String data) {
    JSONObject dataJson = JSON.parseObject(data);
    String strategyName = (String) (dataJson).get(StrategyEntity.STRATEGY_NAME);
    eventProcessor.stopStrategy(strategyName);
    List<StrategyEntity> strategyEntityList = strategyEntityDAO.findAllStrategiesOrderByLastModifiedDateDesc(getUserId());

    if (strategyEntityDAO.deleteByStrategyName(strategyName, getUserId())) {
      return getDisplayPageUUID(strategyEntityList, dataJson);
    } else {
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  /**
   * Reorder page display list
   *
   * @param strategyEntityList
   * @param dataJson
   * @return
   */
  private ResponseData getDisplayPageUUID(List<StrategyEntity> strategyEntityList, JSONObject dataJson) {
    List<String> uuidList = new ArrayList<>();
    int count = 0;
    int index = 0;
    for (StrategyEntity strategyEntity : strategyEntityList) {
      if (strategyEntity.getUuid().equals(dataJson.get(StrategyEntity.ID))) {
        index = count;
      }
      count++;
      uuidList.add(strategyEntity.getUuid());
    }
    if (index == strategyEntityList.size() - 1) {
      index--;
    } else {
      index++;
    }
    if (index != -1) {
      return new ResponseData(ExceptionMsg.SUCCESS, uuidList.get(index));
    } else {
      return new ResponseData(ExceptionMsg.SUCCESS);
    }
  }

  @RequestMapping(value = "/getStrategies/{userId}", method = RequestMethod.POST)
  @LoggerManage(description = "获取策略")
  public List<StrategyEntity> getStrategies(@PathVariable("userId") Long userId) {
    List<StrategyEntity> strategyEntities = null;
    try {
      String id = getUserId();
      if (null != userId && 0 != userId) {
        id = Long.toString(userId);
      }
      strategyEntities = strategyEntityDAO.findAllStrategiesOrderByLastModifiedDateDesc(id);
    } catch (Exception e) {
      logger.error("获取策略失败", e);
    }
    return strategyEntities;
  }

  @RequestMapping(value = "/submitStrategy", method = RequestMethod.POST)
  @LoggerManage(description = "提交策略")
  public Response submitStrategy(Model model, String name) {
    try {
      model.addAttribute("name", name.toUpperCase());
      return new ResponseData(ExceptionMsg.SUCCESS);
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }

  @RequestMapping(value = "/generateBasicStrategyParam", method = RequestMethod.POST)
  @LoggerManage(description = "提交策略")
  public Response generateBasicStrategyParam(Model model, String name) {
    try {
      // According name to generate basic params
      model.addAttribute("basicStrategyParamJson", strategyBasicParamGenerator.getBasicStrategyParam(name));
      return new ResponseData(ExceptionMsg.SUCCESS);
    } catch (Exception e) {
      logger.error("策略保存失败！", e);
      return new ResponseData(ExceptionMsg.FAILED);
    }
  }
}

