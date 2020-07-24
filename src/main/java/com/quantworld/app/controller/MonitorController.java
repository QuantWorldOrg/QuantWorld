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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quantworld.app.domain.HistoricalTradeRecords;
import com.quantworld.app.domain.result.ExceptionMsg;
import com.quantworld.app.domain.result.Response;
import com.quantworld.app.domain.result.ResponseData;
import com.quantworld.app.repository.HistoricalTradeRecordsRepository;
import com.quantworld.app.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: Shawn
 * @Date: 1/16/2020
 * @Description:
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {
  Random random = new Random(47L);

  @Autowired
  private HistoricalTradeRecordsRepository historicalTradeRecordsRepository;

  /**
   * Get all trading data from 00:00:00 of today
   *
   * @return
   */
  @RequestMapping(value = "/getHistoricalTradeData/{strategyName}", method = RequestMethod.GET)
  @ResponseBody
  public List<Object> getAllTradeRecordsToday(@PathVariable String strategyName) {
    Timestamp startTimeStamp = new Timestamp(DateUtils.getDayBegin(new Date().getTime()));
    Timestamp endTimeStamp = new Timestamp(DateUtils.getDayBegin(new Date().getTime() + 86400000L));
    List<HistoricalTradeRecords> records = historicalTradeRecordsRepository.findByStrategyNameAndCreatedTimeAfterAndCreatedTimeBeforeOrderByCreatedTimeDesc(strategyName, startTimeStamp, endTimeStamp);
    JSONArray resArray = new JSONArray();
    for (HistoricalTradeRecords record : records) {
      JSONObject resIndex = (JSONObject) JSONObject.toJSON(record);
      resArray.add(resIndex);
    }
    return resArray;
  }

  /**
   * Return trading data and profit data
   *
   * @param strategyName
   * @return
   */
  @RequestMapping(value = "/getHistoricalTradingAndProfitData/{strategyName}", method = RequestMethod.GET)
  @ResponseBody
  private Response getAllStatisticalTradingRecord(@PathVariable String strategyName) {
//    tradingRecordGenerator();
    List records = historicalTradeRecordsRepository.getAllTradingAndProfitRecordGroupByCreateDate(strategyName);
    Map<String, List> historicalTradingAndProfitMap = new ConcurrentHashMap<>();
    List<Object> tradingDataList = new ArrayList<>();
    List<Object> profitDataList = new ArrayList<>();
    List<Long> allTradingVolume = new ArrayList<>();
    double allTradingVolumeSum = 0;
    List<Long> allProfitVolume = new ArrayList<>();
    double allProfitVolumeSum = 0;
    for (Object recordByDate : records) {
      Object[] tradingAndProfitDateByDate = (Object[]) recordByDate;
      List<Object> tradingData = new ArrayList<>();
      tradingData.add(tradingAndProfitDateByDate[2]);
      tradingData.add(tradingAndProfitDateByDate[0]);
      allTradingVolumeSum = allTradingVolumeSum + (double) tradingAndProfitDateByDate[0];
      tradingDataList.add(tradingData);
      List<Object> profitData = new ArrayList<>();
      profitData.add(tradingAndProfitDateByDate[2]);
      profitData.add(tradingAndProfitDateByDate[1]);
      allProfitVolumeSum = allProfitVolumeSum + (double) tradingAndProfitDateByDate[1];
      profitDataList.add(profitData);
    }
    allTradingVolume.add(Math.round(allTradingVolumeSum));
    allProfitVolume.add(Math.round(allProfitVolumeSum));
    historicalTradingAndProfitMap.put("TradeData", tradingDataList);
    historicalTradingAndProfitMap.put("ProfitData", profitDataList);
    historicalTradingAndProfitMap.put("allTradingVolume", allTradingVolume);
    historicalTradingAndProfitMap.put("allProfitVolume", allProfitVolume);
    return new ResponseData(ExceptionMsg.SUCCESS, historicalTradingAndProfitMap);
  }

  @RequestMapping(value = "/getHistoricalTradingAndProfitDataBetweenStartAndEndDate/{strategyName}/{startDate}/{endDate}",
      method = RequestMethod.GET)
  @ResponseBody
  private Response getHistoricalTradingAndProfitDataBetweenStartAndEndDate(Model model,
                                                                           @PathVariable String strategyName,
                                                                           @PathVariable String startDate,
                                                                           @PathVariable String endDate) {

    List records = historicalTradeRecordsRepository.getAllTradingAndProfitDataBetweenStartAndEndDateGroupByCreatedDate(strategyName,
        DateUtils.parse(DateUtils.YYYY_MM_DD, startDate, DateUtils.ASIA_SHANGHAI),
        DateUtils.parse(DateUtils.YYYY_MM_DD, endDate, DateUtils.ASIA_SHANGHAI));
    Map<String, List> historicalTradingAndProfitMap = new ConcurrentHashMap<>();
    List<Object> tradingDataList = new ArrayList<>();
    List<Object> profitDataList = new ArrayList<>();
    List<Long> allTradingVolume = new ArrayList<>();
    double allTradingVolumeSum = 0;
    List<Long> allProfitVolume = new ArrayList<>();
    double allProfitVolumeSum = 0;
    for (Object recordByDate : records) {
      Object[] tradingAndProfitDateByDate = (Object[]) recordByDate;
      List<Object> tradingData = new ArrayList<>();
      tradingData.add(tradingAndProfitDateByDate[2]);
      tradingData.add(tradingAndProfitDateByDate[0]);
      allTradingVolumeSum = allTradingVolumeSum + (double) tradingAndProfitDateByDate[0];
      tradingDataList.add(tradingData);
      List<Object> profitData = new ArrayList<>();
      profitData.add(tradingAndProfitDateByDate[2]);
      profitData.add(tradingAndProfitDateByDate[1]);
      allProfitVolumeSum = allProfitVolumeSum + (double) tradingAndProfitDateByDate[1];
      profitDataList.add(profitData);
    }
    allTradingVolume.add(Math.round(allTradingVolumeSum));
    allProfitVolume.add(Math.round(allProfitVolumeSum));
    historicalTradingAndProfitMap.put("TradeData", tradingDataList);
    historicalTradingAndProfitMap.put("ProfitData", profitDataList);
    historicalTradingAndProfitMap.put("allTradingVolume", allTradingVolume);
    historicalTradingAndProfitMap.put("allProfitVolume", allProfitVolume);
    return new ResponseData(ExceptionMsg.SUCCESS, historicalTradingAndProfitMap);
  }

  private void tradingRecordGenerator() {
    for (int i = 0; i < 1000; i++) {
      HistoricalTradeRecords historicalTradeRecords = new HistoricalTradeRecords();
      historicalTradeRecords.setDirection("买");
      historicalTradeRecords.setExchange("货币");
      historicalTradeRecords.setOffset("test");
      historicalTradeRecords.setOrderId("213124");
      historicalTradeRecords.setTradeId("34234525");
      historicalTradeRecords.setStrategyName("三角套利_" + random.nextInt(20));
      historicalTradeRecords.setVolume(random.nextInt(100));
      historicalTradeRecords.setPrice(random.nextFloat() + 10);
      historicalTradeRecords.setProfit(random.nextFloat());
      historicalTradeRecords.setSymbol("huobi.zil");
      historicalTradeRecords.setCreatedTime(new Timestamp(DateUtils.randomDate("2020-01-01", "2020-12-31").getTime()));
      historicalTradeRecordsRepository.save(historicalTradeRecords);
    }
  }
}
